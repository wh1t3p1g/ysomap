package ysomap.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

/**
 * @author wh1t3P1g
 * @since 2021/6/13
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Details {
    String value() default "";

    class Utils {
        public static String getDetail(AnnotatedElement annotated) {
            Details details = annotated.getAnnotation(Details.class);
            if (details != null) {
                return details.value();
            } else {
                return "";
            }
        }
    }
}
