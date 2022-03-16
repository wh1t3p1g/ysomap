package ysomap.core.serializer.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import ysomap.core.serializer.BaseSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class HessianSerializer extends BaseSerializer<byte[]> {

    public String OUTPUT = "file";

    @Override
    public byte[] serialize(Object obj) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        HessianOutput output = new HessianOutput(bos);
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
        HessianInput input = new HessianInput(is);
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
