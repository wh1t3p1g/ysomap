package ysomap.core.serializer.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import ysomap.core.serializer.BaseSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Hessian2Serializer extends BaseSerializer<byte[]> {

    public String OUTPUT = "file";

    @Override
    public byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(bos);
        NoWriteReplaceSerializerFactory sf = new NoWriteReplaceSerializerFactory();
        sf.setAllowNonSerializable(true);
        output.setSerializerFactory(sf);
        output.writeObject(obj);
        output.close();
        return bos.toByteArray();
    }

    @Override
    public Object deserialize(byte[] obj) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(obj);
        Hessian2Input input = new Hessian2Input(is);
        return input.readObject();
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
