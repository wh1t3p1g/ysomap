package ysomap.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author wh1t3P1g
 * @since 2020/2/17
 */
public class OutputHelper {

    public static void writeToFile(String filename, Object body){
        try (BufferedWriter bw =
                     new BufferedWriter(new FileWriter(filename))){
            bw.write(body.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String urlEncode(String body) throws UnsupportedEncodingException {
        return URLEncoder.encode(body, "UTF-8");
    }
}
