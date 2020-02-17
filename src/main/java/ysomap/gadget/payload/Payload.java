package ysomap.gadget.payload;

import ysomap.gadget.ObjectGadget;
import ysomap.exception.ObjectTypeErrorException;

/**
 * @author wh1t3P1g
 * @since 2020/2/15
 */
@SuppressWarnings({"rawtypes"})
public abstract class Payload <T> implements ObjectPayload <T> {

    private ObjectGadget bullet;

    public Payload(){ }

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
}
