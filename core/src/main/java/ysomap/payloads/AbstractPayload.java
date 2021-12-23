package ysomap.payloads;


import ysomap.bullets.Bullet;
import ysomap.common.exception.ObjectTypeErrorException;
import ysomap.common.util.Logger;
import ysomap.core.serializer.Serializer;
import ysomap.core.serializer.SerializerFactory;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3P1g
 * @since 2021/6/13
 */
public abstract class AbstractPayload<T> implements Payload<T>{

    public Bullet bullet;

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }

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

    public boolean checkObject(Object obj) {
        return true;// 默认不检查也可以，如需检查重载该函数
    }

    public Serializer<?> getSerializer(){
        return SerializerFactory.createSerializer("default");
    }
}
