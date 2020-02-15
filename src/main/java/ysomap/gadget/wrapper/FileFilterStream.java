package ysomap.gadget.wrapper;

import ysomap.gadget.ObjectGadget;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * @author wh1t3P1g
 * @since 2020/2/15
 */
public class FileFilterStream extends FilterStream<Object> {

    private String filename;

    public FileFilterStream(ObjectGadget<Object> payload, String filename) {
        super(payload);
        this.filename = filename;
    }

    @Override
    public Boolean getObject() throws Exception {
        Object obj = payload.getObject();
        try (BufferedWriter bw =
                     new BufferedWriter(new FileWriter(filename))){
            bw.write(obj.toString());
        }
        return true;
    }
}
