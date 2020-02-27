package ysomap.exception;

import ysomap.util.ColorStyle;

/**
 * @author wh1t3P1g
 * @since 2020/2/26
 */
public class SessionIDNotFoundException extends Exception {

    public SessionIDNotFoundException(String id) {
        super("[-] Session ID("+ ColorStyle.makeWordRed(id) + ") not found, plz check again");
    }
}
