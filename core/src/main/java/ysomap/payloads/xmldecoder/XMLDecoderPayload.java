package ysomap.payloads.xmldecoder;

import ysomap.core.serializer.Serializer;
import ysomap.core.serializer.SerializerFactory;
import ysomap.payloads.AbstractPayload;

/**
 * @author wh1t3P1g
 * @since 2021/6/13
 */
public abstract class XMLDecoderPayload<T> extends AbstractPayload<T> {

    @Override
    public Serializer<?> getSerializer() {
        return SerializerFactory.createSerializer("xmldecoder");
    }
}
