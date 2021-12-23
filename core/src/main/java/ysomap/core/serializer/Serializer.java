package ysomap.core.serializer;

/**
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public interface Serializer<T> {

    T serialize(Object obj) throws Exception;

    Object deserialize(T obj) throws Exception;

    String getOutputType();

    String getEncoder();

    void setEncoder(String encoder);

    void setOutputType(String output);
}
