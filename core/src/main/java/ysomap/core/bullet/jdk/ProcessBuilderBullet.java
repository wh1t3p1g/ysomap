package ysomap.core.bullet.jdk;

import ysomap.common.annotation.Bullets;
import ysomap.common.annotation.NotNull;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;

import java.util.Arrays;
import java.util.List;

/**
 * @author wh1t3P1g
 * @since 2020/4/15
 */
@Bullets
public class ProcessBuilderBullet extends Bullet<ProcessBuilder> {

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
