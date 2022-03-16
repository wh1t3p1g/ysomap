package ysomap.payloads.json;

import ysomap.payloads.AbstractPayload;

/**
 * @author wh1t3P1g
 * @since 2020/3/18
 */
public abstract class FastJsonPayload extends AbstractPayload<Object> {

    public FastJsonPayload() {
        serializeType = "fastjson";
        serializerOutputType = "console";
    }
}
