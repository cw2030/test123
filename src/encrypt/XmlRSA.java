package encrypt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 处理由C#生成的RSA公钥<br>
 * C#生成的公钥是xml格式的，如：<br>
 * <RSAKeyValue>
 * <Modulus>rLPl8MDJvMkDkNHh0yd6NzbxrdVZ7Pry/GlYQO3QD3j7p/XGNoYgKNkL1s7
 * /xxqQUvbxItGeoijYfTrdQQQySFchEUR6JgWp5dZy2a2YVnXteGvk
 * +qelApUrF8I+gypsaUPl/7uAlW5CT79zkQ5wYkjurvNLlynYDpgsmKsrgJE=</Modulus>
 * <Exponent>AQAB</Exponent> </RSAKeyValue>
 * 
 * @author user
 * 
 */
public class XmlRSA {
    private static final int PRIVATE_KEY = 1;
    private static final int PUBLIC_KEY = 2;
    private static final String[] PUBLIC_KEY_XML_NODES = {"Modulus", "Exponent"};
    public static final int MAXENCRYPTSIZE = 117;

    public static RSAPublicKey getRSAPublicKey(String xmlPublicKey)
            throws MalformedURLException, DocumentException {
//        Security.addProvider(new BouncyCastleProvider());
        Document doc = DocumentHelper.parseText(xmlPublicKey);
        int keyType = getKeyType(doc);
        if (keyType == PRIVATE_KEY || keyType == PUBLIC_KEY) {
            System.out.println("seems to be a "
                               + (keyType == PRIVATE_KEY ? "private" : "public")
                               + " XML Security key");
        } else {
            return null;
        }

        Element root = doc.getRootElement();
        List<Element> children = root.elements();

        BigInteger modulus = null, exponent = null;

        for (int i = 0; i < children.size(); i++) {

            Element node = children.get(i);
            String textValue = node.getText();
            if (node.getName().equals("Modulus")) {
                modulus = new BigInteger(b64decode(textValue));
            } else if (node.getName().equals("Exponent")) {
                exponent = new BigInteger(b64decode(textValue));
            }
        }

        try {

            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);

        }
        catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static RSAPrivateKey getRSAPrivateKey(String xmlPrivateKey)
            throws MalformedURLException, DocumentException {
        Document doc = DocumentHelper.parseText(xmlPrivateKey);
        int keyType = getKeyType(doc);
        if (keyType == PRIVATE_KEY || keyType == PUBLIC_KEY) {
            System.out.println("seems to be a "+ (keyType == PRIVATE_KEY ? "private" : "public") + " XML Security key");
        } else {
            return null;
        }

        Element root = doc.getRootElement();
        List<Element> children = root.elements();

        BigInteger modulus = null, exponent = null, primeP = null, primeQ = null, primeExponentP = null, primeExponentQ = null, crtCoefficient = null, privateExponent = null;

        for (int i = 0; i < children.size(); i++) {

            Element node = children.get(i);
            String textValue = node.getText();
            if (node.getName().equals("Modulus")) {
                modulus = new BigInteger(b64decode(textValue));
            } else if (node.getName().equals("Exponent")) {
                exponent = new BigInteger(b64decode(textValue));
            } else if (node.getName().equals("P")) {
                primeP = new BigInteger(b64decode(textValue));
            } else if (node.getName().equals("Q")) {
                primeQ = new BigInteger(b64decode(textValue));
            } else if (node.getName().equals("DP")) {
                primeExponentP = new BigInteger(b64decode(textValue));
            } else if (node.getName().equals("DQ")) {
                primeExponentQ = new BigInteger(b64decode(textValue));
            } else if (node.getName().equals("InverseQ")) {
                crtCoefficient = new BigInteger(b64decode(textValue));
            } else if (node.getName().equals("D")) {
                privateExponent = new BigInteger(b64decode(textValue));
            }

        }

        try {

            RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, exponent, privateExponent, primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int getKeyType(Document xmldoc) {

        Element root = xmldoc.getRootElement();
        if (!root.getName().equals("RSAKeyValue")) {
            System.out.println("Expecting <RSAKeyValue> node, encountered <" + root.getName() + ">");
            return 0;
        }
        // NodeList children = root.getChildNodes();
        if (root.elements().size() == PUBLIC_KEY_XML_NODES.length) {
            return PUBLIC_KEY;
        }
        return PRIVATE_KEY;

    }

    public static byte[] decodeBase64(String input) throws Exception {
        return Base64.decodeBase64(input);
    }

    public static String encodeBase64(byte[] input) throws Exception {
        return Base64.encodeBase64String(input);
    }

    public static String encrypt(byte[] source, RSAPrivateKey privateKey)
            throws Exception {
        String encryptData = "";
//        Security.addProvider(new BouncyCastleProvider()); 
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            System.out.println(privateKey.getModulus().bitLength()/8);
            int length = source.length;
            int offset = 0;
            byte[] cache;
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            int i = 0;
            while (length - offset > 0) {
                if (length - offset > MAXENCRYPTSIZE) {
                    cache = cipher.doFinal(source, offset, MAXENCRYPTSIZE);
                } else {
                    cache = cipher.doFinal(source, offset, length - offset);
                }
                outStream.write(cache, 0, cache.length);
                i++;
                offset = i * MAXENCRYPTSIZE;
            }
            return encodeBase64(outStream.toByteArray());
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return encryptData;
    }

    public static String decryptByPrivateKey(String data, RSAPublicKey publicKey)
            throws Exception {
//        Security.addProvider(new BouncyCastleProvider()); 
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        // 模长
        int key_len = publicKey.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
        System.err.println(bcd.length);
        // 如果密文长度大于模长则要分组解密
        String ming = "";
        byte[][] arrays = splitArray(bcd, key_len);
        for (byte[] arr : arrays) {
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

    public static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    private static final byte[] b64decode(String data) {
        try {
            sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
            byte[] bytes = dec.decodeBuffer(data.trim());
            return bytes;
        }
        catch (IOException e) {
            System.out.println("Exception caught when base64 decoding!"
                               + e.toString());
        }
        return null;
    }
}
