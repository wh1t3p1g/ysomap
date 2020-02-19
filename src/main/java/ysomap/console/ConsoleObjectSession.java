package ysomap.console;

import ysomap.exploit.Exploit;
import ysomap.gadget.ObjectGadget;
import ysomap.util.PayloadHelper;
import ysomap.util.Reflections;
import ysomap.util.enums.BulletEnums;
import ysomap.util.enums.ExploitEnums;
import ysomap.util.enums.PayloadEnums;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author wh1t3P1g
 * @since 2020/2/19
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ConsoleObjectSession implements Session<String> {

    private String type;
    private Class<? extends ObjectGadget> clazz;
    private ObjectGadget obj;
    private HashMap<String, String> args = new LinkedHashMap<>();
    private Object retObj;

    public ConsoleObjectSession(String type) {
        this.type = type;
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
    public void set(String key, String value) throws Exception {
        args.remove(key);// clear current key:value
        args.put(key, value);
        if(obj != null){
            obj.set(key, value);
        }
    }

    @Override
    public boolean has(String key) {
        return Reflections.getField(clazz, key) != null;
    }

    @Override
    public void run() throws Exception {
        if(type.equals("exploit")){
            ((Exploit)obj).run();
        }else{
            retObj = obj.getObject();
        }
    }

    @Override
    public String get(String key) {
        return args.get(key);
    }
}
