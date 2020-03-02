package ysomap.gadget.bullet.collections;

import ysomap.annotation.Require;
import ysomap.gadget.bullet.Bullet;
import ysomap.util.Reflections;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

/**
 * @author wh1t3P1g
 * @since 2020/3/2
 */
@SuppressWarnings({"rawtypes"})
public abstract class AbstractTransformerBullet extends Bullet<Object> {

    @Require(name="version", type="int", detail = "commons-collections version, plz choose 3 or 4")
    public String version = "3";// 默认生成commonscollections 3.2.1

    public Class transformerClazz;
    public Class constantTransformerClazz;
    public Class invokerTransformerClazz;
    public Class instantiateTransformer;


    public void initClazz() throws ClassNotFoundException {
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
        return Reflections.newInstance(constantTransformerClazz.getName(), new Class[]{Object.class}, args);
    }

    public Object createInvokerTransformer(Object... args) throws Exception {
        return Reflections.newInstance(invokerTransformerClazz.getName(),
                new Class[]{String.class, Class[].class, Object[].class},
                args);
    }

    public Object createInstantiateTransformer(Object... args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return Reflections.newInstance(instantiateTransformer.getName(),
                new Class<?>[]{Class[].class,Object[].class},
                args
        );
    }
}
