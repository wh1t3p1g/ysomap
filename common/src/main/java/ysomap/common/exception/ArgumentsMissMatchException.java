package ysomap.common.exception;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
public class ArgumentsMissMatchException extends BaseException {

    public ArgumentsMissMatchException(String message) {
        super("[-] Arguments missing match, please use -> " + message);

    }
}
