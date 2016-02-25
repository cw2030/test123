package encrypt;

import java.io.ByteArrayOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class RSA {

    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";
    
    /** 
     * String to hold name of the encryption algorithm. 
     */  
    public static final String ALGORITHM = "RSA";  
    
    /** 
     * String to hold name of the security provider. 
     */  
    public static final String PROVIDER = "BC";
  
    /** 
     * String to hold name of the encryption padding. 
     */  
    public static final String PADDING = "RSA";  
    
    public static final int MAXENCRYPTSIZE = 117;
    
    public static final Map<String,Object> getKeys()throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", PROVIDER);
        generator.initialize(1024);
        KeyPair pair = generator.generateKeyPair();
        
        map.put(PUBLIC_KEY, (RSAPublicKey)pair.getPublic());
        map.put(PRIVATE_KEY, (RSAPrivateKey)pair.getPrivate());
        return map;
    }
    
    public static String encrypt(String text, RSAPublicKey key) {  
        byte[] cipherText = null;  
        try {  
            // get an RSA cipher object and print the provider  
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());  
            final Cipher cipher = Cipher.getInstance(PADDING,PROVIDER);  
              
            // encrypt the plain text using the public key  
            cipher.init(Cipher.ENCRYPT_MODE, key);  
            byte[] sources = text.getBytes("utf-8");
            int length = sources.length;  
            int offset = 0;  
            byte[] cache;  
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
            int i = 0;  
            while(length - offset > 0){  
                if(length - offset > MAXENCRYPTSIZE){  
                    cache = cipher.doFinal(sources, offset, MAXENCRYPTSIZE);  
                }else{  
                    cache = cipher.doFinal(sources, offset, length - offset);  
                }  
                outStream.write(cache, 0, cache.length);  
                i++;  
                offset = i * MAXENCRYPTSIZE;  
            }  
            return Base64.encodeBase64String(outStream.toByteArray());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;
    }  
    
    public static String decrypt(String text, RSAPrivateKey privateKey) {  
        byte[] dectyptedText = null;  
        try {  
            // get an RSA cipher object and print the provider  
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());  
            final Cipher cipher = Cipher.getInstance(PADDING,PROVIDER);  
  
            // decrypt the text using the private key  
            byte[] data = Base64.decodeBase64(text);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);  
//            cipher.doFinal(input, inputOffset, inputLen)
            /*int length = data.length;
            int mode = length/MAXENCRYPTSIZE;
            int count = 0;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            while(count < mode){
                outputStream.write(cipher.doFinal(data, count * MAXENCRYPTSIZE, MAXENCRYPTSIZE));
                count++;
            }
            if(length != mode * MAXENCRYPTSIZE){
                outputStream.write(cipher.doFinal(data, mode * MAXENCRYPTSIZE, length-mode * MAXENCRYPTSIZE));
            }*/
            dectyptedText = cipher.doFinal(data);    
//            return new String(outputStream.toByteArray(),"utf-8");
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
  
        return new String(dectyptedText);  
    }  
    
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {  
        byte[] bcd = new byte[asc_len / 2];  
        int j = 0;  
        for (int i = 0; i < (asc_len + 1) / 2; i++) {  
            bcd[i] = asc_to_bcd(ascii[j++]);  
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));  
        }  
        return bcd;  
    }  
    public static byte asc_to_bcd(byte asc) {  
        byte bcd;  
  
        if ((asc >= '0') && (asc <= '9'))  
            bcd = (byte) (asc - '0');  
        else if ((asc >= 'A') && (asc <= 'F'))  
            bcd = (byte) (asc - 'A' + 10);  
        else if ((asc >= 'a') && (asc <= 'f'))  
            bcd = (byte) (asc - 'a' + 10);  
        else  
            bcd = (byte) (asc - 48);  
        return bcd;  
    }  
    
    public static byte[][] splitArray(byte[] data,int len){  
        int x = data.length / len;  
        int y = data.length % len;  
        int z = 0;  
        if(y!=0){  
            z = 1;  
        }  
        byte[][] arrays = new byte[x+z][];  
        byte[] arr;  
        for(int i=0; i<x+z; i++){  
            arr = new byte[len];  
            if(i==x+z-1 && y!=0){  
                System.arraycopy(data, i*len, arr, 0, y);  
            }else{  
                System.arraycopy(data, i*len, arr, 0, len);  
            }  
            arrays[i] = arr;  
        }  
        return arrays;  
    }  
}
