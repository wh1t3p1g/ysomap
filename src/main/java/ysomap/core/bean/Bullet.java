package ysomap.core.bean;

import ysomap.core.ObjectGadget;
import ysomap.util.ReflectionHelper;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
@SuppressWarnings({"rawtypes"})
public abstract class Bullet<T> implements ObjectGadget<T> {

    public Bullet(){ }

    @Override
    public ObjectGadget set(String key, Object value) throws Exception {
        ReflectionHelper.setFieldValue(this, key, value);
        return this;
    }
}
