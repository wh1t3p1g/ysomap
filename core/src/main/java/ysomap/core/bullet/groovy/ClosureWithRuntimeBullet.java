package ysomap.core.bullet.groovy;

import org.codehaus.groovy.runtime.MethodClosure;
import ysomap.common.annotation.Bullets;
import ysomap.common.annotation.NotNull;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;

/**
 * @author wh1t3P1g
 * @since 2020/4/18
 */
@Bullets
public class ClosureWithRuntimeBullet extends Bullet<Object> {

    @NotNull
    @Require(name = "command", detail = "所需执行的命令")
    public String command = null;

    @Override
    public Object getObject() throws Exception {
        Runtime runtime = Runtime.getRuntime();
        return new MethodClosure(runtime, "exec");
    }
}
