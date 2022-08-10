package ysomap.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author wh1t3p1g
 * @since 2021/7/30
 */
public class FileHelper {

    public static byte[] fileGetContent(String filepath) throws IOException {
        File file = new File(filepath);
        if(file.exists() && file.isFile()){
            return Files.readAllBytes(file.toPath());
        }
        throw new FileNotFoundException(filepath);
    }

    public static void filePutContent(String filepath, byte[] data) throws IOException {
        if (filepath != null && data != null) {
            try (OutputStream outputStream = Files.newOutputStream(Paths.get(filepath))) {
                outputStream.write(data);
            } catch (IOException ignored) {
            }
            return;
        }
        throw new FileNotFoundException(filepath);
    }
}
