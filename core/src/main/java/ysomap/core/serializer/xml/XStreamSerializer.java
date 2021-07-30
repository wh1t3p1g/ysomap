package ysomap.core.serializer.xml;

import com.thoughtworks.xstream.XStream;
import ysomap.core.serializer.BaseSerializer;
import ysomap.core.serializer.Serializer;

/**
 * @author wh1t3P1g
 * @since 2020/4/15
 */
public class XStreamSerializer extends BaseSerializer<String> {

    public static Serializer serializer = new XStreamSerializer();
    public String OUTPUT = "console";
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
    public Object deserialize(String obj) throws Exception {
        return xStream.fromXML(obj);
    }

    @Override
    public String getOutputType() {
        return OUTPUT;
    }

    @Override
    public void setOutputType(String output) {
        OUTPUT = output;
    }

}
