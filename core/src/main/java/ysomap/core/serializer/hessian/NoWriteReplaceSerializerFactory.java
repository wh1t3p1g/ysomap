package ysomap.core.serializer.hessian;

import com.caucho.hessian.io.*;

public class NoWriteReplaceSerializerFactory extends SerializerFactory {
    /**
     * {@inheritDoc}
     *
     * @see com.caucho.hessian.io.SerializerFactory#getObjectSerializer(java.lang.Class)
     */
    @Override
    public com.caucho.hessian.io.Serializer getObjectSerializer (Class<?> cl ) throws HessianProtocolException {
        return super.getObjectSerializer(cl);
    }


    /**
     * {@inheritDoc}
     *
     * @see com.caucho.hessian.io.SerializerFactory#getSerializer(java.lang.Class)
     */
    @Override
    public com.caucho.hessian.io.Serializer getSerializer (Class cl ) throws HessianProtocolException {
        com.caucho.hessian.io.Serializer serializer = super.getSerializer(cl);

        if ( serializer instanceof WriteReplaceSerializer) {
            return UnsafeSerializer.create(cl);
        }

        return serializer;
    }
}
