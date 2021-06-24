package ysomap.payloads.java.collections;

import junit.framework.TestCase;
import ysomap.bullets.Bullet;
import ysomap.core.serializer.Serializer;
import ysomap.payloads.Payload;

/**
 * @author wh1t3P1g
 * @since 2021/6/16
 */
public class CommonsBeanutils1Test extends TestCase {

    public void testPack() throws Exception {
        Payload payload = new CommonsBeanutils1();
        Bullet bullet = payload.getDefaultBullet("socket", "127.0.0.1:1099", "SocketEcho", "false");
        payload.setBullet(bullet);
        Serializer serializer = payload.getSerializer();
//        serializer.deserialize(serializer.serialize(payload.getObject()));
    }
}