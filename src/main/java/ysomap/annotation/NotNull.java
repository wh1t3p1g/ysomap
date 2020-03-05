package ysomap.annotation;

import ysomap.exception.ArgumentsNotCompleteException;
import ysomap.util.ReflectionHelper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wh1t3P1g
 * @since 2020/3/5
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {

    class Util {
        public static void checkValueNotNull(Object obj, String field) throws ArgumentsNotCompleteException {
            Object fvalue = null;
            try {
                fvalue = ReflectionHelper.getFieldValue(obj, field);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(fvalue == null){
                throw new ArgumentsNotCompleteException(field);
            }
        }
    }
}
