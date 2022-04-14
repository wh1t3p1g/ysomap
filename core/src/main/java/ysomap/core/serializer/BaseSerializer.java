package ysomap.core.serializer;

import ysomap.common.exception.ArgumentsMissMatchException;
import ysomap.core.util.ByteHelper;
import ysomap.payloads.Payload;

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
        this.UIDMap.clear();
        if(UIDMap != null){
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

    @Override
    public T serialize(Payload payload) throws Exception {
        setSerialVersionUID(payload.getSerialVersionUID());
        Object obj = serialize(payload.getObject());
        if(obj instanceof byte[] && !getSerialVersionUID().isEmpty()){
            Map<String, String> uidMap = getSerialVersionUID();
            String oldByteHexString = ByteHelper.bytesToHexString((byte[]) obj);

            for(String oldUid: uidMap.keySet()){
                if(oldByteHexString.contains(oldUid)){
                    String newUid = uidMap.get(oldUid);
                    oldByteHexString = oldByteHexString.replace(oldUid, newUid);
                }
            }

            obj = ByteHelper.hexStringToBytes(oldByteHexString);
        }
        return (T) obj;
    }
}
