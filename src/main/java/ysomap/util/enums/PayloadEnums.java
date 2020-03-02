package ysomap.util.enums;

import ysomap.exception.YsoClassNotFoundException;
import ysomap.gadget.payload.Payload;

/**
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes"})
public enum PayloadEnums implements ObjectEnums{
    CommonsCollections1(ysomap.gadget.payload.collections.CommonsCollections1.class),
    CommonsCollections2(ysomap.gadget.payload.collections.CommonsCollections2.class),
    CommonsCollections3(ysomap.gadget.payload.collections.CommonsCollections3.class),
    CommonsCollections4(ysomap.gadget.payload.collections.CommonsCollections4.class),
    CommonsCollections5(ysomap.gadget.payload.collections.CommonsCollections5.class),
    CommonsCollections6(ysomap.gadget.payload.collections.CommonsCollections6.class),
    CommonsCollections7(ysomap.gadget.payload.collections.CommonsCollections7.class),
    CommonsCollections8(ysomap.gadget.payload.collections.CommonsCollections8.class),
    CommonsCollections9(ysomap.gadget.payload.collections.CommonsCollections9.class),
    URLDNS(ysomap.gadget.payload.jdk.URLDNS.class),
    RMIConnectWrapped(ysomap.gadget.payload.rmi.RMIConnectWrapped.class),
    RMIConnectWrappedWithProxy(ysomap.gadget.payload.rmi.RMIConnectWrappedWithProxy.class),
    RefWrapper(ysomap.gadget.payload.rmi.RefWrapper.class),
    ;

    private Class clazz;

    PayloadEnums(Class<? extends Payload> clazz){
        this.clazz = clazz;
    }

    @Override
    public Class getClazz() {
        return clazz;
    }

    public static Class getClazzByName(String name) throws YsoClassNotFoundException {
        try{
            PayloadEnums payload = PayloadEnums.valueOf(name);
            return payload.getClazz();
        }catch (IllegalArgumentException e){
            throw new YsoClassNotFoundException("payload", name);
        }
    }

}
