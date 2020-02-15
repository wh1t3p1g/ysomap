package ysomap.gadget.wrapper;

import ysomap.gadget.ObjectGadget;
import ysomap.serializer.Serializer;

/**
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public class DeserializeFilterStream<T> extends FilterStream<Object> {

    private Serializer<T> serializer;

    public DeserializeFilterStream(ObjectGadget<Object> payload, Serializer<T> serializer) {
        super(payload);
        this.serializer = serializer;
    }

    @Override
    public Object getObject() throws Exception {
        Object obj = payload.getObject();
        return serializer.deserialize((T) obj);
    }
}
