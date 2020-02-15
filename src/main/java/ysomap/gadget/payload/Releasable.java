package ysomap.gadget.payload;

/**
 * @author mbechler
 *
 */
public interface Releasable<T> {
    void release(T obj) throws Exception;
}
