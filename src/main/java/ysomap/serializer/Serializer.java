package ysomap.serializer;

/**
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public interface Serializer<T> {

    T serialize(Object obj) throws Exception;

    Object deserialize(T obj) throws Exception;
}
