package com.whstone.utils.EncryptionAndDecryption;


import java.security.MessageDigest;

public class MD5Utils {


    /**
     * MD5加密字符串
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String encryptString(String str) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] md5 = digest.digest(str.getBytes("UTF-8"));
        StringBuffer md5StringBuffer = new StringBuffer();
        String part = null;
        for (int i = 0; i < md5.length; i++) {
            part = Integer.toHexString(md5[i] & 0xFF);
            if (part.length() == 1) {
                part = "0" + part;
            }
            md5StringBuffer.append(part);
        }
        return md5StringBuffer.toString().toUpperCase();
    }


    public static void main(String[] args) throws Exception {
        System.out.println(encryptString("admin"));
    }

}
