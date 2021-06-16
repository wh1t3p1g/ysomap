package ysomap.cli;

import java.util.Collections;

/**
 * @author wh1t3P1g
 * @since 2021/6/13
 */
public class App {

    public static void main(String[] args) {
        Console console = new Console();
        console.init();
        if(args.length == 1 && "cli".equals(args[0])){
            console.run();
        }else if(args.length == 2 && "script".equals(args[0])){
            console.setArgs(Collections.singletonList(args[1]));
            try {
                console.script();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
