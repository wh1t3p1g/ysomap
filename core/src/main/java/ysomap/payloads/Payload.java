package ysomap.payloads;

import ysomap.bullets.Bullet;
import ysomap.core.serializer.Serializer;

/**
 * @author wh1t3P1g
 * @since 2021/6/13
 */
public interface Payload<T> {

    T getObject() throws Exception;

    void setBullet(Bullet bullet);

    boolean checkObject(Object obj);

    Serializer<?> getSerializer();

    Bullet getDefaultBullet(Object... args) throws Exception;

    void setSerializeType(String serializeType);

    void setEncoder(String encoder);

    void setOutputType(String outputType);

    void setSerialVersionUID(String uid);

    String getSerializeType();

    String getEncoder();

    String getOutputType();

    String getSerialVersionUID();
    /**
     * 装弹，将最终达成的利用效果拼接反序列化利用链
     * 反序列化利用链的实现也在这部分实现
     * @param obj
     * @return
     */
    T pack(Object obj) throws Exception;

    Payload<T> set(String key, Object value) throws Exception;

    String get(String key);

    boolean has(String key);

    String getName();
}
