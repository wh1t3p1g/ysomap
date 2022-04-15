package ysomap.core.serializer.hessian;

import com.caucho.hessian.io.*;

public class NoWriteReplaceSerializerFactory extends SerializerFactory {

    @Override
    public com.caucho.hessian.io.Serializer getSerializer (Class cl ) throws HessianProtocolException {
        Serializer serializer = super.getSerializer(cl);

        if ( serializer instanceof WriteReplaceSerializer) {
            return UnsafeSerializer.create(cl);
        }

        return serializer;
    }
}
