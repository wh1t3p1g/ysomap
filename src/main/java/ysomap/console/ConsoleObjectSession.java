package ysomap.console;

import org.apache.commons.lang3.RandomStringUtils;
import ysomap.core.ObjectGadget;
import ysomap.core.bean.Exploit;
import ysomap.core.bean.Payload;
import ysomap.util.Logger;
import ysomap.util.PayloadHelper;
import ysomap.util.ReflectionHelper;

import java.io.FileOutputStream;
import java.util.*;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ConsoleObjectSession implements Session<Class<?>> {

    private String sessionID;
    private String type;
    private Class<? extends ObjectGadget> clazz;
    private ObjectGadget obj;
    private HashMap<String, String> args = new LinkedHashMap<>();
    private Object retObj;

    public ConsoleObjectSession(String type) {
        this.type = type;
        sessionID = RandomStringUtils.randomAlphabetic(16);
    }

    @Override
    public void accept(Class<?> clazz) throws Exception {
        if(clazz != null){
            this.clazz = (Class<? extends ObjectGadget>) clazz;
            obj = PayloadHelper.makeGadget(this.clazz, type);
        }
    }

    @Override
    public void set(String key, Object value) throws Exception {
        if(value instanceof String){
            args.remove(key);// clear current key:value
            args.put(key, (String) value);
        }

        if(obj != null){
            obj.set(key, value);
        }
    }

    @Override
    public boolean has(String key) {
        return ReflectionHelper.getField(clazz, key) != null;
    }

    /**
     * only run exploit or payload
     * @throws Exception
     */
    @Override
    public void run() throws Exception {
        if(type.equals("exploit")){// multi thread running
            Exploit exploit = (Exploit)obj;
            exploit.setExit(false);
            new Thread(exploit).start();
        }else if(type.equals("payload")){
            Payload payload = (Payload) obj;
            retObj = payload.getObject();
            FileOutputStream fos = new FileOutputStream("obj.ser");
            payload.getSerializer().serialize(retObj, fos);
            Logger.success("* generate " + payload.getClass().getSimpleName() + " success, plz see obj.ser");
        }else{
            Logger.error("[-] current type<"+ type +"> is not allowed to run");
        }
    }

    @Override
    public String get(String key) {
        return args.get(key);
    }

    @Override
    public void close() throws Exception {
//        for(Object resource: resources){
//            if(resource instanceof Closeable){
//                ((Closeable) resource).close();
//            }
//        }
        if(obj instanceof Exploit){
            ((Exploit)obj).stop();
        }
    }

    @Override
    public ObjectGadget getObj() {
        return obj;
    }

    public String getType() {
        return type;
    }

    public Class<? extends ObjectGadget> getClazz() {
        return clazz;
    }

    public HashMap<String, String> getArgs() {
        return args;
    }

    public Object getRetObj() {
        return retObj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConsoleObjectSession that = (ConsoleObjectSession) o;
        return sessionID.equals(that.sessionID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionID);
    }

    @Override
    public boolean isExit() {
        if(obj instanceof Exploit){
            return ((Exploit)obj).isExit();
        }
        return false;
    }
}
