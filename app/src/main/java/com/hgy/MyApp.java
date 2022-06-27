package com.hgy;


import android.app.Application;

import com.hgy.aty.BuildConfig;
import com.hgy.net.FakeX509TrustManager;
import com.hgy.tool.KeyboardVisibilityObserver;
import com.hgy.tool.SPUtil;
import com.tecsun.devgateway.net.TokenInterceptor;
import com.tecsun.network.network.FileHttpLoggingInterceptor;
import com.tecsun.network.network.InterceptorCallBack;
import com.tecsun.network.network.RetrofitManager;
import com.tencent.bugly.crashreport.CrashReport;

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

    @Override
    public void onCreate() {
        super.onCreate();
        SPUtil.initContext(this);
        myApplication = this;
        CrashReport.initCrashReport(getApplicationContext(), "9c7a12401e", true);//标准版日志收集key:518da22ab3
        KeyboardVisibilityObserver.getInstance().init(this);
        initNet();
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
                    .setHttpCallback(log -> {
                    })
                    .setCallBackInterceptor(() -> {
                        List<Interceptor> interceptors = new ArrayList<>();
                        interceptors.add(new TokenInterceptor());
                        return interceptors;
                    })
                    .setTrustManager(trustManager)
                    .setDebug(BuildConfig.DEBUG)
                    .initOkHttpClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
