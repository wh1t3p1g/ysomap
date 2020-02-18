package ysomap.gadget.enums;

import ysomap.gadget.payload.Payload;
import ysomap.gadget.payload.collections.*;

/**
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes"})
public enum PayloadEnums implements ObjectEnums{
    PAYLOAD1("1", CommonsCollections1.class),
    PAYLOAD2("2", CommonsCollections2.class),
    PAYLOAD3("3", CommonsCollections3.class),
    PAYLOAD4("4", CommonsCollections4.class),
    PAYLOAD5("5", CommonsCollections5.class),
    PAYLOAD6("6", CommonsCollections6.class),
    PAYLOAD7("7", CommonsCollections7.class),
    PAYLOAD8("8", CommonsCollections8.class),
    PAYLOAD9("9", CommonsCollections9.class),
    ;
    private String name;
    private Class clazz;

    PayloadEnums(String name, Class<? extends Payload> clazz){
        this.name = name;
        this.clazz = clazz;
    }

    @Override
    public Class getClazz() {
        return clazz;
    }

    @Override
    public String getName() {
        return name;
    }

    public static Class getClazzByName(String name){
        try{
            PayloadEnums payload = PayloadEnums.valueOf("PAYLOAD"+name);
            return payload.getClazz();
        }catch (IllegalArgumentException e){
            System.out.println("* payload"+name+" not found, exit.");
            System.exit(1);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(PayloadEnums.getClazzByName("10"));
    }
}
