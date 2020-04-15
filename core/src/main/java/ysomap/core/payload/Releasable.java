package ysomap.core.payload;

/**
 * @author mbechler
 *
 */
public interface Releasable<T> {
    void release(T obj) throws Exception;
}
