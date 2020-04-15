package ysomap.core.payload.json;

import ysomap.core.bean.Payload;
import ysomap.core.serializer.Serializer;
import ysomap.core.serializer.json.FastJsonSerializer;

/**
 * @author wh1t3P1g
 * @since 2020/3/18
 */
public abstract class AbstractJson extends Payload<Object> {

    @Override
    public Serializer<?> getSerializer() {
        return new FastJsonSerializer();
    }
}
