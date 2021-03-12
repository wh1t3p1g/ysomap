package ysomap.core.payload.xstream;

import org.junit.Test;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.serializer.Serializer;

/**
 * @author wh1t3P1g
 * @since 2021/2/19
 */
public class XercesValueTest {

    @Test
    public void getObject() throws Exception {
        Payload payload = new XercesValue();
        Bullet bullet = payload.getDefaultBullet("rmi://localhost:1099/test");
        payload.setBullet(bullet);
        Serializer serializer = payload.getSerializer();
//        System.out.println(serializer.serialize(payload.getObject()));
    }
}