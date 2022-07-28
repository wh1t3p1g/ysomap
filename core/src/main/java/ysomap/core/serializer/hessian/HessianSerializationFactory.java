package ysomap.core.serializer.hessian;

import com.caucho.hessian.io.*;

/**
 * @author wh1t3p1g
 * @since 2022/5/16
 */
public class HessianSerializationFactory extends SerializerFactory {

    public HessianSerializationFactory() {
    }

    protected Serializer getDefaultSerializer(Class cl) {
        return (Serializer)(new JavaSerializer(cl));
    }

    protected Deserializer getDefaultDeserializer(Class cl) {
        if (Throwable.class.isAssignableFrom(cl)) {
            return new JavaDeserializer(cl);
        } else {
            return (Deserializer)(new JavaDeserializer(cl));
        }
    }

}
