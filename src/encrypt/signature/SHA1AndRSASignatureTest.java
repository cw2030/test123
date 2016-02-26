/**
 * 2008-6-11
 */
package encrypt.signature;

import static org.junit.Assert.assertTrue;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

import encrypt.md5.MD5;
import encrypt.md5.MD5Test;
import encrypt.rsa.XmlRSA;
import encrypt.rsa.XmlRSAKeyValue;
import encrypt.sha.SHACoder;

/**
 * RSA数字签名校验
 * 
 * @author 梁栋
 * @version 1.0
 */
public class SHA1AndRSASignatureTest {

	/**
	 * 公钥
	 */
	private byte[] publicKey;

	/**
	 * 私钥
	 */
	private byte[] privateKey;

	/**
	 * 初始化密钥
	 * 
	 * @throws Exception
	 */
	@Before
	public void initKey() throws Exception {

		Map<String, Object> keyMap = Maps.newHashMap();
		RSAPublicKey rsaPublicKey = XmlRSA.getRSAPublicKey(XmlRSAKeyValue.publicKey);
		RSAPrivateKey rsaPrivateKey = XmlRSA.getRSAPrivateKey(XmlRSAKeyValue.privateKey);
		keyMap.put(SHA1AndRSASignature.PUBLIC_KEY, rsaPublicKey);
		keyMap.put(SHA1AndRSASignature.PRIVATE_KEY, rsaPrivateKey);

		publicKey = SHA1AndRSASignature.getPublicKey(keyMap);

		privateKey = SHA1AndRSASignature.getPrivateKey(keyMap);
		
		
		System.err.println("公钥: \n" + Base64.encodeBase64String(publicKey));
		System.err.println("私钥： \n" + Base64.encodeBase64String(privateKey));
	}

	/**
	 * 校验
	 * 
	 * @throws Exception
	 */
	@Test
	public void testSign() throws Exception {
	    
	    MD5 md5 = new MD5();
        byte[] md5Result2 = md5.md5(MD5Test.content, "GBK") ;
        String md5Str = MD5.byteToHexString(md5Result2);
        System.out.println(md5Str);
        
		// 产生签名
		byte[] sign = SHA1AndRSASignature.sign(md5Str.getBytes("GBK"), privateKey);
		System.out.println("==签名:\n" + Base64.encodeBase64String(sign));

		// 验证签名
		boolean status = SHA1AndRSASignature.verify(md5Str.getBytes("GBK"), publicKey, sign);
		System.err.println("状态:\n" + status);
		assertTrue(status);

	}

}
