package ysomap.bullets;

/**
 * @author wh1t3P1g
 * @since 2021/6/13
 */
public interface Bullet<T> {

    T getObject() throws Exception;

//    default Bullet<T> set(String key, Object value) throws Exception {
//        ReflectionHelper.setFieldValue(this, key, value);
//        return this;
//    }
//
//    default boolean has(String key) {
//        return ReflectionHelper.getField(this.getClass(), key) != null;
//    }
//
//    default String get(String key) {
//        try {
//            Object obj = ReflectionHelper.getFieldValue(this, key);
//            if(obj != null){
//                return obj.toString();
//            }
//            return null;
//        } catch (Exception e) {
//            Logger.error("Key "+key+" not found");
//            return null;
//        }
//    }
}
