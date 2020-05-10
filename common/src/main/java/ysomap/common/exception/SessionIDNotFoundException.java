package ysomap.common.exception;

/**
 * @author wh1t3P1g
 * @since 2020/2/26
 */
public class SessionIDNotFoundException extends BaseException {

    public SessionIDNotFoundException(String id) {
        super("[-] Session ID("+ id + ") killing failed, plz check again");
    }
}
