package com.tecsun.sixse.testyjdqrcode;

import android.text.TextUtils;

import com.tecsun.network.utils.LogUntil;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 扫码工具类
 */
public class QrCodeUtil {
    public static volatile QrCodeUtil util;
    private SerialPort mSerialPort;
    private OutputStream outputStream;
    public boolean isInit;

    public static QrCodeUtil getInstance() {
        if (util == null) {
            synchronized (QrCodeUtil.class) {
                if (util == null) {
                    util = new QrCodeUtil();
                }
            }
        }
        return util;
    }

    public void init(InitCallBack initCallBack, String port) {
        SerialPortFinder finder = new SerialPortFinder();
        String[] ports = finder.getAllDevicesPath();
        List<String> list = Arrays.asList(ports);
        if (!list.contains(port)) {
            initCallBack.onFailed("选择的串口号不存在，请检查基本设置");
            isInit = false;
            return;
        }
        if (isInit) {
            initCallBack.onSuccess();
            return;
        }
        Observable.just(port)
                .map(r -> {
                    try {
                        mSerialPort = new SerialPort(new File(r), 9600, 8, 0, 0, 0, 0);
                        outputStream = mSerialPort.getOutputStream();
                        return 0;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return -1;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(r -> {
                    if (r == 0) {
                        isInit = true;
                        initCallBack.onSuccess();
                    } else {
                        isInit = false;
                        initCallBack.onFailed("串口打开失败，无法扫码");
                    }
                }, e -> {
                    isInit = false;
                    e.printStackTrace();
                    LogUntil.e("错误：" + e.getMessage());
                    initCallBack.onFailed("串口打开失败");
                });

    }

    /**
     * 读取数据
     */
    public String readQrCode() {
        if (!isInit) {
            init(new InitCallBack() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailed(String msg) {

                }
            }, "/dev/ttyS1");
        }
        try {
            InputStream inputStream = mSerialPort.getInputStream();
            byte[] readBuffer = new byte[inputStream.available()];
            int size = inputStream.read(readBuffer);
            if (size > 0) {
                String str = byteToStr(readBuffer, readBuffer.length);
                LogUntil.e("原始数据：" + str);
                String dataHex = hexStringToString(str);
                LogUntil.e("原始数据" + dataHex);
                if (!TextUtils.isEmpty(dataHex)) {
                    return dataHex;
                }
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 16进制转换成为string类型字符串
     *
     * @param s
     * @return
     */
    public static String hexStringToString(String s) {
        if (s == null || s.equals("")) {
            return null;
        }
        s = s.replace(" ", "");
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "UTF-8");
            new String();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }


    /**
     * 接收到的字节数组转换16进制字符串
     */
    public static String byteToStr(byte[] b, int size) {
        String ret = "";
        for (int i = 0; i < size; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }


    public void close() {
        isInit = false;
        try {
            mSerialPort.close();
            mSerialPort = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface InitCallBack {
        void onSuccess();

        void onFailed(String msg);
    }

    public interface QrCallBack {

        void readOk(String qrCode);

        void err();
    }
}
