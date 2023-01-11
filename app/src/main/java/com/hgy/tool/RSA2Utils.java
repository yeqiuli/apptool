package com.hgy.tool;


import android.util.Log;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import javax.crypto.Cipher;

import httpcode.org.apache.commons.codec.binary.Base64;

public class RSA2Utils {
    private static final String KEY_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 128;//设置长度
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String RSA_TYPE = "RSA/ECB/PKCS1Padding";

    private final static String PUBLIC_KEY_NAME = "bbc6d864ca884249a3ec351951627ce54d57c353b55b4917b5fd450a23900204";
    private final static String PRIVATE_KEY_NAME = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCMO9GVLNicOYjC9I47SQugJY8hHb2EkMftcWaMZbj0iY0iruCltYgiFGvol8V/pj1T2CXbncAReyLh8yzc3k8imrAHDPBw5QNH0+c7jIvPo/ZMuFwGoSXIZ2FB2j6tGZhA6lUsM1yjtFy1DpNjBqjUati3h/9A1FaRh5gu0rK3clNHWQmpA+7a8j8abwdabgYVXxl4kUQM9bVmmCMy73OBCO/ojs/2StS0tQOvu1GfVkLGeUqu2fHwfuvFdLuIhlx5b9nlN7BO5mkHgQevfpoeojv5pYLrsrqiqqHxrHV4uvHoCDryTCfL7AP6xxNncwrgKaEBydOsTsajsyx8xTL5AgMBAAECggEAZh2tKU7F0UVIJIcHB37Se2S+TCQm/GCdZXc9cKEmRNPhNU9ZAPIm1oI+bdoPFDwOzn5IWxsNYO1k4lo84fz0bUNtSUUP1XW+pNBWwpM+wA4qbYWWZbF1HPDC7rwpBc3sg+Df8tbX79GH7MRXaTXRPRUtCMB8jE3TWBgAJqCBSXlftLa6NZhSRiWWpkCM6CpaaBe34IfJTPYoKBFHosAfd6gKp+z/C7OKkHbwr2+yO5YXHwgjiHFerhqQM9aTmRRPo+TtNrnH8D5Bl5TezGZGFlK9jAJntHaB+WTHDWioFhxJp0gTVDvQAy3qiF6K13XLftr+k0DYKnE6t3KwSJHsgQKBgQDEJxmbHYpd3SJjHJR9+Ox7v5/5v0A0qv1+Rwy2YtCHlQgTcuNNEwFJeGz4E/cTELu3fvIv2SFIcL15o+sOvn9YVvQ+n2Igf5uiPVIdT5uxbfzd5KisxzmjCXit0liCNA/eux7lhX0ZZOu7Nksnb8C/doODh9JYKQsYUWbBljUeSQKBgQC3BQxq9xGmafZOaatg2Y6zhmJIyUQ4CN3EzFu2Fsku1lQy+D/W441mlixpv+46IUcZRuCVYQFxsnlEhYuhbfBq+64NPo4OvUs6XqB0PEBBdeAwCUhz+2cis/mZbsTuJuZaAKF1rGmVg0QUZliCzPdrR0hZtJHAeBrC49A8V7MvMQKBgQC8+DI59raTgPOc6i7AQayJ921GLJLNaqG2IvrDRuVAiTnziq4iVZnazxKj2JAiJO/DcqAdqp/e6wYPBvTwCmQilfrNzby6NFWaclsGc+g5gg2nM5+wfGoxgHFrfAbawQ8886ZrVjPT4B8eB8tVXdsiWSmP8KybVEclEV7eRzg7SQKBgD6p3NmW5JUs/KdWaNZNRx3Sqj4vo0roRj5GljQfUxzVR5j4BKun19dDHcvLal6+3CAcc3LR9vOd3wWGEAvin4mRyMNCItOLHoHKundx1bJUEMGBvCvx7RslshVFpum/qekxBBdNRA1sF1hAvpdyZFh33J7SQ4E53jfSVo8CnBhRAoGAAT9HqzS2KiGuX6WI9GKogjm3y8Bl3jjKGMScFR40pjcvLLPVCwTM0cMaHOcLXO1C17NRzjJioFUcT3YuQ+SK9qQXvSujiq5Vx5LN/oafy2l8F66vV7KZpoBao63nrV/6gWHMYzUc9Vwi0lHXK1eb/D3ZF8WLe0rsebBO74o3f1w=";
    private final static String SERVICE_PUBLIC_KEY_NAME = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmP0ag59TmO3jiSN265UCOhVDc0UDFeoeTZevRIuft+hIUA7ZEPziYudyBvt51qFq5Gfp4KOVGscpAgcU/8IyNTCMk2t9NpOe710L+W9xRzTPZR9NKbAm7cOE6Wk0tjDstuyUXIke22gk0lBy1izlhPQw1uaByC7RMMKu3bRE9GzdDvNbdcAnKYTQ3nc68npejGP/SpQ18hsf0ELT51PBdF23B7m3W275J5tuag9Zbg6jzsIAx9dzgdNi1/xrq7Tae4yyBfB989UTt5wQbddOqieFaEAzGFn7K05EPnQDQJRojVb0RNDrhr/bMHz3hl7UwY6THFL2CpI/BBy5VD9HXQIDAQAB";


