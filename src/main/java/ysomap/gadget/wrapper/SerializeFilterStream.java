package ysomap.gadget.wrapper;

import ysomap.gadget.ObjectGadget;
import ysomap.serializer.Serializer;

/**
 * 序列化流处理类，将Object转化为相应的序列化数据
 * 这个类必须被调用，当输出最终的payload时
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public class SerializeFilterStream<T> extends FilterStream<Object> {

    private Serializer<T> serializer;

    public SerializeFilterStream(ObjectGadget<Object> payload, Serializer<T> serializer) {
        super(payload);
        this.serializer = serializer;
    }

    @Override
    public T getObject() throws Exception {
        Object obj = payload.getObject();
        return serializer.serialize(obj);
    }
}
