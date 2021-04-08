package ysomap.core.payload.xmldecoder;

import org.junit.Test;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;

import static org.junit.Assert.*;

/**
 * @author wh1t3P1g
 * @since 2021/4/8
 */
public class WeblogicXML1Test {

    @Test
    public void getObject() throws Exception {
        Payload payload = new WeblogicXML2();
        Bullet bullet = payload.getDefaultBullet("open -a Calculator");
        payload.setBullet(bullet);
//        System.out.println(payload.getObject());
    }
}