package ysomap.gadget.bullet;

import ysomap.gadget.ObjectGadget;
import ysomap.util.Reflections;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
@SuppressWarnings({"rawtypes"})
public abstract class Bullet<T> implements ObjectGadget<T> {

    public Bullet(){ }

    @Override
    public ObjectGadget set(String key, Object value) throws Exception {
        Reflections.setFieldValue(this, key, value);
        return this;
    }
}
