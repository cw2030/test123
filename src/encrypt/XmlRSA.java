package encrypt;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

/**
 * 处理由C#生成的RSA公钥<br>
 * C#生成的公钥是xml格式的，如：<br>
 * <RSAKeyValue>
      <Modulus>rLPl8MDJvMkDkNHh0yd6NzbxrdVZ7Pry/GlYQO3QD3j7p/XGNoYgKNkL1s7/xxqQUvbxItGeoijYfTrdQQQySFchEUR6JgWp5dZy2a2YVnXteGvk+qelApUrF8I+gypsaUPl/7uAlW5CT79zkQ5wYkjurvNLlynYDpgsmKsrgJE=</Modulus>
      <Exponent>AQAB</Exponent>
   </RSAKeyValue>
 * @author user
 *
 */
public class XmlRSA {
    public static final int MAXENCRYPTSIZE = 117;
    public static HashMap<String,String> rsaParameters(String xmlPublicKey) throws MalformedURLException, DocumentException{  
        HashMap<String ,String> map = new HashMap<String, String>();   
        Document doc = DocumentHelper.parseText(xmlPublicKey);  
        String mudulus = (String) doc.getRootElement().element("Modulus").getData();  
        String exponent = (String) doc.getRootElement().element("Exponent").getData();  
        map.put("mudulus", mudulus);  
        map.put("exponent", exponent);  
        return map;  
    }  
    
    public static byte[] decodeBase64(String input) throws Exception{
        return Base64.decodeBase64(input);
        /*Class<?> clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");    
        Method mainMethod= clazz.getMethod("decode", String.class);    
        mainMethod.setAccessible(true);    
         Object retObj=mainMethod.invoke(null, input);    
         return (byte[])retObj;    */
    }
    
    public static String encodeBase64(byte[] input) throws Exception{
        return Base64.encodeBase64String(input);
        /*Class<?> clazz=Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");    
        Method mainMethod= clazz.getMethod("encode", String.class);    
        mainMethod.setAccessible(true);    
         Object retObj=mainMethod.invoke(null, input); 
         return (String)retObj;    */
    }
    
    public static RSAPublicKey getPublicKey(String modulus, String exponent){  
        try {   
            byte[] m = decodeBase64(modulus);  
            byte[] e = decodeBase64(exponent); 
            
            BigInteger b1 = new BigInteger(1,m);    
            BigInteger b2 = new BigInteger(1,e);    
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");    
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);    
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);    
        } catch (Exception e) {    
            e.printStackTrace();    
            return null;    
        }     
    }  
    
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {  
        try {  
            BigInteger b1 = new BigInteger(decodeBase64(modulus));  
            BigInteger b2 = new BigInteger(decodeBase64(exponent));  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);  
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
    
    public static String encrypt(byte[] source, PublicKey publicKey) throws Exception   {  
        String encryptData ="";  
        try {  
            Cipher cipher = Cipher.getInstance("RSA");  
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);  
            int length = source.length;  
            int offset = 0;  
            byte[] cache;  
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
            int i = 0;  
            while(length - offset > 0){  
                if(length - offset > MAXENCRYPTSIZE){  
                    cache = cipher.doFinal(source, offset, MAXENCRYPTSIZE);  
                }else{  
                    cache = cipher.doFinal(source, offset, length - offset);  
                }  
                outStream.write(cache, 0, cache.length);  
                i++;  
                offset = i * MAXENCRYPTSIZE;  
            }  
            return encodeBase64(outStream.toByteArray());  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        } catch (NoSuchPaddingException e) {  
            e.printStackTrace();  
        } catch (InvalidKeyException e) {  
            e.printStackTrace();  
        } catch (IllegalBlockSizeException e) {  
            e.printStackTrace();  
        } catch (BadPaddingException e) {  
            e.printStackTrace();  
        }  
        return encryptData;       
    }  
    
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)  
            throws Exception {  
        Cipher cipher = Cipher.getInstance("RSA");  
        cipher.init(Cipher.DECRYPT_MODE, privateKey);  
        //模长  
        int key_len = privateKey.getModulus().bitLength() / 8;  
        byte[] bytes = data.getBytes();  
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);  
        System.err.println(bcd.length);  
        //如果密文长度大于模长则要分组解密  
        String ming = "";  
        byte[][] arrays = splitArray(bcd, key_len);  
        for(byte[] arr : arrays){  
            ming += new String(cipher.doFinal(arr));  
        }  
        return ming;  
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
