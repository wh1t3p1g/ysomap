package ysomap.common.exception;

/**
 * @author wh1t3P1g
 * @since 2020/2/17
 */
public class ObjectTypeErrorException extends BaseException {

    public ObjectTypeErrorException(Object obj) {
        super("Bullet Type Not Match; Error TYPE: "+obj.getClass());
    }
}
