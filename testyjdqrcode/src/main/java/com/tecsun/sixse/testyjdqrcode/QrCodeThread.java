package com.tecsun.sixse.testyjdqrcode;

import static com.tecsun.network.network.BaseHelper.ThreadTransformer;

import android.text.TextUtils;


import java.util.concurrent.TimeUnit;

import rx.Observable;

public class QrCodeThread {
    public boolean isRead;
    public boolean isShow;
    public QrCodeUtil.QrCallBack qrCallBack;
    private ReadThread readThread;

    public static volatile QrCodeThread read;

    public static QrCodeThread getRead() {
        if (read == null) {
            synchronized (QrCodeThread.class) {
                if (read == null) {
                    read = new QrCodeThread();
                }
            }
        }
        return read;
    }

    public void stopRead() {
        isRead = false;
    }

    public void startRead() {
        Observable.just(0)
                .delay(200, TimeUnit.MILLISECONDS)
                .compose(ThreadTransformer())
                .subscribe(r -> isRead = true, Throwable::printStackTrace);
    }


    public void setDataCallback(QrCodeUtil.QrCallBack qrCallBack) {
        this.qrCallBack = qrCallBack;
    }

    public void start() {
        readThread = new ReadThread();
        readThread.run = true;
        readThread.start();
    }

    public void release() {
        if (readThread != null) {
            readThread.run = false;
            readThread = null;
        }
    }

    public void isShow(boolean isShow) {
        this.isShow = isShow;
    }

    private class ReadThread extends Thread {
        public boolean run;

        @Override
        public void run() {
            while (run) {
                try {
                    sleep(300);
                    if (isRead && isShow) {
                        String str = QrCodeUtil.getInstance().readQrCode();
                        if (TextUtils.isEmpty(str)) {
                            continue;
                        }
                        String data = str.replaceAll("\\n", "").replaceAll("\\r", "").trim();
                        qrCallBack.readOk(data);
                    }
                } catch (Exception e) {
//                    MyFileUtil.saveLog("读二维码错误：" + e.getMessage());
                }
            }
        }
    }
}
