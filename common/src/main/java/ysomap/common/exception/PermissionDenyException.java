package ysomap.common.exception;

/**
 * @author wh1t3p1g
 * @since 2021/12/29
 */
public class PermissionDenyException extends BaseRuntimeException {
    public PermissionDenyException(String message) {
        super("[-] permission deny for " + message);
    }
}
