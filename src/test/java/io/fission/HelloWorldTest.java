package io.fission;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

public class HelloWorldTest {

	public static void main(String[] args) {
		// extracted();
		// desStr();
		sm4();
		System.out.println(CryptType.DECRYPT_SM4.typeValue);
	}

	private static void extracted() {
		String content = "test中文";

		//随机生成密钥
		byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();

		//构建
		SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

		//加密
		byte[] encrypt = aes.encrypt(content);
		//解密
		byte[] decrypt = aes.decrypt(encrypt);
		//解密
		System.out.println("普通加密: "+encrypt);
		System.out.println("普通解密: "+decrypt);

		//加密为16进制表示
		String encryptHex = aes.encryptHex(content);

		System.out.println("16 进制加密: "+encryptHex);
		//解密为字符串
		String decryptStr = aes.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);

		System.out.println("16 进制解密: "+decryptStr);
	}

	public static void desStr() {
		String content = "test中文";

		byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DESede.getValue()).getEncoded();

		SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.DESede, key);

		//加密
		byte[] encrypt = des.encrypt(content);
		//解密
		byte[] decrypt = des.decrypt(encrypt);

		System.out.println("普通加密: "+encrypt);
		System.out.println("普通解密: "+decrypt);
		//加密为16进制字符串（Hex表示）
		String encryptHex = des.encryptHex(content);
		System.out.println("16 进制加密: "+encryptHex);
		//解密为字符串
		String decryptStr = des.decryptStr(encryptHex);
		System.out.println("16 进制解密: "+decryptStr);
	}

	public static void sm4(){
		String content = "我是一段测试aaaa";

		SymmetricCrypto sm4 = new SymmetricCrypto("SM4");
		String encryptHex = sm4.encryptHex(content);
		String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);//test中文

		System.out.println(encryptHex+"....."+decryptStr);
	}

}

