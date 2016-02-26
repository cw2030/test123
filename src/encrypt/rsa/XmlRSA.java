package encrypt.rsa;

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
