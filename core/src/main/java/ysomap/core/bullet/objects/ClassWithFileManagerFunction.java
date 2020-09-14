package ysomap.core.bullet.objects;

import ysomap.common.annotation.Bullets;
import ysomap.common.annotation.NotNull;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;

/**
 * @author wh1t3P1g
 * @since 2020/9/5
 */
@Bullets
public class ClassWithFileManagerFunction extends Bullet<byte[]> {

    @NotNull
    @Require(name = "classname", detail = "所需生成的类名")
    public String classname;

    @NotNull
    @Require(name = "type", detail = "所需生成的文件类型，支持class或jar")
    public String type = null;

    @Override
    public byte[] getObject() throws Exception {
        return new byte[0];
    }
}
