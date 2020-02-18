package ysomap.runner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import ysomap.gadget.ObjectGadget;
import ysomap.gadget.bullet.collections.TransformerBullet;
import ysomap.gadget.enums.BulletEnums;
import ysomap.gadget.enums.PayloadEnums;
import ysomap.gadget.payload.Payload;
import ysomap.gadget.payload.Releasable;
import ysomap.serializer.Serializer;
import ysomap.util.PayloadHelper;

import java.io.*;

/**
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class PayloadRunner implements ObjectRunner{

    Payload payload;
    Serializer serializer;
    ObjectGadget bullet;
    OutputStream out;

    public PayloadRunner(){}

    public PayloadRunner(CommandLine options) throws ParseException {
        if(options.hasOption("payload")){
            this.payload = PayloadHelper.makePayload(
                    PayloadEnums.getClazzByName(options.getOptionValue("payload")));
        }else{
            throw new ParseException("payload not set");
        }

        if(options.hasOption("bullet") && options.hasOption("args")){
            this.bullet = PayloadHelper.makeBullet(
                    BulletEnums.getClazzByName(options.getOptionValue("bullet")),
                    options.getOptionValues("args")
            );
        }else if(options.hasOption("args")){
            System.err.println("* using pre-design bullet");
            this.bullet = payload.getDefaultBullet(options.getOptionValue("args"));
        } else{
            System.err.println("* setting bullet error, using default bullet<TransformerBullet>");
            defaultBullet();
        }

        if(options.hasOption("output")){
            File file = null;
            try {
                file = new File(options.getOptionValue("output"));
                out = new FileOutputStream(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            out = System.out;
        }
    }

    @Override
    public void run(){
        try {
            serializer = payload.getSerializer();
            payload.setBullet(bullet);
            Object obj = payload.getObject();
            serializer.serialize(obj, out);
            if(payload instanceof Releasable){
                ((Releasable) payload).release(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(out instanceof FileOutputStream){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void defaultBullet(){
        bullet = new TransformerBullet(PayloadHelper.defaultTestCommand(),"3");
    }

    public ObjectRunner setSerializer(Serializer serializer) {
        this.serializer = serializer;
        return this;
    }

    public ObjectRunner setBullet(ObjectGadget bullet) {
        this.bullet = bullet;
        return this;
    }
}
