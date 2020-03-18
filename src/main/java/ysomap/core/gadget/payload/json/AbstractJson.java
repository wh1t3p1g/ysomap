package ysomap.core.gadget.payload.json;

import ysomap.core.bean.Payload;
import ysomap.serializer.Serializer;
import ysomap.serializer.json.FastJsonSerializer;

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
