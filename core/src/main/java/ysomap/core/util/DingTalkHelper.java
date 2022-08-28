package ysomap.core.util;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wh1t3p1g
 * @since 2022/5/23
 */
public class DingTalkHelper {

    public static void send(String token, String keyword, String data) throws IOException {
        Map<String, String> content = new HashMap<>();
        Map<String, String> text = new HashMap<>();
        text.put("content", keyword + ": " + data);
        content.put("msgtype", "text");
        content.put("text", JSON.toJSONString(text));
        URL url = new URL("https://oapi.dingtalk.com/robot/send?access_token="+token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoOutput(true);
        data = JSON.toJSONString(content);
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = data.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int code = con.getResponseCode();
        if (code == 200) {
//                try(InputStream is = con.getInputStream()){
//                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//                    StringBuffer sb = new StringBuffer();
//                    String temp = null;
//                    // 循环遍历一行一行读取数据
//                    while ((temp = br.readLine()) != null) {
//                        sb.append(temp);
//                        sb.append("\r\n");
//                    }
//                    System.out.println(sb);
//                }
        }
    }
}
