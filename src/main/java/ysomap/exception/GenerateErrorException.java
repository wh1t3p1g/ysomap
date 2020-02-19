package ysomap.exception;

import ysomap.util.ColorStyle;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
public class GenerateErrorException extends Exception {

    public GenerateErrorException(String type, String classname) {
        super("[-] generate "+type+"("+ ColorStyle.makeWordRed(classname) +") error");
    }
}
