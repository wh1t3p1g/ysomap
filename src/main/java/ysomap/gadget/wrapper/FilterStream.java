package ysomap.gadget.wrapper;

import ysomap.gadget.ObjectGadget;

/**
 * 流装饰器
 * 用于包装反序列化利用链最终产生的Object
 * 如将该Object进行序列化(SerializeFilterStream)
 * 或将序列化后的字符串或byte数组进行输出预处理(URL编码URLFilterStream,文件输出FileFilterStream)
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public abstract class FilterStream<T> implements ObjectGadget<T> {
    ObjectGadget<T> payload;

    public FilterStream(ObjectGadget<T> payload) {
        this.payload = payload;
    }
}
