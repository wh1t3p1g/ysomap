package ysomap.core.bean;

import ysomap.common.exception.ObjectTypeErrorException;
import ysomap.common.util.Logger;
import ysomap.core.ObjectGadget;
import ysomap.core.ObjectPayload;
import ysomap.core.serializer.Serializer;
import ysomap.core.serializer.SerializerFactory;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3P1g
 * @since 2020/2/15
 */
@SuppressWarnings({"rawtypes"})
public abstract class Payload <T> implements ObjectPayload<T> {

    public Bullet bullet;

    @Override
    final public T getObject() throws Exception {
        // check bullet args first
        ReflectionHelper.checkClassFieldsNotNull(bullet);
        // start to generate payload
        Logger.success("generate payload("+this.getClass().getSimpleName()+") started!");
        Object obj = bullet.getObject();
        if(checkObject(obj)){
            // arm bullet
            T retObj = pack(obj);
            Logger.success("generate payload("+this.getClass().getSimpleName()+") done!");
            return retObj;
        }
        throw new ObjectTypeErrorException(obj);
    }

    @Override
    public boolean checkObject(Object obj) {
        return true;// 默认不检查也可以，如需检查重载该函数
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    @Override
    public ObjectGadget set(String key, Object value) throws Exception {
        ReflectionHelper.setFieldValue(this, key, value);
        return this;
    }

    public abstract Bullet getDefaultBullet(String command) throws Exception;

    public Serializer<?> getSerializer(){
        return SerializerFactory.createSerializer("default");
    }
}
