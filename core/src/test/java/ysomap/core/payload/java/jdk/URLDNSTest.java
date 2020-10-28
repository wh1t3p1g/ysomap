package ysomap.core.payload.java.jdk;

import org.junit.Test;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;

/**
 * @author wh1t3P1g
 * @since 2020/10/27
 */
public class URLDNSTest {

    @Test
    public void pack() throws Exception {
        Payload payload = new URLDNS();
        Bullet bullet = payload.getDefaultBullet("http://xxxxxx.ceye.io");
        payload.setBullet(bullet);
//        Serializer serializer = payload.getSerializer();
//        serializer.deserialize(serializer.serialize(payload.getObject()));
    }
}