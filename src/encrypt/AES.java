package encrypt;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * AES 算法 对称加密，密码学中的高级加密标准 2005年成为有效标准 
 * @author stone
 * @date 2014-03-10 06:49:19
 */
public class AES {
    public static final int AES_KEY = 10000000;
    public static final String AES_KEY_PREFIX = "huabaoec";
    static final String KEY_ALGORITHM = "AES";
    static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";
    static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";
    static final String CIPHER_ALGORITHM_CBC_NoPadding = "AES/CBC/NoPadding"; 
    
    static SecretKey secretKey;
        
    public static void main(String[] args) throws Exception {
        String es = encrypt("abc",1);
        String ds = decrypt(es,2);
        System.out.println(es);
        System.out.println(ds);
    }
    
    public static String encrypt(String content,int randomKey)throws Exception{
        Cipher c = Cipher.getInstance(KEY_ALGORITHM);
        
        SecretKeySpec sks = new SecretKeySpec((AES_KEY_PREFIX+(AES_KEY + randomKey)).getBytes(), KEY_ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, sks);
        return Base64.encodeBase64String(c.doFinal(content.getBytes())); 
    }
    
    public static String decrypt(String content,int randomKey)throws Exception{
        Cipher c = Cipher.getInstance(KEY_ALGORITHM);
        SecretKeySpec sks = new SecretKeySpec((AES_KEY_PREFIX+(AES_KEY + randomKey)).getBytes(), KEY_ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, sks);
        
        return new String(c.doFinal(Base64.decodeBase64(content))); 
    }
    
    
}

