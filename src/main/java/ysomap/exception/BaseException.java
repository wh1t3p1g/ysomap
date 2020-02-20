package ysomap.exception;

import ysomap.util.ColorStyle;

/**
 * @author wh1t3P1g
 * @since 2020/2/20
 */
public class BaseException extends Exception {

    public BaseException(String message) {
        super(ColorStyle.makeWordRed(message));
    }
}
