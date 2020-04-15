package ysomap.core;

public interface ObjectPayload <T> extends ObjectGadget <T> {

    /**
     * 检查obj的类型是否符合当前payload的要求
     * @param obj
     * @return
     */
    boolean checkObject(Object obj);

    /**
     * 装弹，将最终达成的利用效果拼接反序列化利用链
     * 反序列化利用链的实现也在这部分实现
     * @param obj
     * @return
     */
    T pack(Object obj) throws Exception;
}
