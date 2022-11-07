package ysomap.payloads.java.jdk;

import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.DetectClassBullet;
import ysomap.common.annotation.*;
import ysomap.payloads.AbstractPayload;

/**
 * DetectClass 通过 dnslog 探测存在哪些类
 * 不依赖jdk版本
 *
 * @author Ar3h
 * @since 2022/11/06
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Payloads
@Targets({Targets.JDK})
@Dependencies({"*"})
@Require(bullets = {"DetectClassBullet"}, param = false)
public class DetectClass extends AbstractPayload<Object> {
    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return DetectClassBullet.newInstance(args);
    }
    
    @Override
    public Object pack(Object obj) throws Exception {
        return obj;
    }
}
