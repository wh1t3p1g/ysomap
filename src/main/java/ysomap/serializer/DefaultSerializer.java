package ysomap.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 默认产品 ObjectOutputStream的序列化器
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public class DefaultSerializer implements Serializer<byte[]> {

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

}
