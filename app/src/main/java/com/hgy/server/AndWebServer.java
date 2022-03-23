package com.hgy.server;

import static com.tecsun.network.network.BaseHelper.ThreadTransformer;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.tecsun.network.utils.AppUtils;
import com.tecsun.network.utils.LogUntil;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * andserver服务
 */
public class AndWebServer extends Service {
    private Server mServer;
    private NetReceiver receiver;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initReceiver();
    }


    private void initServer() {
        if (mServer != null && mServer.isRunning()) {
            return;
        }
        try {
            mServer = AndServer.webServer(this)
                    .inetAddress(AppUtils.getInstance().getLocalIPAddress())
//                    .inetAddress( InetAddress.getByName("172.0.0.1"))
                    .port(8868)
                    .timeout(10, TimeUnit.SECONDS)
                    .listener(new Server.ServerListener() {
                        @Override
                        public void onStarted() {
                            LogUntil.e("AndServer start");
                        }

                        @Override
                        public void onStopped() {
    //                        resetNotify("服务已停止");
                            LogUntil.e("AndServer stop");
    //                        FileUtils.saveLog("AndServer stop");
    //                        retryServer(50);
                        }

                        @Override
                        public void onException(Exception e) {
    //                        FileUtils.saveLog(e.getMessage());
    //                        resetNotify("服务已停止：" + e.getMessage());
                            LogUntil.e("AndServer exception:" + e.getMessage());
                            retryServer(60);
                        }
                    })
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        startServer();
    }

    /**
     * Start server.
     */
    private void startServer() {
        if (!mServer.isRunning()) {
            mServer.startup();
        }
    }

    /**
     * Stop server.
     */
    private void stopServer() {
        if (mServer != null) {
            mServer.shutdown();
        }
    }

    /**
     * 设置网络监听
     */
    private void initReceiver() {
        receiver = new NetReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
    }


    /**
     * 服务重试
     */

    private void retryServer(int time) {
        stopServer();
        Observable.just(0)
                .delay(time, TimeUnit.SECONDS)
                .compose(ThreadTransformer())
                .subscribe(r -> initServer(), Throwable::printStackTrace);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public class NetReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
//                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    if (NetworkInfo.State.CONNECTED == info.getState()) {
                        initServer();
                    } else {
                        stopServer();
                    }
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        stopServer();
        super.onDestroy();
    }


}
