package ysomap.core.serializer.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import ysomap.core.serializer.BaseSerializer;

/**
 * fastjson 序列化器
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public class FastJsonSerializer extends BaseSerializer<String> {

    public String OUTPUT = "console";

    @Override
    public String serialize(Object obj) throws Exception {
        if(obj instanceof String){
            return (String) obj;
        }
        return JSON.toJSONString(obj, SerializerFeature.WriteClassName);
    }


    @Override
    public Object deserialize(String obj) throws Exception {
        ParserConfig config = new ParserConfig();
        return JSON.parseObject(obj, Object.class, config, Feature.SupportNonPublicField);
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
