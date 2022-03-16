package ysomap.payloads.hessian;

import ysomap.payloads.AbstractPayload;

public abstract class HessianPayload extends AbstractPayload<Object> {

    public HessianPayload() {
        serializeType = "hessian";
        serializerOutputType = "file";
    }
}