    /**
     * 生成公、私钥
     * 根据需要返回String或byte[]类型
     *
     * @return
     */
    public static ArrayList<String> createRSAKeys() {
        ArrayList<String> array = new ArrayList<>();
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            //获取公、私钥值
            String publicKeyValue = Base64.encodeBase64String(publicKey.getEncoded());
            String privateKeyValue = Base64.encodeBase64String(privateKey.getEncoded());

            //存入
            array.add(publicKeyValue);
            array.add(privateKeyValue);

            Log.e(" >>> ", publicKeyValue);
            Log.e(" >>> ", privateKeyValue);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return array;
    }


    //获取本地RSA公钥
    public static PublicKey getPublicKey() {
        try {
            return getPublicKey(PUBLIC_KEY_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取本地RSA公钥
    public static String getPublicKeyString() {
        try {
            return PUBLIC_KEY_NAME;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取服务器RSA公钥
    public static PublicKey getServicePublicKey() {
        try {
            return getPublicKey(SERVICE_PUBLIC_KEY_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //获取RSA公钥 根据钥匙字段
    public static PublicKey getPublicKey(String key) {
        try {
            byte[] byteKey = Base64.decodeBase64(key);
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            return keyFactory.generatePublic(x509EncodedKeySpec);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取RSA私钥   根据钥匙字段
    private static PrivateKey getPrivateKey(String key) {
        try {
            byte[] byteKey = Base64.decodeBase64(key);
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(byteKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    //本地RSA私钥 签名
    public static String sign(String requestData) {
        String signature = null;
        byte[] signed = null;
        try {
//            Log.e("=0== 签名前 >>>",requestData);
            PrivateKey privateKey = getPrivateKey(PRIVATE_KEY_NAME);
            Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            Sign.initSign(privateKey);
            Sign.update(requestData.getBytes());
            signed = Sign.sign();
            signature = Base64.encodeBase64String(signed);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return signature;
    }


    //公钥验证签名   base64签名 signature   签名内容requestData
    public static boolean verifySign(String requestData, String signature) {
        boolean verifySignSuccess = false;
        try {
            PublicKey publicKey = getServicePublicKey();
            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            verifySign.initVerify(publicKey);
            verifySign.update(requestData.getBytes());

            verifySignSuccess = verifySign.verify(Base64.decodeBase64(signature));
            System.out.println(" >>> " + verifySignSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return verifySignSuccess;
    }


    public static String encrypt(String clearText) {
        String encryptedBase64 = "";
        try {
            Key key = getServicePublicKey();
            final Cipher cipher = Cipher.getInstance(RSA_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //
            byte[] encryptedBytes = cipher.doFinal(clearText.getBytes("UTF-8"));
            encryptedBase64 = Base64.encodeBase64String(encryptedBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedBase64;
    }

    public static String decrypt(String encryptedBase64) {
        String decryptedString = "";
        try {
            Key key = getPrivateKey(PRIVATE_KEY_NAME);
            final Cipher cipher = Cipher.getInstance(RSA_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedBytes = Base64.decodeBase64(encryptedBase64);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            decryptedString = new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedString;
    }
}
