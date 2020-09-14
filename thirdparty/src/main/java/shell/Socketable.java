package shell;

/**
 * @author wh1t3P1g
 * @since 2020/9/5
 */
public interface Socketable {

    void setRemote(String host, Integer port);

    void exploit();

    Socketable newInstance();
}
