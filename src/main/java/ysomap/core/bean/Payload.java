package ysomap.core.bean;

import ysomap.core.ObjectPayload;
import ysomap.exception.ObjectTypeErrorException;
import ysomap.core.ObjectGadget;
import ysomap.serializer.Serializer;
import ysomap.serializer.SerializerFactory;
import ysomap.util.ReflectionHelper;

/**
 * @author wh1t3P1g
 * @since 2020/2/15
 */
@SuppressWarnings({"rawtypes"})
public abstract class Payload <T> implements ObjectPayload<T> {

    public ObjectGadget bullet;

    @Override
    final public T getObject() throws Exception {
        Object obj = bullet.getObject();
        if(checkObject(obj)){
            return pack(obj);
        }
        throw new ObjectTypeErrorException(obj);
    }

    @Override
    public boolean checkObject(Object obj) {
        return true;// 默认不检查也可以，如需检查重载该函数
    }

    public void setBullet(ObjectGadget bullet) {
        this.bullet = bullet;
    }

    @Override
    public ObjectGadget set(String key, Object value) throws Exception {
        ReflectionHelper.setFieldValue(this, key, value);
        return this;
    }

    public abstract ObjectGadget getDefaultBullet(String command) throws Exception;

    public Serializer<?> getSerializer(){
        return SerializerFactory.createSerializer("default");
    }
}
