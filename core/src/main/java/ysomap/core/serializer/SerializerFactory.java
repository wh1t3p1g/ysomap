package ysomap.core.serializer;

import ysomap.bullets.Bullet;
import ysomap.common.util.Logger;
import ysomap.core.ObjectInputFilterManager;
import ysomap.core.serializer.hessian.HessianSerializer;
import ysomap.core.serializer.json.FastJsonSerializer;
import ysomap.core.serializer.json.JacksonJsonSerializer;
import ysomap.core.serializer.xml.XMLDecoderSerializer;
import ysomap.core.serializer.xml.XStreamSerializer;
import ysomap.payloads.Payload;

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
            case "jackson":
                return JacksonJsonSerializer.serializer;
            case "xstream":
                return XStreamSerializer.serializer;
            case "xmldecoder":
                return XMLDecoderSerializer.serializer;
            case "hessian":
                return HessianSerializer.serializer;
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
                Logger.normal("\n");
            }
            if(out != null){

                serializer.serialize(obj, out);

                if(serializer.getOutputType().equals("console")){
                    Logger.normal("\n");
                }
                if(needClose){
                    out.close();
                }
            }
        }else {
            Logger.success(current + " not serializable, so do nothing");
        }
    }

    public static Object test(Payload payload, Bullet bullet) throws Exception {
        ObjectInputFilterManager.suspend();
        Serializer serializer = payload.getSerializer();
        payload.setBullet(bullet);
        Object obj = serializer.deserialize(serializer.serialize(payload.getObject()));
        ObjectInputFilterManager.setup();
        return obj;
    }
}
