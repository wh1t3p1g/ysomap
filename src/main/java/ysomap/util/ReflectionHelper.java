package ysomap.util;

import com.nqzero.permit.Permit;
import ysomap.annotation.NotNull;
import ysomap.exception.ArgumentsNotCompleteException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings ( "restriction" )
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
        }
        catch (NoSuchFieldException ex) {
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
				NotNull.Util.checkValueNotNull(obj, field.getName());
			}
		}
	}

}
