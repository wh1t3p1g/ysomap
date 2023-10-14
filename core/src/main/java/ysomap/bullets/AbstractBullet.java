package ysomap.bullets;

import ysomap.core.util.ReflectionHelper;

import java.util.Map;

/**
 * @author wh1t3p1g
 * @since 2021/12/24
 */
public abstract class AbstractBullet<T> implements Bullet<T> {

    @Override
    public Bullet<T> set(String key, Object value) throws Exception {
        return ReflectionHelper.set(this, key, value);
    }

    @Override
    public String get(String key) {
        return ReflectionHelper.get(this, key);
    }

    @Override
    public boolean has(String key) {
        return ReflectionHelper.has(this, key);
    }

    @Override
    public Map<String, String> getAllParameters() {
        Class<?> clazz = getClass();
        return ReflectionHelper.getAllRequireParameters(clazz, this);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String dump() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("use bullet %s\n", getClass().getSimpleName()));
        return sb.toString();
    }
}
