package ysomap.bullets.collections;

import ysomap.bullets.AbstractBullet;
import ysomap.core.util.ReflectionHelper;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

/**
 * @author wh1t3P1g
 * @since 2020/3/2
 */
@SuppressWarnings({"rawtypes"})
public abstract class AbstractTransformerBullet extends AbstractBullet<Object> {

    public Class transformerClazz;
    public Class constantTransformerClazz;
    public Class invokerTransformerClazz;
    public Class instantiateTransformer;


    public void initClazz(String version) throws ClassNotFoundException {
        try{
            if(version.equals("3")){
                transformerClazz = Class.forName("org.apache.commons.collections.Transformer");
                constantTransformerClazz = Class.forName("org.apache.commons.collections.functors.ConstantTransformer");
                invokerTransformerClazz = Class.forName("org.apache.commons.collections.functors.InvokerTransformer");
                instantiateTransformer = Class.forName("org.apache.commons.collections.functors.InstantiateTransformer");
            }else{
                transformerClazz = Class.forName("org.apache.commons.collections4.Transformer");
                constantTransformerClazz = Class.forName("org.apache.commons.collections4.functors.ConstantTransformer");
                invokerTransformerClazz = Class.forName("org.apache.commons.collections4.functors.InvokerTransformer");
                instantiateTransformer = Class.forName("org.apache.commons.collections4.functors.InstantiateTransformer");
            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public Object createTransformerArray(LinkedList<Object> transformers) throws ClassNotFoundException {
        Object transformerArray =
                Array.newInstance(transformerClazz, transformers.size());
        for(int i=0; i< transformers.size(); i++){
            Array.set(transformerArray, i, transformers.get(i));
        }
        return transformerArray;
    }

    public Object createConstantTransformer(Object args) throws Exception {
        return ReflectionHelper.newInstance(constantTransformerClazz.getName(), new Class[]{Object.class}, args);
    }

    public Object createInvokerTransformer(Object... args) throws Exception {
        return ReflectionHelper.newInstance(invokerTransformerClazz.getName(),
                new Class[]{String.class, Class[].class, Object[].class},
                args);
    }

    public Object createInstantiateTransformer(Object... args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return ReflectionHelper.newInstance(instantiateTransformer.getName(),
                new Class<?>[]{Class[].class,Object[].class},
                args
        );
    }
}
