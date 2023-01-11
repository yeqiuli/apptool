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
public class TicketQrCodeUtil {
    public static volatile TicketQrCodeUtil util;
    private SerialPort mSerialPort;
    private OutputStream outputStream;
    public boolean isRead;
    public boolean isShow;
    private QrCodeCallback qrCodeCallback;
    private ReadThread readThread;

    public static TicketQrCodeUtil getInstance() {
        if (util == null) {
            synchronized (TicketQrCodeUtil.class) {
                if (util == null) {
                    util = new TicketQrCodeUtil();
                }
            }
        }
        return util;
    }

    public void init(String port, InitCallBack initCallBack, QrCodeCallback qrCodeCallback) {
        this.qrCodeCallback = qrCodeCallback;
        SerialPortFinder finder = new SerialPortFinder();
        String[] ports = finder.getAllDevicesPath();
        List<String> list = Arrays.asList(ports);
        if (!list.contains(port)) {
            initCallBack.onFailed("选择的串口号不存在，请检查基本设置");
            return;
        }
        Observable.just(port)
                .map(r -> {
                    try {
                        mSerialPort = new SerialPort(new File(r), 19200, 8, 0, 0, 0, 0);
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
                        initCallBack.onSuccess();
                    } else {
                        initCallBack.onFailed("串口打开失败，无法扫码");
                    }
                }, e -> {
                    e.printStackTrace();
                    LogUntil.e("错误：" + e.getMessage());
                    initCallBack.onFailed("串口打开失败");
                });

    }

    public void start() {
        readThread = new ReadThread();
        readThread.run = true;
        readThread.start();
    }

    public void startRead() {
        isRead = true;

    }

    public void stopRead() {
        isRead = false;
    }

    public void isShow(boolean isShow) {
        this.isShow = isShow;
    }

    public byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private byte toByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    private class ReadThread extends Thread {
        public boolean run;

        @Override
        public void run() {
            while (run) {
                try {
                    sleep(00);
                    if (isRead && isShow) {
                        String qrCode = readQrCode();
                        if (TextUtils.isEmpty(qrCode)) {
                            continue;
                        }
                        qrCodeCallback.onSuccess(qrCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取数据
     */
    public String readQrCode() {
        try {
            String sHex = "FF540D";
            byte[] data = hexStringToByte(sHex);
            outputStream.write(data);
            Thread.sleep(100);
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
        isRead = false;
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

}
