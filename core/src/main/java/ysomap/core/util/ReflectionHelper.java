package ysomap.core.util;

import com.nqzero.permit.Permit;
import sun.reflect.ReflectionFactory;
import ysomap.common.annotation.NotNull;
import ysomap.common.exception.ArgumentsNotCompleteException;
import ysomap.common.util.Logger;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings ( {"restriction","rawtypes", "unchecked"} )
public class ReflectionHelper {

    public static void setAccessible(AccessibleObject member) {
        // quiet runtime warnings from JDK9+
        Permit.setAccessible(member);
    }

	public static Field getField(final Class<?> clazz, final String fieldName) {
        Field field = null;
		try {
			field = clazz.getDeclaredField(fieldName);
			setAccessible(field);
        } catch (NoSuchFieldException ex) {
            if (clazz.getSuperclass() != null)
                field = getField(clazz.getSuperclass(), fieldName);
        }
		return field;
	}

	public static void setFieldValue(final Object obj, final String fieldName, final Object value) throws Exception {
		final Field field = getField(obj.getClass(), fieldName);
		if(field != null) {
			field.set(obj, value);
		}
	}

	public static void setStaticFieldValue(final Class<?> clazz, final String fieldName, final Object value) throws Exception {
		final Field field = getField(clazz, fieldName);
		if(field != null){
			field.set(clazz, value);
		}
	}

	public static Object getFieldValue(final Object obj, final String fieldName) throws Exception {
		final Field field = getField(obj.getClass(), fieldName);
		return field.get(obj);
	}

	public static Constructor<?> getFirstCtor(final String name) throws Exception {
		final Constructor<?> ctor = Class.forName(name).getDeclaredConstructors()[0];
	    setAccessible(ctor);
	    return ctor;
	}

	public static Constructor<?> getConstructor(String classname, Class<?>[] paramTypes) throws ClassNotFoundException, NoSuchMethodException {
    	Constructor<?> ctor = Class.forName(classname).getDeclaredConstructor(paramTypes);
		setAccessible(ctor);
		return ctor;
	}

	public static Object newInstance(String className, Object ... args) throws Exception {
        return getFirstCtor(className).newInstance(args);
    }

    public static Object newInstance(String classname, Class<?>[] paramTypes, Object... args) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
		return getConstructor(classname, paramTypes).newInstance(args);
	}

	public static <T> T newInstance(Class<T> cls, Class<?>[] paramTypes, Object... args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		Constructor<?> ctor = cls.getDeclaredConstructor(paramTypes);
		setAccessible(ctor);
		return (T) ctor.newInstance(args);
	}

	public static void checkClassFieldsNotNull(Object obj) throws ArgumentsNotCompleteException {
    	if(obj == null){
    		throw new ArgumentsNotCompleteException("null");
		}
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			if(field.isAnnotationPresent(NotNull.class)){
				checkValueNotNull(obj, field.getName());
			}
		}
	}

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

	@SuppressWarnings ( {
			"unchecked"
	} )
	public static <T> T createWithConstructor ( Class<T> classToInstantiate, Class<? super T> constructorClass, Class<?>[] consArgTypes,
												Object[] consArgs ) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		// 可以根据提供的Class的构造器来为classToInstantiate新建对象
    	Constructor<? super T> objCons = constructorClass.getDeclaredConstructor(consArgTypes);
		objCons.setAccessible(true);
		// 实现不调用原有构造器去实例化一个对象，相当于动态增加了一个构造器
		Constructor<?> sc = ReflectionFactory.getReflectionFactory()
							.newConstructorForSerialization(classToInstantiate, objCons);
		sc.setAccessible(true);
		return (T) sc.newInstance(consArgs);
	}

	public static Object createWithoutConstructor(String classname) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
    	return createWithoutConstructor(Class.forName(classname));
	}

	public static <T> T createWithoutConstructor ( Class<T> classToInstantiate )
			throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    	return createWithConstructor(classToInstantiate, Object.class, new Class[0], new Object[0]);
	}


	public static <T> T set(T thisObj, String key, Object value) throws Exception {
		ReflectionHelper.setFieldValue(thisObj, key, value);
		return thisObj;
	}

	public static String get(Object thisObj, String key) {
		try {
			Object obj = ReflectionHelper.getFieldValue(thisObj, key);
			if(obj != null){
				return obj.toString();
			}
			return null;
		} catch (Exception e) {
			Logger.error("Key "+key+" not found");
			return null;
		}
	}

	public static boolean has(Object thisObj, String key) {
		return ReflectionHelper.getField(thisObj.getClass(), key) != null;
	}
}
