package ysomap.core.serializer.xml;

import ysomap.core.serializer.BaseSerializer;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author wh1t3P1g
 * @since 2021/4/6
 */
public class XMLDecoderSerializer extends BaseSerializer<String> {

    public String OUTPUT = "console";

    @Override
    public String serialize(Object obj) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(out);
        encoder.writeObject(obj);
        encoder.flush();
        return out.toString();
    }

    @Override
    public Object deserialize(String obj) throws Exception {
        InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(obj.getBytes()));
        XMLDecoder decoder = new XMLDecoder(inputStream);
        return decoder.readObject();
    }

    @Override
    public String getOutputType() {
        return OUTPUT;
    }

    @Override
    public void setOutputType(String output) {
        OUTPUT = output;
    }
}
