package ysomap.common.exception;

/**
 * @author wh1t3P1g
 * @since 2020/3/5
 */
public class ArgumentsNotCompleteException extends BaseException {

    public ArgumentsNotCompleteException(String message) {
        super("[-] Argument("+message+") not completed");
    }
}
