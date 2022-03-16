package ysomap.payloads.xstream;

import ysomap.payloads.AbstractPayload;

/**
 * @author wh1t3P1g
 * @since 2021/6/13
 */
public abstract class XStreamPayload<T> extends AbstractPayload<T> {
    public XStreamPayload() {
        serializeType = "xstream";
        serializerOutputType = "console";
    }
}
