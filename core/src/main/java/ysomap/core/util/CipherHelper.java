package ysomap.core.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * @author wh1t3P1g
 * @since 2020/8/25
 */
public class CipherHelper {

    public static byte[] encrypt(byte[] plain, byte[] key, byte[] iv){
        try{
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            return cipher.doFinal(plain);
        }catch (Exception e){
            return null;
        }
    }

    public static byte[] decrypt(byte[] plain, String key, byte[] iv){
        try{
            // AES/GCM/NoPadding
            // AES/ECB/PKCS5Padding
            // AES/CBC/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            Key speckey = new SecretKeySpec(key.getBytes(), "AES");
            if(iv != null){
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, speckey, ivParameterSpec);
            }else{
                cipher.init(Cipher.DECRYPT_MODE, speckey);
            }

            return cipher.doFinal(plain);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
