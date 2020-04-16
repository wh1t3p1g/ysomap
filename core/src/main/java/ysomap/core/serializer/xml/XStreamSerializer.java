package ysomap.core.serializer.xml;

import com.thoughtworks.xstream.XStream;
import ysomap.core.serializer.Serializer;

import java.io.OutputStream;

/**
 * @author wh1t3P1g
 * @since 2020/4/15
 */
public class XStreamSerializer implements Serializer<String> {

    public static Serializer serializer = new XStreamSerializer();
    public static String OUTPUT = "console";
    public static XStream xStream = new XStream();

    static {
//        xStream.addPermission(NoTypePermission.NONE);
//        xStream.addPermission(NullPermission.NULL);
//        xStream.allowTypeHierarchy(Collections.class);
//        xStream.allowTypesByWildcard(new String[]{"*.*"});
    }

    @Override
    public String serialize(Object obj) throws Exception {
        return xStream.toXML(obj);
    }

    @Override
    public void serialize(Object obj, OutputStream out) throws Exception {
        String result = serialize(obj);
        out.write("\n".getBytes());
        out.write(result.getBytes());
        out.write("\n".getBytes());
    }

    @Override
    public Object deserialize(String obj) throws Exception {
        return xStream.fromXML(obj);
    }

    @Override
    public String getOutputType() {
        return OUTPUT;
    }
}
