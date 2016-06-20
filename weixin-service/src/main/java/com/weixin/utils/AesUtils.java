package com.weixin.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 加密请求参数
 * @author: fuhongxing
 * @date:   2015年4月1日
 * @version 1.0.0
 */
public class AesUtils {
	private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	private static final String KEY_ALGORITHM = "AES";
	private static final String ENCODING = "utf-8";
	private static final String KEY = "$1$jXJ0a/At$BJy.";//密钥
	private static Logger LOGGER = LoggerFactory.getLogger(AesUtils.class);
	
	
	/**
	 * 加密
	 * @param content
	 * @return
	 */
	public static String valueEncrypt(String value) {
		try {
			return AesUtils.encrypt(value);
		} catch (Exception e) {
			return value;
		}
	}
	
	
	/**
	 * 解密
	 * @param content
	 * @return
	 */
	public static String valueDecrypt(String content) {
		try {
			String text = decrypt(content);
			return StringUtils.isEmpty(text) ? content : text;
		} catch (Exception e) {
			return content;
		}
	}
	
	
	/** 
     * 加密方式--这种加密方式有两种限制 
     * 1、密钥必须是16位的 
     * 2、待加密内容的长度必须是16的倍数，如果不是16的倍数，就会出如下异常 
     * javax.crypto.IllegalBlockSizeException: Input length not multiple of 16 bytes 
        at com.sun.crypto.provider.SunJCE_f.a(DashoA13*..) 
        at com.sun.crypto.provider.SunJCE_f.b(DashoA13*..) 
        at com.sun.crypto.provider.SunJCE_f.b(DashoA13*..) 
        at com.sun.crypto.provider.AESCipher.engineDoFinal(DashoA13*..) 
        at javax.crypto.Cipher.doFinal(DashoA13*..) 
        要解决如上异常，可以通过补全传入加密内容等方式进行避免。 
     * @method encrypt2 
     * @param content   需要加密的内容 
     * @param password  加密密码 
     * @return 
     * @throws  
     * @since v1.0 
     */  
	public static String encrypt(String content) {
		SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), KEY_ALGORITHM);
		Cipher cipher;
		try {
			//Cipher对象实际完成解密操作
			cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			//用密钥加密明文(plainText),生成密文(cipherText)
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);// 初始化
			//得到加密后的字节数组
			byte[] result = cipher.doFinal(content.getBytes(ENCODING));
			return Hex.encodeHexString(result);//转16进制
		} catch (Exception e) {
			LOGGER.error("加密 "+content+" 失败!!!", e);
			throw new RuntimeException("加密失败" + content, e);
		}
	}
	
	public static String decrypt(String content) {
		SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), KEY_ALGORITHM);
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, keySpec);// 初始化
			byte[] result = cipher.doFinal(Hex.decodeHex(content.toCharArray()));
			return new String(result);
		} catch (Exception e) {
			LOGGER.error("解密 "+content+" 失败!!!", e);
			throw new RuntimeException("解密失败" + content, e);
		}
	}
	

	public static String urlEncrypt(String url) {
		StringBuffer sb = new StringBuffer();
		try {
			int index = url.indexOf("?");
			if (index == -1) {
				sb.append(url);
			} else {
				sb.append(url.substring(0, index));
				String estr = url.substring(index);
				String[] group = estr.split("&");
				for (int i = 0; i < group.length; i++) {
					String[] b = group[i].split("=");
					sb.append(b[0]);
					if (b.length == 2) {
						sb.append("=").append(valueEncrypt(b[1]));
					}
					sb.append("&");
				}
				sb.deleteCharAt(sb.length() - 1);
			}
		} catch (Exception e) {
			sb.append(url);
		}
		return sb.toString();
	}
	
	
}