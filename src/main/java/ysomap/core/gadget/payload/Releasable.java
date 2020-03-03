package ysomap.core.gadget.payload;

/**
 * @author mbechler
 *
 */
public interface Releasable<T> {
    void release(T obj) throws Exception;
}
