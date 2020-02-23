package ysomap.util.enums;

import ysomap.exception.YsoClassNotFoundException;
import ysomap.gadget.ObjectGadget;

/**
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes"})
public enum BulletEnums implements ObjectEnums{
    TransformerBullet("1", ysomap.gadget.bullet.collections.TransformerBullet.class),
    TemplatesImplBullet("2", ysomap.gadget.bullet.jdk.TemplatesImplBullet.class),
    TransformerWithTemplatesImplBullet("3", ysomap.gadget.bullet.collections.TransformerWithTemplatesImplBullet.class),
    TransformerWithResponseBullet("4", ysomap.gadget.bullet.collections.TransformerWithResponseBullet.class)
    ;
    private String name;
    private Class clazz;

    BulletEnums(String name, Class<? extends ObjectGadget> clazz) {
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
            BulletEnums bullet = BulletEnums.valueOf(name);
            return bullet.getClazz();
        }catch (IllegalArgumentException e){
            throw new YsoClassNotFoundException("bullet", name);
        }
    }
}
