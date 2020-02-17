package ysomap.serializer;

import ysomap.serializer.json.FastJsonSerializer;

/**
 * Serializer 工厂类
 * 用于生产各类序列化器，如FastJson、JackJson等等
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public class SerializerFactory {

    public static Serializer<?> createSerializer(String type){
        switch( type ){
            case "fastjson":
                return new FastJsonSerializer();
            case "jackjson":
                return null;
            default:
                return new DefaultSerializer();
        }
    }
}
