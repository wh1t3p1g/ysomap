package ysomap.core.serializer.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import ysomap.core.serializer.Serializer;

import java.io.OutputStream;

/**
 * @author wh1t3P1g
 * @since 2021/3/7
 */
public class JacksonJsonSerializer implements Serializer<String> {

    public static Serializer serializer = new JacksonJsonSerializer();
    public static String OUTPUT = "console";

    @Override
    public String serialize(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Override
    public void serialize(Object obj, OutputStream out) throws Exception {
        String result = serialize(obj);
        out.write("\n".getBytes());
        out.write(result.getBytes());
        out.write("\n".getBytes());
    }

    @Override
    public Object deserialize(String obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping();
        return objectMapper.readValue(obj, Object.class);
    }

    @Override
    public String getOutputType() {
        return OUTPUT;
    }
}
