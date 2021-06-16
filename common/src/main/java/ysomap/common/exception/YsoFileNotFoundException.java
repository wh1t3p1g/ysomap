package ysomap.common.exception;

/**
 * @author wh1t3P1g
 * @since 2021/6/16
 */
public class YsoFileNotFoundException extends BaseException{

    public YsoFileNotFoundException(String message) {
        super("[-] script file not found. Filepath: "+message);
    }
}
