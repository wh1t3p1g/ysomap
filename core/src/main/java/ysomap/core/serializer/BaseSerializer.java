package ysomap.core.serializer;

import ysomap.common.exception.ArgumentsMissMatchException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wh1t3p1g
 * @since 2021/7/30
 */
public abstract class BaseSerializer<T> implements Serializer<T> {

    public String ENCODER = null;
    public Map<String, String> UIDMap = new HashMap<>();

    @Override
    public String getEncoder() {
        return ENCODER;
    }

    @Override
    public void setEncoder(String encoder) {
        ENCODER = encoder;
    }

    @Override
    public void setSerialVersionUID(String UIDMap) throws ArgumentsMissMatchException {
        if(UIDMap != null){
            this.UIDMap.clear();
            String[] UIDs = new String[0];
            if(UIDMap.contains(";")){
                UIDs = UIDMap.split(";");
            }else if(UIDMap.contains(":")){
                UIDs = new String[]{UIDMap};
            }

            for(String uid:UIDs){
                if(uid != null && uid.contains(":")){
                    try{
                        String[] pair = uid.split(":");
                        String oldUID = Long.toHexString(Long.parseLong(pair[0]));
                        String newUID = Long.toHexString(Long.parseLong(pair[1]));
                        this.UIDMap.put(oldUID, newUID);
                    }catch (Exception e){
                        // setting error
                        throw new ArgumentsMissMatchException("set serialVersionUID oldUID1:newUID1;oldUID2:newUID2");
                    }
                }
            }
        }
    }

    @Override
    public Map<String, String> getSerialVersionUID() {
        return UIDMap;
    }
}
