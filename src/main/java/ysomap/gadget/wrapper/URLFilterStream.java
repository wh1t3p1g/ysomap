package ysomap.gadget.wrapper;

import ysomap.gadget.ObjectGadget;

import java.net.URLEncoder;

/**
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public class URLFilterStream extends FilterStream<String> {

    public URLFilterStream(ObjectGadget<String> payload){
        super(payload);
    }

    @Override
    public String getObject() throws Exception {
        String obj = payload.getObject();
        return URLEncoder.encode(obj, "UTF-8");
    }
}
