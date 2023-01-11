package com.tecsun;

import android.app.Application;

import com.tecsun.network.network.FakeX509TrustManager;
import com.tecsun.network.network.RetrofitManager;
import com.tecsun.network.utils.LogUntil;
import com.tecsun.testread.BuildConfig;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;

public class MyApp extends Application {
    private static MyApp myApplication;
    public final String suffix = "/open-apis/device/machine/heartbeats,/open-apis/device/user_voucher/issue,/open-apis/device/file";

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        initBug();
        initNet();
    }

    private void initBug() {

    }

    public static MyApp getInstances() {
        return myApplication;
    }


    private void initNet() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new FakeX509TrustManager() {
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
            X509TrustManager trustManager = (X509TrustManager) trustAllCerts[0];
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            RetrofitManager.get()
                    .setFactory(sslSocketFactory)
                    .setVerifier((hostname, session) -> true)
                    .setTrustManager(trustManager)
                    .setCallBackInterceptor(() -> {
                        List<Interceptor> interceptors = new ArrayList<>();
//                        interceptors.add(new LoginOutInterceptor());
//                        interceptors.add(new AuthorizationInterceptor());
                        return interceptors;
                    })
                    .setDebug(BuildConfig.DEBUG)
                    .setSuffix(suffix)
                    .setHttpCallback((log, url, type) -> LogUntil.e("网络请求日志：" + log))
                    .initOkHttpClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
