package ysomap.runner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import ysomap.exception.GenerateErrorException;
import ysomap.exception.YsoClassNotFoundException;
import ysomap.gadget.ObjectGadget;
import ysomap.gadget.payload.Payload;
import ysomap.gadget.payload.Releasable;

/**
 * @author wh1t3P1g
 * @since 2020/2/17
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class PayloadTester extends PayloadRunner{

    public PayloadTester(CommandLine options) throws ParseException, YsoClassNotFoundException, GenerateErrorException {
        super(options);
    }

    public PayloadTester(Class<? extends Payload> payload) throws GenerateErrorException {
//        this.payload = PayloadHelper.makePayload(payload);
        defaultBullet();
    }

    public PayloadTester(Class<? extends Payload> payload, ObjectGadget bullet) throws GenerateErrorException {
//        this.payload = PayloadHelper.makePayload(payload);
        this.bullet = bullet;
    }

    public PayloadTester(Class<? extends Payload> payload, Class<? extends ObjectGadget> bullet, String[] args) throws GenerateErrorException {
//        this.payload = PayloadHelper.makePayload(payload);
//        this.bullet = PayloadHelper.makeBullet(bullet, args);
    }

    @Override
    public void run(){
        try {
            serializer = payload.getSerializer();
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


}
