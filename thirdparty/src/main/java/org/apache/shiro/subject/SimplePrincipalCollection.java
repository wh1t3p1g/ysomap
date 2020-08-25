package org.apache.shiro.subject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

/**
 * @author wh1t3P1g
 * @since 2020/8/25
 */
public class SimplePrincipalCollection implements Serializable {
    // Serialization reminder:
    // You _MUST_ change this number if you introduce a change to this class
    // that is NOT serialization backwards compatible.  Serialization-compatible
    // changes do not require a change to this number.  If you need to generate
    // a new number in this case, use the JDK's 'serialver' program to generate it.
    private static final long serialVersionUID = -6305224034025797558L;

    //TODO - complete JavaDoc

    private Map<String, Set> realmPrincipals;

    private transient String cachedToString; //cached toString() result, as this can be printed many times in logging

    public SimplePrincipalCollection() {
    }


    /**
     * Serialization write support.
     * <p/>
     * NOTE: Don't forget to change the serialVersionUID constant at the top of this class
     * if you make any backwards-incompatible serialization changes!!!
     * (use the JDK 'serialver' program for this)
     *
     * @param out output stream provided by Java serialization
     * @throws IOException if there is a stream error
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        boolean principalsExist = false;
        out.writeBoolean(principalsExist);
        if (principalsExist) {
            out.writeObject(realmPrincipals);
        }
    }

    /**
     * Serialization read support - reads in the Map principals collection if it exists in the
     * input stream.
     * <p/>
     * NOTE: Don't forget to change the serialVersionUID constant at the top of this class
     * if you make any backwards-incompatible serialization changes!!!
     * (use the JDK 'serialver' program for this)
     *
     * @param in input stream provided by
     * @throws IOException            if there is an input/output problem
     * @throws ClassNotFoundException if the underlying Map implementation class is not available to the classloader.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        boolean principalsExist = in.readBoolean();
        if (principalsExist) {
            this.realmPrincipals = (Map<String, Set>) in.readObject();
        }
    }
}
