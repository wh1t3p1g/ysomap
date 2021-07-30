package ysomap.core.serializer;

/**
 * @author wh1t3p1g
 * @since 2021/7/30
 */
public abstract class BaseSerializer<T> implements Serializer<T> {

    public String ENCODER = null;

    @Override
    public String getEncoder() {
        return ENCODER;
    }

    @Override
    public void setEncoder(String encoder) {
        ENCODER = encoder;
    }
}
