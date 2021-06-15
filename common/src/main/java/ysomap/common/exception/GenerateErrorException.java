package ysomap.common.exception;

import ysomap.common.util.ColorStyle;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
public class GenerateErrorException extends BaseException {

    public GenerateErrorException(String type, String classname) {
        super("[-] generate "+type+"("+ ColorStyle.makeWordRed(classname) +") error");
    }

    public GenerateErrorException(String info) {
        super("[-] generate "+ ColorStyle.makeWordRed(info) +" error");
    }
}
