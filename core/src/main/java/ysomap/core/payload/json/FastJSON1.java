package ysomap.core.payload.json;

import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Payloads;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.bullet.json.JdbcRowSetImplJsonBullet;

/**
 * @author wh1t3P1g
 * @since 2020/3/18
 */
@SuppressWarnings({"rawtypes"})
@Payloads
@Authors({ Authors.WH1T3P1G })
@Require(bullets = {"JdbcRowSetImplJsonBullet","TemplatesImplJsonBullet"})
@Dependencies({"开启AutoType 或 低版本fastjson"})
public class FastJSON1 extends AbstractJson {

    @Override
    public Bullet getDefaultBullet(String command) throws Exception {
        return new JdbcRowSetImplJsonBullet();
    }

    @Override
    public Object pack(Object obj) throws Exception {
        return obj;
    }
}
