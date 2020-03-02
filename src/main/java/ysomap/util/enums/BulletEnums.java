package ysomap.util.enums;

import ysomap.exception.YsoClassNotFoundException;
import ysomap.gadget.ObjectGadget;

/**
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes"})
public enum BulletEnums implements ObjectEnums{
    TransformerBullet(ysomap.gadget.bullet.collections.TransformerBullet.class),
    TemplatesImplBullet(ysomap.gadget.bullet.jdk.TemplatesImplBullet.class),
    TransformerWithTemplatesImplBullet(ysomap.gadget.bullet.collections.TransformerWithTemplatesImplBullet.class),
    TransformerWithResponseBullet(ysomap.gadget.bullet.collections.TransformerWithResponseBullet.class),
    TransformerWithJNDIBullet(ysomap.gadget.bullet.collections.TransformerWithJNDIBullet.class),
    RMIConnectBullet(ysomap.gadget.bullet.jdk.rmi.RMIConnectBullet.class),
    RefBullet(ysomap.gadget.bullet.jdk.rmi.RefBullet.class),
    TomcatRefBullet(ysomap.gadget.bullet.jdk.rmi.TomcatRefBullet.class),
    JdbcRowSetImplBullet(ysomap.gadget.bullet.jdk.JdbcRowSetImplBullet.class),
    ;


    private Class clazz;

    BulletEnums(Class<? extends ObjectGadget> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Class getClazz() {
        return clazz;
    }

    public static Class getClazzByName(String name) throws YsoClassNotFoundException {
        try{
            BulletEnums bullet = BulletEnums.valueOf(name);
            return bullet.getClazz();
        }catch (IllegalArgumentException e){
            throw new YsoClassNotFoundException("bullet", name);
        }
    }
}
