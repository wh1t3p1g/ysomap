package ysomap.core.serializer;

/**
 * @author wh1t3p1g
 * @since 2021/12/2
 */
public class EmptySerializer extends BaseSerializer<byte[]>{

    public String OUTPUT = "file";

    @Override
    public byte[] serialize(Object obj) throws Exception {
        return (byte[]) obj;
    }

    @Override
    public Object deserialize(byte[] obj) throws Exception {
        return obj;
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
