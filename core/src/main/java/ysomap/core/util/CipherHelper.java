package ysomap.core.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

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
}
