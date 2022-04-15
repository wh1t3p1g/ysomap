package ysomap.core.serializer;

/**
 * @author wh1t3p1g
 * @since 2022/4/13
 */
public enum SerializerTypeCodes {
    JAVA("default"),
    FASTJSON("fastjson"),
    JACKSON("jackson"),
    XSTREAM("xstream"),
    XMLDECODER("xmldecoder"),
    HESSIAN("hessian"),
    HESSIAN2("hessian2"),
    KRYO("kryo"),
    KRYO_ALT_STRATEGY("kryo_alt_strategy"),
    EMPTY("empty")
    ;

    private String name;
    SerializerTypeCodes(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public static String[] getAllSerializerType(){
        return new String[]{
                "default", "empty", "fastjson", "jackson", "xstream",
                "xmldecoder", "hessian", "kryo", "kryo_alt_strategy"
        };
    }
}
