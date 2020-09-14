package shell;

/**
 * @author wh1t3P1g
 * @since 2020/9/5
 */
public class ThreadRunner {

    public ThreadRunner(String host, Integer port, Socketable s){
        s.setRemote(host, port);
        s.exploit();
    }
}
