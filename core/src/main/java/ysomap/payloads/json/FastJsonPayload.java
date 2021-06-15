package ysomap.payloads.json;

import ysomap.core.serializer.Serializer;
import ysomap.core.serializer.SerializerFactory;
import ysomap.payloads.AbstractPayload;

/**
 * @author wh1t3P1g
 * @since 2020/3/18
 */
public abstract class FastJsonPayload extends AbstractPayload<Object> {

    @Override
    public Serializer<?> getSerializer() {
        return SerializerFactory.createSerializer("fastjson");
    }
}
