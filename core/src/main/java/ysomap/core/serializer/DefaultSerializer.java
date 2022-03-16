package ysomap.core.serializer;

import java.io.*;

/**
 * 默认产品 ObjectOutputStream的序列化器
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public class DefaultSerializer extends BaseSerializer<byte[]> {

    public String OUTPUT = "file";

    @Override
    public byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
        return out.toByteArray();
    }

    @Override
    public Object deserialize(byte[] obj) throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream(obj);
        ObjectInputStream objIn = new ObjectInputStream(in);
        return objIn.readObject();
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
