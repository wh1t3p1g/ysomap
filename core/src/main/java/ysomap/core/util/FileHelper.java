package ysomap.core.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author wh1t3p1g
 * @since 2021/7/30
 */
public class FileHelper {

    public static byte[] getFileContent(String filepath) throws IOException {
        File file = new File(filepath);
        if(file.exists() && file.isFile()){
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String content = "";
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content += line;
            }
            return content.getBytes(StandardCharsets.UTF_8);
        }
        throw new FileNotFoundException(filepath);
    }
}
