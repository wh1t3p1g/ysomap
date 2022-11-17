package ysomap.bullets.wildfly;

import org.jboss.as.connector.subsystems.datasources.WildFlyDataSource;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;

/**
 * @author wh1t3p1g
 * @since 2022/9/29
 */
@Bullets
@Authors({Authors.WH1T3P1G})
@Details("jndi")
@Targets({Targets.JDK})
@Dependencies({"org.wildfly:wildfly-connector"})
public class WildFlyDataSourceBullet extends AbstractBullet<Object> {

    @NotNull
    @Require(name = "jndi", detail = "jndi")
    public String jndi;

    @Override
    public Object getObject() throws Exception {
        return new WildFlyDataSource(null, jndi);
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new WildFlyDataSourceBullet();
        bullet.set("jndi", args[0]);
        return bullet;
    }
}
