package ysomap.common.exception;

import ysomap.common.util.ColorStyle;

/**
 * @author wh1t3P1g
 * @since 2020/2/20
 */
public class BaseRuntimeException extends RuntimeException {

    public BaseRuntimeException(String message) {
        super(ColorStyle.makeWordRed(message));
    }
}
