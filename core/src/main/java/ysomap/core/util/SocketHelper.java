package ysomap.core.util;

import ysomap.common.util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;

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
            socket.setSoTimeout(5000);
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
        } catch (SocketTimeoutException e){
            Logger.error(String.format("connect %s:%s timeout!", host, port));
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
