package ysomap.core.serializer;

import ysomap.common.exception.ArgumentsMissMatchException;
import ysomap.payloads.Payload;

import java.util.Map;

/**
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public interface Serializer<T> {

    T serialize(Object obj) throws Exception;
    T serialize(Payload payload) throws Exception;

    Object deserialize(T obj) throws Exception;

    String getOutputType();

    String getEncoder();

    void setEncoder(String encoder);

    void setOutputType(String output);

    void setSerialVersionUID(String UIDMap) throws ArgumentsMissMatchException;

    Map<String, String> getSerialVersionUID();
}
