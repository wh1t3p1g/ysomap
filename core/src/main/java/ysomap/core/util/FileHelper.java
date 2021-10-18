package ysomap.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author wh1t3p1g
 * @since 2021/7/30
 */
public class FileHelper {

    public static byte[] getFileContent(String filepath) throws IOException {
        File file = new File(filepath);
        if(file.exists() && file.isFile()){
            return Files.readAllBytes(file.toPath());
        }
        throw new FileNotFoundException(filepath);
    }
}
