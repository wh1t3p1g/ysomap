package ysomap.bullets;

/**
 * @author wh1t3P1g
 * @since 2021/6/13
 */
public interface Bullet<T> {

    T getObject() throws Exception;

    Bullet<T> set(String key, Object value) throws Exception;

    String get(String key);

    boolean has(String key);
}
