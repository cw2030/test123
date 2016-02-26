/**
 * 2009-9-3
 */
package encrypt.sha;

import static org.junit.Assert.assertArrayEquals;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import encrypt.md5.MD5;
import encrypt.md5.MD5Test;
import encrypt.rsa.RSACoder;

/**
 * SHA校验
 * 
 * @author 梁栋
 * @version 1.0
 * @since 1.0
 */
public class SHACoderTest {
    /**
     * 公钥
     */
    private byte[] publicKey;

    /**
     * 私钥
     */
    private byte[] privateKey;
    
    @Test
    public void md5AndSha()throws Exception{
        MD5 md5 = new MD5();
        String md5Result = md5.md5toHex(MD5Test.content, "GBK");
        byte[] md5Result2 = md5.md5(MD5Test.content, "GBK") ;
        System.out.println(md5Result);
        
        byte[] shaResult = SHACoder.encodeSHA(md5Result.getBytes("GBK"));
        byte[] shaResult2 = SHACoder.encodeSHA(md5Result2);
        // 初始化密钥
        Map<String, Object> keyMap = RSACoder.initKey();

        publicKey = RSACoder.getPublicKey(keyMap);
        privateKey = RSACoder.getPrivateKey(keyMap);

//        System.err.println("公钥: \n" + Base64.encodeBase64String(publicKey));
//        System.err.println("私钥： \n" + Base64.encodeBase64String(privateKey));
        byte[] rsaResult = RSACoder.encryptByPrivateKey(shaResult, privateKey);
        System.out.println(Base64.encodeBase64String(rsaResult));
        
        byte[] rsaResult2 = RSACoder.encryptByPrivateKey(shaResult2, privateKey);
        System.out.println(Base64.encodeBase64String(rsaResult2));
    }

	/**
	 * 测试SHA-1
	 * 
	 * @throws Exception
	 */
	@Test
	public final void testEncodeSHA() throws Exception {
		String str = "SHA1消息摘要";
		
		// 获得摘要信息
		byte[] data1 = SHACoder.encodeSHA(str.getBytes());
		byte[] data2 = SHACoder.encodeSHA(str.getBytes());

		// 校验
		assertArrayEquals(data1, data2);
	}

	/**
	 * 测试SHA-256
	 * 
	 * @throws Exception
	 */
	@Test
	public final void testEncodeSHA256() throws Exception {
		String str = "SHA256消息摘要";

		// 获得摘要信息
		byte[] data1 = SHACoder.encodeSHA256(str.getBytes());
		byte[] data2 = SHACoder.encodeSHA256(str.getBytes());

		// 校验
		assertArrayEquals(data1, data2);
	}

	/**
	 * 测试SHA-384
	 * 
	 * @throws Exception
	 */
	@Test
	public final void testEncodeSHA384() throws Exception {
		String str = "SHA384消息摘要";

		// 获得摘要信息
		byte[] data1 = SHACoder.encodeSHA384(str.getBytes());
		byte[] data2 = SHACoder.encodeSHA384(str.getBytes());

		// 校验
		assertArrayEquals(data1, data2);
	}

	/**
	 * 测试SHA-512
	 * 
	 * @throws Exception
	 */
	@Test
	public final void testEncodeSHA512() throws Exception {
		String str = "SHA512消息摘要";

		// 获得摘要信息
		byte[] data1 = SHACoder.encodeSHA512(str.getBytes());
		byte[] data2 = SHACoder.encodeSHA512(str.getBytes());

		// 校验
		assertArrayEquals(data1, data2);
	}
}
