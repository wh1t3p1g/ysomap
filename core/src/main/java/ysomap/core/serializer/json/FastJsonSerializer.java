package ysomap.core.serializer.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import ysomap.core.serializer.BaseSerializer;
import ysomap.core.serializer.Serializer;

/**
 * fastjson 序列化器
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public class FastJsonSerializer extends BaseSerializer<String> {

    public static Serializer serializer = new FastJsonSerializer();
    public static String OUTPUT = "console";
    public static String encoder = null;


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

}
