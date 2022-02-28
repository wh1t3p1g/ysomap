package ysomap.core.serializer.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import ysomap.common.util.Logger;
import ysomap.core.serializer.BaseSerializer;
import ysomap.core.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Hessian2Serializer extends BaseSerializer<byte[]> {

    public static Serializer serializer = new Hessian2Serializer();
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
        Logger.success("Hessian2Serializer running.");
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
