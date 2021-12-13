package ysomap.core;

import sun.misc.ObjectInputFilter;
import ysomap.core.util.ReflectionHelper;

import java.util.Arrays;

/**
 * @author wh1t3p1g
 * @since 2021/11/23
 */
public class ObjectInputFilterManager {

    private static ObjectInputFilter filter = new ObjectWhitelistFilter();

    public static void setup(){
        try{
            ObjectInputFilter.Config.setSerialFilter(filter);
        } catch (IllegalStateException e){
            try {
                ReflectionHelper.setStaticFieldValue(ObjectInputFilter.Config.class, "serialFilter", filter);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void suspend(){
        try {
            ReflectionHelper.setStaticFieldValue(ObjectInputFilter.Config.class, "serialFilter", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ObjectWhitelistFilter implements ObjectInputFilter{

        private static final String[] whitelist = new String[]{
                "java.",
                "com.sun.",
                "com.oracle.",
                "javax."
        };

        private static final String[] blacklist = new String[]{
                "com.sun.jndi.ldap.LdapAttribute",
                "com.sun.jndi.rmi.registry.BindingEnumeration",
                "com.sun.jndi.toolkit.dir.LazySearchEnumerationImpl",
                "com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl",
                "com.sun.rowset.JdbcRowSetImpl",
                "com.sun.syndication.feed.impl.ObjectBean",
                "com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data",
                "java.net.URL",
                "java.rmi.registry.Registry",
                "java.rmi.server.ObjID",
                "java.rmi.server.RemoteObjectInvocationHandler",
                "java.rmi.server.UnicastRemoteObject",
                "java.util.ServiceLoader$LazyIterator",
                "javax.imageio.ImageIO$ContainsFilter",
                "javax.management.BadAttributeValueExpException",
                "javax.management.MBeanServerInvocationHandler",
                "javax.management.openmbean.CompositeDataInvocationHandler",
                "javax.naming.spi.ObjectFactory",
        };

        @Override
        public Status checkInput(FilterInfo filterInfo) {
            Class<?> clazz = getSerialClass(filterInfo);
            if(clazz != null){
                if(clazz.isPrimitive() ||
                        (Arrays.stream(whitelist).anyMatch(pre -> clazz.getName().startsWith(pre))
                                && Arrays.stream(blacklist).noneMatch(cl -> cl.equals(clazz.getName())))){
                    return Status.ALLOWED;
                }
                return Status.REJECTED;
            }
            return Status.UNDECIDED;
        }

        public Class<?> getSerialClass(FilterInfo filterInfo){
            Class<?> clazz = filterInfo.serialClass();
            if(clazz.isArray()){
                do {
                    clazz = clazz.getComponentType();
                } while (clazz.isArray());
            }
            return clazz;
        }
    }
}
