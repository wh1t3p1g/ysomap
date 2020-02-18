package ysomap.gadget.enums;

import ysomap.gadget.ObjectGadget;
import ysomap.gadget.bullet.TemplatesImplBullet;
import ysomap.gadget.bullet.collections.TransformerBullet;
import ysomap.gadget.bullet.collections.TransformerWithResponseBullet;
import ysomap.gadget.bullet.collections.TransformerWithTemplatesImplBullet;

/**
 * @author wh1t3P1g
 * @since 2020/2/18
 */
@SuppressWarnings({"rawtypes"})
public enum BulletEnums implements ObjectEnums{
    BULLET1("1", TransformerBullet.class),
    BULLET2("2", TemplatesImplBullet.class),
    BULLET3("3", TransformerWithTemplatesImplBullet.class),
    BULLET4("4", TransformerWithResponseBullet.class)
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

    public static Class getClazzByName(String name){
        try{
            BulletEnums bullet = BulletEnums.valueOf("BULLET"+name);
            return bullet.getClazz();
        }catch (IllegalArgumentException e){
            System.out.println("* bullet"+name+" not found, exit.");
            System.exit(1);
        }
        return null;
    }
}
