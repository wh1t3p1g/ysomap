package ysomap.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author wh1t3p1g
 * @since 2021/11/22
 */
public class SocketHelper {

    public static String send(String host, int port, byte[] bytes){
        Socket socket = null;
        BufferedReader in = null;
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socket.getOutputStream().write(bytes);
            String resp = null;
            StringBuilder ret = new StringBuilder();
            do{
                resp = in.readLine();
                if(resp != null){
                    ret.append(resp);
                }
            }while (resp != null);

            return ret.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                in.close();
            } catch (Exception e) {
                // do nothing
            }
        }
        return null;
    }
}
