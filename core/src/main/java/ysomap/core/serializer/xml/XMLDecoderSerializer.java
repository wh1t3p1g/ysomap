package ysomap.core.serializer.xml;

import ysomap.core.serializer.Serializer;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author wh1t3P1g
 * @since 2021/4/6
 */
public class XMLDecoderSerializer implements Serializer<String> {

    public static Serializer serializer = new XMLDecoderSerializer();
    public static String OUTPUT = "console";

    @Override
    public String serialize(Object obj) throws Exception {
        return null;
    }

    @Override
    public void serialize(Object obj, OutputStream out) throws Exception {
        XMLEncoder encoder = new XMLEncoder(out);
        encoder.writeObject(obj);
        encoder.flush();
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

}
