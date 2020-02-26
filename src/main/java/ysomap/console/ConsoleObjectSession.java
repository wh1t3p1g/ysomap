package ysomap.console;

import org.apache.commons.lang3.RandomStringUtils;
import ysomap.exploit.Exploit;
import ysomap.gadget.ObjectGadget;
import ysomap.gadget.payload.Payload;
import ysomap.util.Logger;
import ysomap.util.PayloadHelper;
import ysomap.util.Reflections;
import ysomap.util.enums.BulletEnums;
import ysomap.util.enums.ExploitEnums;
import ysomap.util.enums.PayloadEnums;

import java.io.FileOutputStream;
import java.util.*;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ConsoleObjectSession implements Session<String> {

    private String sessionID;
    private String type;
    private Class<? extends ObjectGadget> clazz;
    private ObjectGadget obj;
    private HashMap<String, String> args = new LinkedHashMap<>();
    private Object retObj;
    private List<Object> resources;

    public ConsoleObjectSession(String type) {
        this.type = type;
        sessionID = RandomStringUtils.randomAlphabetic(16);
        resources = new LinkedList<>();
    }

    @Override
    public void accept(String name) throws Exception {
        switch (type){
            case "exploit":
                clazz = ExploitEnums.getClazzByName(name);
                break;
            case "payload":
                clazz = PayloadEnums.getClazzByName(name);
                break;
            case "bullet":
                clazz = BulletEnums.getClazzByName(name);
                break;
        }
        if(clazz != null){
            obj = PayloadHelper.makeGadget(clazz, type);
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
        return Reflections.getField(clazz, key) != null;
    }

    /**
     * only run exploit or payload
     * @throws Exception
     */
    @Override
    public void run() throws Exception {
        if(type.equals("exploit")){// multi thread running
            new Thread(((Exploit)obj)).start();
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
