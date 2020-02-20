package ysomap.util;

/**
 * @author wh1t3P1g
 * @since 2020/2/20
 */
public class Logger {

    public static void success(String message){
        System.out.println(ColorStyle.makeWordGreen(message));
    }

    public static void error(String message){
        System.out.println(ColorStyle.makeWordRed(message));
    }

    public static void warn(String message){
        System.out.println(ColorStyle.makeWordBold(message));
    }

    public static void strongWarn(String message){
        System.out.println(ColorStyle.makeWordBoldAndUnderline(message));
    }

    public static void normal(String message){
        System.out.println(message);
    }

}
