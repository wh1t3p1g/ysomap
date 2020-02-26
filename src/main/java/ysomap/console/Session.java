package ysomap.console;

import ysomap.gadget.ObjectGadget;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
public interface Session<T> {

    void accept(T name) throws Exception;

    boolean has(String key);

    void set(String key, Object value) throws Exception;

    String get(String key);

    void run() throws Exception;

    ObjectGadget getObj();

    void close() throws Exception;

    boolean isExit();
}
