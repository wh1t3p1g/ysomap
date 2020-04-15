package ysomap.common.exception;

import ysomap.common.util.ColorStyle;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
public class YsoClassNotFoundException extends BaseException {

    public YsoClassNotFoundException(String type, String classname) {
        super("[-] "+type+"("+ ColorStyle.makeWordRed(classname) + ") not found, plz check again");
    }
}
