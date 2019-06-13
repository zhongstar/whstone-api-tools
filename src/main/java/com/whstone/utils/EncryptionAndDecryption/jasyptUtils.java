package com.whstone.utils.EncryptionAndDecryption;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

/**
 * Created by weijun on 2017/11/23.
 */
public class jasyptUtils {


    //加密
    public String encrypt(String plantKey) {
        // 创建加密器
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        // 配置
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");// 加密算法
        config.setPassword("fuyung");// 系统属性值
        encryptor.setConfig(config);

        //  String plaintext = "root"; //明文
        String ciphertext = encryptor.encrypt(plantKey); // 加密
        return ciphertext;

    }


    //解密
    public String decrypt(String ciphertext) {

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setPassword("fuyung");
        encryptor.setConfig(config);
        //   String ciphertext = "8y9G4kIZQuCHB78mMJNkHw==";// 密文

        //解密
        String plaintext = encryptor.decrypt(ciphertext); // 解密
        return plaintext;
    }
}
