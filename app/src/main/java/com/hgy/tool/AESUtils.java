package com.hgy.tool;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import httpcode.org.apache.commons.codec.binary.StringUtils;

public class AESUtils {
    private static final int KEY_SIZE = 128;
    public static final String KEY_ALGORITHM = "AES";
    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    public AESUtils() {
    }

    public static byte[] encrypt(byte[] content, byte[] key) {
        if (content != null && content.length != 0) {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                secureRandom.setSeed(key);
                kgen.init(128, secureRandom);
                SecretKey secretKey = kgen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(1, secretKeySpec);
                byte[] result = cipher.doFinal(content);
                return result;
            } catch (Exception var9) {
                throw new RuntimeException(var9);
            }
        } else {
            return null;
        }
    }

    public static String encrypt(String content, String key) {
        try {
            return DcUtil.encodeHexString(encrypt(content.getBytes("UTF-8"), key.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException var3) {
            throw new RuntimeException("不支持UTF-8字符集,", var3);
        }
    }

    public static byte[] decrypt(byte[] content, byte[] key) {
        if (content != null && content.length != 0) {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
                secureRandom.setSeed(key);
                kgen.init(128, secureRandom);
                SecretKey secretKey = kgen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(2, secretKeySpec);
                byte[] result = cipher.doFinal(content);
                return result;
            } catch (Exception var9) {
                throw new RuntimeException(var9);
            }
        } else {
            return null;
        }
    }

    public static String decrypt(String content, String key) {
        try {
            return StringUtils.isBlank(content) ? null : new String(decrypt(DcUtil.decodeHex(content.toCharArray()), key.getBytes("UTF-8")), "UTF-8");
        } catch (Exception var3) {
            throw new RuntimeException("不支持UTF-8字符集,", var3);
        }
    }
}
