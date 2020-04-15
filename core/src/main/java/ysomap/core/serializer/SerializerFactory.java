package ysomap.core.serializer;

import ysomap.common.util.Logger;
import ysomap.core.serializer.json.FastJsonSerializer;
import ysomap.core.serializer.xml.XStreamSerializer;

import java.io.FileOutputStream;
import java.io.OutputStream;

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
                return FastJsonSerializer.serializer;
            case "jackjson":
                return null;
            case "xstream":
                return XStreamSerializer.serializer;
            default:
                return DefaultSerializer.serializer;
        }
    }

    @SuppressWarnings({"rawtypes"})
    public static void serialize(String current, Serializer serializer, Object obj) throws Exception {
        if(serializer != null){
            OutputStream out = null;
            boolean needClose = false;
            if(serializer.getOutputType().endsWith("file")){
                out = new FileOutputStream("obj.ser");
                needClose = true;
                Logger.success("generate " + current + " success, plz see obj.ser");
            }else if(serializer.getOutputType().equals("console")){
                out = System.out;
            }
            if(out != null){
                if(obj instanceof String){
                    out.write(((String) obj).getBytes());
                }else{
                    // 非String类型的需要进行序列化操作
                    serializer.serialize(obj, out);
                }
                if(needClose){
                    out.close();
                }
            }
        }else {
            Logger.success(current + " not serializable, so do nothing");
        }
    }
}
