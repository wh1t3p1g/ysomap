package ysomap.payloads.hessian;

import ysomap.core.serializer.Serializer;
import ysomap.core.serializer.SerializerFactory;
import ysomap.payloads.AbstractPayload;

public abstract class HessianPayload extends AbstractPayload<Object> {
    public String serializeType;

    @Override
    public Serializer<?> getSerializer() {
        if(serializeType == null){
            return SerializerFactory.createSerializer("hessian");
        }else{
            return SerializerFactory.createSerializer(serializeType);
        }
    }

    @Override
    public Serializer<?> setSerializer(String type) {
        serializeType = type;
        return SerializerFactory.createSerializer(serializeType);
    }
}
