package ysomap.core;

import ysomap.bullets.Bullet;
import ysomap.common.exception.GenerateErrorException;
import ysomap.exploits.Exploit;
import ysomap.payloads.Payload;

/**
 * @author wh1t3P1g
 * @since 2021/6/13
 */
public class Generator {

    public Exploit exploit;
    public Payload payload;
    public Bullet bullet;

    public static Object generate(String type, Class<?> clazz) throws GenerateErrorException {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new GenerateErrorException(type, clazz.getSimpleName());
        }
    }

    public static Object setValue(Object obj, String key, Object value) throws Exception {
        if(obj instanceof Exploit){
            Exploit exploit = (Exploit) obj;
            exploit.set(key, value);
        }else if(obj instanceof Payload){
            Payload payload = (Payload) obj;
            payload.set(key, value);
        }
        throw new GenerateErrorException("SET key "+key);
    }
}
