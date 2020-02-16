package ysomap.gadget.payload;

import ysomap.gadget.ObjectGadget;

/**
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public abstract class Payload <T> implements ObjectPayload <T> {

    private ObjectGadget<T> bullet;

    public Payload(ObjectGadget<T> bullet) {
        this.bullet = bullet;
    }

    @Override
    public T getObject() throws Exception {
        T obj = bullet.getObject();
        if(checkObject(obj)){
            return pack(obj);
        }
        return null;
    }

    @Override
    public boolean checkObject(T obj) {
        return true;// 默认不检查也可以，如需检查重载该函数
    }
}
