package ysomap.gadget.payload;

import ysomap.gadget.ObjectGadget;

@SuppressWarnings ( "rawtypes" )
public interface ObjectPayload <T> extends ObjectGadget <T> {

    /*
     * return armed payload object to be serialized that will execute specified
     * command on deserialization
     */
    @Override
    T getObject() throws Exception;
}
