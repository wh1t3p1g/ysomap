package ysomap.serializer.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import ysomap.serializer.Serializer;

import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * fastjson 序列化器
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public class FastJsonSerializer implements Serializer<String> {

    @Override
    public String serialize(Object obj) throws Exception {
        return JSON.toJSONString(obj, SerializerFeature.WriteClassName);
    }

    @Override
    public void serialize(Object obj, OutputStream out) throws Exception {
        String result = serialize(obj);
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(result);
    }

    @Override
    public Object deserialize(String obj) throws Exception {
        return JSON.parseObject(obj, Feature.SupportNonPublicField);
    }
}
