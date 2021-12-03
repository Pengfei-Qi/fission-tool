package io.fission.util;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: TODO 加解密工具
 * @author: qpf
 * @date: 2021/12/2
 * @version: 1.0
 */
public class DeAndEnUtils {

    public static Map<String, Object> enAes(String content){
        Map<String, Object>  aesEn = new HashMap<>();
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        byte[] encrypt = aes.encrypt(content);
        String encryptHex = aes.encryptHex(content);
        aesEn.put("encrypt",String.valueOf(encrypt));
        aesEn.put("encryptHex",encryptHex);
        return aesEn;
    }

    public static Map<String, Object> deAes(String content){
        Map<String, Object>  aesDe = new HashMap<>();
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue()).getEncoded();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        byte[] decrypt = aes.decrypt(content);
        String decryptStr = aes.decryptStr(content, CharsetUtil.CHARSET_UTF_8);
        aesDe.put("decrypt",decrypt);
        aesDe.put("decryptStr",decryptStr);
        return aesDe;
    }

    public static Map<String, Object> enDes(String content){
        Map<String, Object> desEn = new HashMap<>();
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DESede.getValue()).getEncoded();
        SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.DESede, key);
        byte[] encrypt = des.encrypt(content);
        String encryptHex = des.encryptHex(content);
        desEn.put("encrypt",String.valueOf(encrypt));
        desEn.put("encryptHex",encryptHex);
        return desEn;
    }

    public static Map<String, Object> deDes(String content){
        Map<String, Object> desDe = new HashMap<>();
        byte[] key = SecureUtil.generateKey(SymmetricAlgorithm.DESede.getValue()).getEncoded();
        SymmetricCrypto des = new SymmetricCrypto(SymmetricAlgorithm.DESede, key);
        byte[] decrypt = des.decrypt(content);
        String decryptStr = des.decryptStr(content, CharsetUtil.CHARSET_UTF_8);
        desDe.put("decrypt",decrypt);
        desDe.put("decryptStr",decryptStr);
        return desDe;
    }

    public static Map<String, Object> enSm4(String content){
        Map<String, Object> sm4De = new HashMap<>();
        SymmetricCrypto sm4 = new SymmetricCrypto("SM4");
        String encryptHex = sm4.encryptHex(content);
        sm4De.put("encryptHex",encryptHex);
        return sm4De;
    }

    public static Map<String, Object> deSm4(String content){
        Map<String, Object> sm4En = new HashMap<>();
        SymmetricCrypto sm4 = new SymmetricCrypto("SM4/ECB/PKCS5Padding");
        String decryptStr = sm4.decryptStr(content, CharsetUtil.CHARSET_UTF_8);//test中文
        sm4En.put("decryptStr",decryptStr);
        return sm4En;
    }
}
