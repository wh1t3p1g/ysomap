package ysomap.core.serializer;

import ysomap.common.util.Logger;

import java.io.OutputStream;
import java.util.Base64;

/**
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public interface Serializer<T> {

    T serialize(Object obj) throws Exception;

    default void serialize(Object obj, OutputStream out) throws Exception{
        Object serialized = serialize(obj);
        byte[] serializedBytes;
        String encoder = getEncoder();
        if(serialized instanceof String){
            serializedBytes = ((String) serialized).getBytes();
        }else if(serialized instanceof byte[]){
            serializedBytes = (byte[]) serialized;
        }else{
            serializedBytes = new byte[0];
            Logger.error("no serialize type found!");
        }
        if("base64".equals(encoder)){
            serializedBytes = Base64.getEncoder().encode(serializedBytes);
        }

        out.write(serializedBytes);
    }

    Object deserialize(T obj) throws Exception;

    String getOutputType();

    String getEncoder();

    void setEncoder(String encoder);
}
