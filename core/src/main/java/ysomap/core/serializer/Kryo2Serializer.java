package ysomap.core.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * @author wh1t3p1g
 * @since 2022/3/7
 */
public class Kryo2Serializer extends BaseSerializer<byte[]>{

    public String OUTPUT = "file";
    public static Kryo kryo = null;

    @Override
    public byte[] serialize(Object obj) throws Exception {
        Output output = null;
        try{
            output = new Output();
            kryo.writeClassAndObject(output, obj);
            return output.toBytes();
        } finally {
            if(output != null){
                output.close();
            }
        }
    }

    @Override
    public Object deserialize(byte[] obj) throws Exception {
        Input input = null;
        try{
            input = new Input(obj);
            return kryo.readClassAndObject(input);
        }finally {
            if(input != null){
                input.close();
            }
        }
    }

    @Override
    public String getOutputType() {
        return OUTPUT;
    }

    @Override
    public void setOutputType(String output) {
        OUTPUT = output;
    }

    static {
        kryo = new Kryo();
    }
}
