package ysomap.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wh1t3P1g
 * @since 2020/2/20
 */
public class Logger {

    public static void success(String message){
        System.out.println(ColorStyle.makeWordGreen(String.format("[+] [%s] %s", getTime(), message)));
    }

    public static void error(String message){
        System.out.println(ColorStyle.makeWordRed(String.format("[-] [%s] %s", getTime(), message)));
    }

    public static void warn(String message){
        System.out.println(ColorStyle.makeWordBold(String.format(" *  [%s] %s",getTime(), message)));
    }

    public static void strongWarn(String message){
        System.out.println(ColorStyle.makeWordBoldAndUnderline(" *  "+message));
    }

    public static void normal(String message){
        if("\n".equals(message)){
            System.out.println("");
        }else{
            System.out.printf("    [%s] %s%n", getTime(), message);
        }
    }

    public static String getTime(){
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return ft.format(dNow);
    }


    public static void main(String[] args) {
        Logger.success("test");
        Logger.error("test");
        Logger.normal("test");
        Logger.normal("\n");
        Logger.warn("test");
    }
}
