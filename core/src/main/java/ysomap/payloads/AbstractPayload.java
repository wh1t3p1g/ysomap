package ysomap.payloads;


import ysomap.bullets.Bullet;
import ysomap.common.exception.ArgumentsMissMatchException;
import ysomap.common.exception.ObjectTypeErrorException;
import ysomap.common.util.Logger;
import ysomap.core.serializer.Serializer;
import ysomap.core.serializer.SerializerFactory;
import ysomap.core.util.ReflectionHelper;

/**
 * @author wh1t3P1g
 * @since 2021/6/13
 */
public abstract class AbstractPayload<T> implements Payload<T>{

    public Serializer<?> serializer;
    public String serializeType;
    public String serializerOutputType;
    // 自由定义
    public String serializerEncoder;
    public String serializerSerialVersionUID;
    public Bullet bullet;

    public AbstractPayload(){
        serializeType = "default";
        serializerOutputType = "file";
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    public void setSerializeType(String serializeType){
        this.serializeType = serializeType;
    }

    @Override
    public void setEncoder(String encoder) {
        this.serializerEncoder = encoder;
    }

    @Override
    public void setOutputType(String outputType) {
        this.serializerOutputType = outputType;
    }

    @Override
    public void setSerialVersionUID(String uid) {
        this.serializerSerialVersionUID = uid;
    }

    @Override
    final public T getObject() throws Exception {
        // check bullet args first
        ReflectionHelper.checkClassFieldsNotNull(bullet);
        // start to generate payload
        Logger.success("generate payload("+this.getClass().getSimpleName()+") started!");
        Object obj = bullet.getObject();
        if(checkObject(obj)){
            // arm bullet
            T retObj = pack(obj);
            Logger.success("generate payload("+this.getClass().getSimpleName()+") done!");
            return retObj;
        }
        throw new ObjectTypeErrorException(obj);
    }

    public boolean checkObject(Object obj) {
        return true;// 默认不检查也可以，如需检查重载该函数
    }

    public Serializer<?> getSerializer(){
        if(serializeType != null){
            serializer = SerializerFactory.createSerializer(serializeType);
        }else{
            serializer = SerializerFactory.createSerializer("default");
        }

        if(serializerEncoder != null){
            serializer.setEncoder(serializerEncoder);
        }

        if(serializerOutputType != null){
            serializer.setOutputType(serializerOutputType);
        }

        if(serializerSerialVersionUID != null){
            try {
                serializer.setSerialVersionUID(serializerSerialVersionUID);
            } catch (ArgumentsMissMatchException e) {
                Logger.error("SerialVersion UID set error. Do not change!");
            }
        }
        return serializer;
    }

    @Override
    public String getEncoder() {
        return serializerEncoder;
    }

    @Override
    public String getOutputType() {
        return serializerOutputType;
    }

    @Override
    public String getSerialVersionUID() {
        return serializerSerialVersionUID;
    }

    @Override
    public String getSerializeType() {
        return serializeType;
    }

    @Override
    public Payload<T> set(String key, Object value) throws Exception {
        return ReflectionHelper.set(this, key, value);
    }

    @Override
    public String get(String key) {
        return ReflectionHelper.get(this, key);
    }

    @Override
    public boolean has(String key) {
        return ReflectionHelper.has(this, key);
    }

    @Override
    public String getName() {
        String name = this.getClass().getSimpleName();
        if(bullet != null){
            name += "."+bullet.getClass().getSimpleName();
        }
        return name;
    }
}
