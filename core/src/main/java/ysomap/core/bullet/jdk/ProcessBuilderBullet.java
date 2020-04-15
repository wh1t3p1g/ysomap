package ysomap.core.bullet.jdk;

import ysomap.common.annotation.Bullets;
import ysomap.common.annotation.NotNull;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;

/**
 * @author wh1t3P1g
 * @since 2020/4/15
 */
@Bullets
public class ProcessBuilderBullet extends Bullet<ProcessBuilder> {

    @NotNull
    @Require(name = "command", detail = "system commands")
    public String command;

    @Override
    public ProcessBuilder getObject() throws Exception {
        return new ProcessBuilder(command);
    }
}
