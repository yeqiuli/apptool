package com.hgy.net;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class FakeX509TrustManager implements X509TrustManager {

    private static TrustManager[] trustManagers;
    private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isClientTrusted(X509Certificate[] chain) {
        return true;
    }

    public boolean isServerTrusted(X509Certificate[] chain) {
        return true;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return _AcceptedIssuers;
    }

    public static void allowAllSSL() {
        HttpsURLConnection.setDefaultHostnameVerifier((arg0, arg1) -> {
            // TODO Auto-generated method stub
            return true;
        });

        SSLContext context;
        if (trustManagers == null) {
            trustManagers = new TrustManager[]{new FakeX509TrustManager()};
        }
        try {
            context = SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
