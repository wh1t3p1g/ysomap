package ysomap.util.enums;

import ysomap.exception.YsoClassNotFoundException;
import ysomap.gadget.payload.Payload;

/**
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes"})
public enum PayloadEnums implements ObjectEnums{
    CommonsCollections1("1", ysomap.gadget.payload.collections.CommonsCollections1.class),
    CommonsCollections2("2", ysomap.gadget.payload.collections.CommonsCollections2.class),
    CommonsCollections3("3", ysomap.gadget.payload.collections.CommonsCollections3.class),
    CommonsCollections4("4", ysomap.gadget.payload.collections.CommonsCollections4.class),
    CommonsCollections5("5", ysomap.gadget.payload.collections.CommonsCollections5.class),
    CommonsCollections6("6", ysomap.gadget.payload.collections.CommonsCollections6.class),
    CommonsCollections7("7", ysomap.gadget.payload.collections.CommonsCollections7.class),
    CommonsCollections8("8", ysomap.gadget.payload.collections.CommonsCollections8.class),
    CommonsCollections9("9", ysomap.gadget.payload.collections.CommonsCollections9.class),
    URLDNS("10", ysomap.gadget.payload.jdk.URLDNS.class),
    RMIConnectWrapped("11", ysomap.gadget.payload.rmi.RMIConnectWrapped.class),
    RMIConnectWrappedWithProxy("12", ysomap.gadget.payload.rmi.RMIConnectWrappedWithProxy.class),
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

    public static Class getClazzByName(String name) throws YsoClassNotFoundException {
        try{
            PayloadEnums payload = PayloadEnums.valueOf(name);
            return payload.getClazz();
        }catch (IllegalArgumentException e){
            throw new YsoClassNotFoundException("payload", name);
        }
    }

    public static void main(String[] args) throws YsoClassNotFoundException {
        System.out.println(PayloadEnums.getClazzByName("10"));
    }
}
