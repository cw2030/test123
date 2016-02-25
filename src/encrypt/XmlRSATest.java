package encrypt;

import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.net.util.Base64;
import org.junit.Before;
import org.junit.Test;

public class XmlRSATest {
    public static final String publicKey = "<RSAKeyValue><Modulus>rLPl8MDJvMkDkNHh0yd6NzbxrdVZ7Pry/GlYQO3QD3j7p/XGNoYgKNkL1s7/xxqQUvbxItGeoijYfTrdQQQySFchEUR6JgWp5dZy2a2YVnXteGvk+qelApUrF8I+gypsaUPl/7uAlW5CT79zkQ5wYkjurvNLlynYDpgsmKsrgJE=</Modulus>"
            + "<Exponent>AQAB</Exponent></RSAKeyValue>";
    public static final String privateKey = "<RSAKeyValue><Modulus>rLPl8MDJvMkDkNHh0yd6NzbxrdVZ7Pry/GlYQO3QD3j7p/XGNoYgKNkL1s7/xxqQUvbxItGeoijYfTrdQQQySFchEUR6JgWp5dZy2a2YVnXteGvk+qelApUrF8I+gypsaUPl/7uAlW5CT79zkQ5wYkjurvNLlynYDpgsmKsrgJE=</Modulus>"
            + "<Exponent>AQAB</Exponent><P>02qPkcYDBDp8r2U1EqoL/RJrM3UhSNZbssvHomYWbLrzXEiRiHy9pNEOcPlq2PBJxD137R6qr9FOkJqvequLBw==</P><Q>0R9dBtYc77DA6gtKDZuktgyNP0MXS4LzvFEqtq9BjBZ/JBXHolJCudYGkAp4AhxCETD0thf11J9gynzUmCb5pw==</Q><DP>nzYejH78ApExGL007KtWf+0BAi1xNXMId2tzGd+bf6KCZrrXrluSTa6KG5YZWuoKA1jvGYkArYsIiWmUQOMyvQ==</DP>"
            + "<DQ>rWGTf1eGJjQlveYeP6oLpeRCN3EonzKzYi7pew1TCxKb1w83tRz+tZT9W+9SEG3dWON+AHtdFiwN09QrbvrhgQ==</DQ><InverseQ>KRDkoR39i+cn6r/W0dFjICuNHKG9R0Wky6lXWtHPUqSCBuPZDzueN6wD7OboH2H5JxOTXGIBmP6NAef8+FEpxQ==</InverseQ><D>GTCYLliAVFfhfEMQtDLC4cDS2W8QEb+8p1JaPAYz3b3gvuvbQGKp2CtoTamdpxXZzTAVleWLRM2+Xe2zDeOlzagI4aFr3ejCrVlQuZJ5ZpIWepYGkDwuQ3Aeihhfl9xBZRK5+3KJZFuFYbkbIwZ93m60W3fAxwTzIfIP3BQojn0=</D></RSAKeyValue>";

    @Before
    public void setUp() throws Exception {}

    @Test
    public void test() throws Exception{
        String source = "abcefw";
//        source="RSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSARSAAAAAAAAAAAAAAAAAAAAA";
        
        RSAPublicKey rsaPublicKey = XmlRSA.getRSAPublicKey(publicKey);
        RSAPrivateKey rsaPrivateKey = XmlRSA.getRSAPrivateKey(privateKey);
        
        
        
        String encrypt = XmlRSA.encrypt(source.getBytes("utf-8"),rsaPrivateKey);
        System.out.println(encrypt);
        
        
        String plainText = XmlRSA.decryptByPrivateKey(encrypt, rsaPublicKey);
        System.out.println(plainText);
    }
    
    @Test
    public void test2() throws Exception {
        try{
            RSAPublicKey rsaPublicKey = XmlRSA.getRSAPublicKey(publicKey);
            RSAPrivateKey rsaPrivateKey = XmlRSA.getRSAPrivateKey(privateKey);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            String encrypt = Base64.encodeBase64String(cipher.doFinal("abcd".getBytes()));
            System.out.println(encrypt);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    

}
