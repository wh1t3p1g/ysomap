package ysomap.core.util;

import com.nqzero.permit.Permit;
import sun.reflect.ReflectionFactory;
import ysomap.common.annotation.NotNull;
import ysomap.common.exception.ArgumentsNotCompleteException;

import java.lang.reflect.*;
import java.util.HashMap;

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
		field.set(obj, value);
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
    	Constructor<?> ctor = Class.forName(classname).getConstructor(paramTypes);
		setAccessible(ctor);
		return ctor;
	}

	public static Object newInstance(String className, Object ... args) throws Exception {
        return getFirstCtor(className).newInstance(args);
    }

    public static Object newInstance(String classname, Class<?>[] paramTypes, Object... args) throws NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
		return getConstructor(classname, paramTypes).newInstance(args);
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
		Constructor<? super T> objCons = constructorClass.getDeclaredConstructor(consArgTypes);
		objCons.setAccessible(true);
		Constructor<?> sc = ReflectionFactory.getReflectionFactory().newConstructorForSerialization(classToInstantiate, objCons);
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

	public static HashMap makeMap (Object v1, Object v2 ) throws Exception, ClassNotFoundException, NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		HashMap s = new HashMap();
		ReflectionHelper.setFieldValue(s, "size", 2);
		Class nodeC;
		try {
			nodeC = Class.forName("java.util.HashMap$Node");
		}
		catch ( ClassNotFoundException e ) {
			nodeC = Class.forName("java.util.HashMap$Entry");
		}
		Constructor nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
		ReflectionHelper.setAccessible(nodeCons);

		Object tbl = Array.newInstance(nodeC, 2);
		Array.set(tbl, 0, nodeCons.newInstance(0, v1, v1, null));
		Array.set(tbl, 1, nodeCons.newInstance(0, v2, v2, null));
		ReflectionHelper.setFieldValue(s, "table", tbl);
		return s;
	}

}
