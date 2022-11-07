package ysomap.core.util;

import ysomap.common.util.Logger;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class JarHelper {
    public static String CommonsBeanutils182 = "commons-beanutils-1.8.2.jar";
    
    private static URLClassLoader classLoader = null;
    
    // 获取CommonsBeanuitls指定版本的指定类
    public static Class getClassFromJar(String version, String className) {
        if (CommonsBeanutils182.equals(version)) {
            getURLClassLoader(CommonsBeanutils182);
        }
        
        if (classLoader == null) {
            Logger.error("Not Found " + version);
            return null;
        }
        
        Class clazz = loadClass(className);
        if (clazz != null) {
            return clazz;
        } else {
            Logger.error("not found Class: " + className);
            return null;
        }
    }
    
    // 通过jar包获取Class对象，当前lib目录下没有jar包则会自动创建、生成
    private static void getURLClassLoader(String jarName) {
        String libPath = System.getProperty("user.dir") + File.separator + "lib";
        String jarPath = libPath + File.separator + jarName;   // 当前目录下的jar目录
        File file = new File(jarPath);
        if (!file.exists()) {
        
        }
        URL url = null;
        try {
            url = new File(jarPath).toURI().toURL();
            
            File lib = new File(libPath);
            if (!lib.exists()) {
                lib.mkdir();
                Logger.normal("Create lib dir");
            }
            
            if (!file.exists()) {
                InputStream is = JarHelper.class.getResourceAsStream("/" + jarName);    // 从resources中获取jar包二进制流
                if (is == null) {
                    Logger.error("Not found jar: " + jarName);
                    return;
                }
                FileOutputStream fileOutputStream = new FileOutputStream(jarPath);
                byte[] bytes = new byte[1024 * 1024 * 10];
                int bytesLength = is.read(bytes);
                fileOutputStream.write(bytes, 0, bytesLength);  // 写入到lib下，注意要控制字节的大小
                Logger.normal("Write " + jarName + " to ./lib");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        classLoader = new URLClassLoader(new URL[]{url});
    }
    
    // 从加载器中读取类
    public static Class loadClass(String className) {
        try {
            Method method = URLClassLoader.class.getDeclaredMethod("findClass", String.class);
            method.setAccessible(true);
            Class clazz = (Class) method.invoke(classLoader, className);
            return clazz;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
        Class clazz = getClassFromJar(JarHelper.CommonsBeanutils182, "org.apache.commons.beanutils.BeanComparator");
        System.out.println(clazz.getName());
    }
}

