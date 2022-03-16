package ysomap.core.serializer;

import ysomap.bullets.Bullet;
import ysomap.common.util.Logger;
import ysomap.common.util.Strings;
import ysomap.core.serializer.hessian.Hessian2Serializer;
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
                return new FastJsonSerializer();
            case "jackson":
                return new JacksonJsonSerializer();
            case "xstream":
                return new XStreamSerializer();
            case "xmldecoder":
                return new XMLDecoderSerializer();
            case "hessian":
                return new HessianSerializer();
            case "hessian2":
                return new Hessian2Serializer();
            case "empty":
                return new EmptySerializer();
            case "kyro":
                return new KryoSerializer();
            default:
                return new DefaultSerializer();
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

                serialize(serializer, obj, out);

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

    private static void serialize(Serializer serializer, Object obj, OutputStream out) throws Exception{
        Object serialized = serializer.serialize(obj);
        byte[] serializedBytes;
        String encoder = serializer.getEncoder();
        if(serialized instanceof String){
            serializedBytes = ((String) serialized).getBytes();
        }else if(serialized instanceof byte[]){
            serializedBytes = (byte[]) serialized;
        }else{
            serializedBytes = new byte[0];
            Logger.error("no serialize type found!");
        }
        if("base64".equals(encoder)){
            serializedBytes = Strings.base64(serializedBytes);
        }

        out.write(serializedBytes);
    }

    public static Object test(Payload payload, Bullet bullet) throws Exception {
        Serializer serializer = payload.getSerializer();
        payload.setBullet(bullet);
        Object obj = serializer.deserialize(serializer.serialize(payload.getObject()));
        return obj;
    }
}
