package encrypt;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

public class ThreeDESUtilTest {
    private String encode = "gbk";

    @Before
    public void setUp() throws Exception {}

    @Test
    public void test() throws Exception{
        try{
            /**
             * 农行测试密钥
             */
            byte[] key = Base64.decodeBase64("e8RbahwfDDiUJ3xz5geZFdF1SVBPvbep");
            byte[] keyiv = Base64.decodeBase64("qhkKtbW+KlY=");
            
            byte[] data = "3300161823505300864196".getBytes(encode);
            System.out.println("data.length=" + data.length);
            
            System.out.println("CBC加密解密");
            
            byte[] str5 = ThreeDES.des3EncodeCBC(key, keyiv, data);
            System.out.println(new sun.misc.BASE64Encoder().encode(str5));
            System.out.println(Base64.encodeBase64String(str5));
            byte[] str6 = ThreeDES.des3DecodeCBC(key, keyiv, str5);
            System.out.println(new String(str6, encode));
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

}
