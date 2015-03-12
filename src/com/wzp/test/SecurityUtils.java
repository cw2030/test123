package com.wzp.test;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 安全通信相关工具类 1）基于私有key的数据签名 2）基本DES算法加密
 * 
 * @author user
 * 
 */
public class SecurityUtils {

	static final byte[] CRYPT_PUB_KEY = new byte[] { (byte) 0xCC, (byte) 0x8F,
			(byte) 0x62, (byte) 0x35, 0x2A, (byte) 0xE6, 0x6A, (byte) 0xA7 };
	private static final Charset ENCODE = Charset.forName("utf-8");
	
	public static String cryptDES(String src, byte[] key, boolean isEncrypt) {
		try {
			// DES算法要求有一个可信任的随机数源
			SecureRandom sr = new SecureRandom();
			// 从原始密匙数据创建DESKeySpec对象
			DESKeySpec dks = new DESKeySpec(key == null ? CRYPT_PUB_KEY : key);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			// 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(dks);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE,
					securekey, sr);
			// 现在，获取数据并加密
			// 正式执行加密操作
			byte[] cryptBytes = cipher.doFinal(isEncrypt ? src
					.getBytes("utf-8") : hex2byte(src.getBytes("utf-8")));
			return isEncrypt ? byte2hex(cryptBytes) : new String(cryptBytes);
		} catch (Exception e) {
			return null;
		}

	}

	private static String byte2hex(byte[] b) {
		StringBuffer result = new StringBuffer(50);
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				result.append("0").append(stmp);
			else
				result.append(stmp);
		}
		return result.toString().toUpperCase();
	}

	private static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}
	
	public static String hmacSign(String aValue, String aKey) {
		byte k_ipad[] = new byte[64];
		byte k_opad[] = new byte[64];
		byte keyb[];
		byte value[];
		try {
			if(aKey == null)
				keyb = CRYPT_PUB_KEY;
			else
				keyb = aKey.getBytes(ENCODE);
			value = aValue.getBytes(ENCODE);
		} catch (Exception e) {
			keyb = aKey.getBytes();
			value = aValue.getBytes();
		}
		Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
		Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
		for (int i = 0; i < keyb.length; i++) {
			k_ipad[i] = (byte) (keyb[i] ^ 0x36);
			k_opad[i] = (byte) (keyb[i] ^ 0x5c);
		}

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		md.update(k_ipad);
		md.update(value);
		byte dg[] = md.digest();
		md.reset();
		md.update(k_opad);
		md.update(dg, 0, 16);
		dg = md.digest();
		return byte2hex(dg);
	}

	public static void main(String[] args) throws Exception {
		String str = "流水";
		String str2 = "高山";
		
		String crypt = cryptDES(str2, null, true);
		String src = cryptDES(crypt, null, false);
		
		System.out.println("crypt:" + crypt);
		System.out.println("src:" + src);
	}

}
