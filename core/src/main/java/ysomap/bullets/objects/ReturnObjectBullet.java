package ysomap.bullets.objects;

import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.NotNull;
import ysomap.common.annotation.Require;

public class ReturnObjectBullet extends AbstractBullet<Object> {

    @NotNull
    @Require(name = "object", detail = "")
    public Object obj;

    @Override
    public Object getObject() throws Exception {
        return obj;
    }

    public static Bullet<Object> newInstance(Object... args) throws Exception {
        ReturnObjectBullet bullet = new ReturnObjectBullet();
        bullet.set("obj", args[0]);
        return bullet;
    }
}
