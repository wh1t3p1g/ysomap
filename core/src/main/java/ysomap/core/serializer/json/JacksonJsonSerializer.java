package ysomap.core.serializer.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import ysomap.core.serializer.BaseSerializer;

/**
 * @author wh1t3P1g
 * @since 2021/3/7
 */
public class JacksonJsonSerializer extends BaseSerializer<String> {

    public String OUTPUT = "console";

    @Override
    public String serialize(Object obj) throws Exception {
        if(obj instanceof String){
            return (String) obj;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
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


    @Override
    public void setOutputType(String output) {
        OUTPUT = output;
    }
}
