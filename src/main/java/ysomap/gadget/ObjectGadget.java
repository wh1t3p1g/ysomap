package ysomap.gadget;

/**
 * @author wh1t3P1g
 * @since 2020/2/15
 */
@SuppressWarnings({"rawtypes"})
public interface ObjectGadget <T> {

    T getObject() throws Exception;

    ObjectGadget set(String key, String value) throws Exception;
}
