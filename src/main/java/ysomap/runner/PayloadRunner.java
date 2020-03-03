package ysomap.runner;

import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.serializer.Serializer;

/**
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class PayloadRunner{

    Payload payload;
    Serializer serializer;
    Bullet bullet;

    public void test() throws Exception {
        System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase","true");
        System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase","true");
        if(payload != null && bullet != null){
            serializer = payload.getSerializer();
            payload.setBullet(bullet);
            Object obj = payload.getObject();
            serializer.deserialize(serializer.serialize(obj));
        }
    }


    public PayloadRunner setSerializer(Serializer serializer) {
        this.serializer = serializer;
        return this;
    }

    public Payload getPayload() {
        return payload;
    }

    public PayloadRunner setPayload(Payload payload) {
        this.payload = payload;
        return this;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public Bullet getBullet() {
        return bullet;
    }

    public PayloadRunner setBullet(Bullet bullet) {
        this.bullet = bullet;
        return this;
    }
}
