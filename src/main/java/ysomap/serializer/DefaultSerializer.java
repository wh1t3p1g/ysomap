package ysomap.serializer;

import java.io.*;

/**
 * 默认产品 ObjectOutputStream的序列化器
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public class DefaultSerializer implements Serializer<byte[]> {

    public static String OUTPUT = "file";

    @Override
    public byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        serialize(obj, out);
        return out.toByteArray();
    }

    @Override
    public void serialize(Object obj, OutputStream out) throws Exception {
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
    }

    @Override
    public Object deserialize(byte[] obj) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(obj);
        ObjectInputStream objIn = new ObjectInputStream(in);
        return objIn.readObject();
    }

    @Override
    public String getOutputType() {
        return DefaultSerializer.OUTPUT;
    }
}
