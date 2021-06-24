package ysomap.common.util;

/**
 * @author wh1t3P1g
 * @since 2021/6/24
 */
public enum Status {
    RUNNING("running"),
    STOPPED("stopped"),
    INIT("init");

    String value;

    Status(String value){
        this.value = value;
    }
}
