package com.business;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName EncryptUtils
 * @Description 加密算法工具类
 * @Author ysl1397940314@163.com
 * @CreateTime 2018/8/22 上午11:38
 */
public class EncryptUtils {

    private static final String SHA256_TYPE_STR="SHA-256";


    public static String sha256Encrypt(String password){

        return SHA(password,SHA256_TYPE_STR);

    }

    private static String SHA(String context,String SHAType){
        try {
           MessageDigest messageDigest= MessageDigest.getInstance(SHAType);
           messageDigest.update(context.getBytes());
           byte [] bytes =messageDigest.digest();
           String password = bytes.toString();
           return password;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
