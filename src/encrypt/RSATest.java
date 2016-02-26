package encrypt;

import java.security.Provider;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RSATest {

    @Before
    public void setUp() throws Exception {}

    @Test
    public void test() throws Exception{
        Map<String,Object> keys = RSA.getKeys();
        RSAPublicKey pk = (RSAPublicKey)keys.get(RSA.PUBLIC_KEY);
        RSAPrivateKey priKey = (RSAPrivateKey)keys.get(RSA.PRIVATE_KEY);
        
        String source = "使用模和指数生成RSA私钥使用模和指数生成RSA私钥使用模和指数生成RSA私a使用模";
        String encrypt = RSA.encrypt(source, pk);
        System.out.println(encrypt);
        String plainText = RSA.decrypt(encrypt, priKey);
        
        System.out.println(plainText);
    }

    private void printAll(){
        Provider[] pro = Security.getProviders();
        for (Provider p : pro) {
            System.out.println("Provider:"
                               + p.getName()
                               + " - version:"
                               + p.getVersion());
            System.out.println(p.getInfo());
        }
    }
}
