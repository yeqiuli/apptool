package com.tecsun.utils;

import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by hgy
 * on 11/25/20
 */
public class SSLUtil {

    public static volatile SSLUtil sslUtil;

    public static SSLUtil getInstance() {
        if (sslUtil == null) {
            synchronized (SSLUtil.class) {
                if (sslUtil == null) {
                    sslUtil = new SSLUtil();
                }
            }
        }
        return sslUtil;
    }

    public SSLSocketFactory getSSL() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public HostnameVerifier getHost() {
        return (hostname, session) -> true;
    }

    public X509TrustManager getX509TrustManager() {
        return (X509TrustManager) getTrustManager()[0];
    }

    public TrustManager[] getTrustManager() {
        return new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };
    }
}
