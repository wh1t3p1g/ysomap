package ysomap.payloads.xmldecoder;

import ysomap.payloads.AbstractPayload;

/**
 * @author wh1t3P1g
 * @since 2021/6/13
 */
public abstract class XMLDecoderPayload<T> extends AbstractPayload<T> {
    public XMLDecoderPayload() {
        serializeType = "xmldecoder";
        serializerOutputType = "console";
    }
}
