package ysomap.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

/**
 * @author wh1t3P1g
 * @since 2021/6/15
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Targets {

    String CODE = "Evil Code Generator";
    String RMI = "RMI";
    String LDAP = "LDAP";
    String JNDI = "JNDI";
    String XSTREAM = "XStream";
    String FASTJSON = "Fastjson";
    String JDK = "Serializable";
    String JACKSON = "Jackson";
    String XMLDECODER = "XMLDecoder";
    String HESSIAN = "Hessian";
    String KRYO = "kryo";

    String[] value() default {};

    class Utils {
        public static String[] getTarget(AnnotatedElement annotated) {
            Targets targets = annotated.getAnnotation(Targets.class);
            if (targets != null) {
                return targets.value();
            } else {
                return new String[0];
            }
        }
    }
}
