package ysomap.core.util;

import ysomap.common.util.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

/**
 * @author wh1t3p1g
 * @since 2021/11/22
 */
public class SocketHelper {

    public static String sendAndReceive(String host, int port, byte[] bytes, int timeout){
        byte[] ret = send(host, port, bytes, timeout);
        return new String(ret);
    }

    public static byte[] send(String host, int port, byte[] bytes, int timeout){
        try(Socket socket = new Socket()){
            socket.setSoTimeout(timeout);
            socket.connect(new InetSocketAddress(host, port), timeout);
            Logger.normal(String.format("Connected %s:%s success!", host, port));
            OutputStream output = socket.getOutputStream();

            output.write(bytes);
            output.flush();

            InputStream input = socket.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] dataReceived = new byte[1024];
            int bytesRead;

            try{
                while((bytesRead = input.read(dataReceived)) != -1){
                    baos.write(dataReceived, 0, bytesRead);
                }
            }catch (SocketTimeoutException ig){
                // read all bytes until read timeout
            }

            output.close();
            input.close();
            return baos.toByteArray();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (SocketTimeoutException e){
            Logger.error(String.format("connect %s:%s timeout!", host, port));
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
