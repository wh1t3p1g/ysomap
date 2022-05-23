package ysomap.core.serializer.hessian;

import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;

import java.lang.reflect.Method;

public class NoWriteReplaceSerializerFactory extends SerializerFactory {

    @Override
    public com.caucho.hessian.io.Serializer getSerializer (Class cl ) throws HessianProtocolException {
        Serializer serializer = super.getSerializer(cl);

        if(serializer != null && serializer.getClass().getName().equals("com.caucho.hessian.io.WriteReplaceSerializer")){
            try {
                Class<?> unsafe = Class.forName("com.caucho.hessian.io.UnsafeSerializer");
                Method create = unsafe.getMethod("create", Class.class);
                return (Serializer) create.invoke(unsafe, cl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return serializer;
    }
}
