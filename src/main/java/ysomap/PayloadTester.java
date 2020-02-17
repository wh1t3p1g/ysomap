package ysomap;

import ysomap.gadget.ObjectGadget;
import ysomap.gadget.bullet.collections.TransformerBullet;
import ysomap.gadget.payload.Payload;
import ysomap.gadget.payload.Releasable;
import ysomap.serializer.Serializer;
import ysomap.serializer.SerializerFactory;
import ysomap.util.PayloadHelper;

/**
 * @author wh1t3P1g
 * @since 2020/2/17
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class PayloadTester {

    private Payload<?> payload;
    private Serializer serializer;
    private ObjectGadget bullet;

    public PayloadTester(Class<? extends Payload> payload) {
        makePayload(payload);
        defaultSerializer();
        defaultBullet();
    }

    public PayloadTester(Class<? extends Payload> payload, Serializer serializer) {
        makePayload(payload);
        this.serializer = serializer;
        defaultBullet();
    }

    public PayloadTester(Class<? extends Payload> payload, Serializer serializer, ObjectGadget bullet) {
        makePayload(payload);
        this.serializer = serializer;
        this.bullet = bullet;
    }

    public void run(){
        try {
            payload.setBullet(bullet);
            Object obj = payload.getObject();
            serializer.deserialize(serializer.serialize(obj));
            if(payload instanceof Releasable){
                ((Releasable) payload).release(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void defaultBullet(){
        bullet = new TransformerBullet(PayloadHelper.defaultTestCommand(),3);
    }

    public void defaultSerializer(){
        serializer = SerializerFactory.createSerializer("default");
    }

    public void makePayload(Class<? extends Payload> clazz){
        try {
            payload = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public PayloadTester setSerializer(Serializer serializer) {
        this.serializer = serializer;
        return this;
    }

    public PayloadTester setBullet(ObjectGadget bullet) {
        this.bullet = bullet;
        return this;
    }
}
