package ysomap.payloads.hessian;

import ysomap.core.serializer.Serializer;
import ysomap.core.serializer.SerializerFactory;
import ysomap.payloads.AbstractPayload;

public abstract class HessianPayload extends AbstractPayload<Object> {

    @Override
    public Serializer<?> getSerializer() {
        return SerializerFactory.createSerializer("hessian");
    }

}
