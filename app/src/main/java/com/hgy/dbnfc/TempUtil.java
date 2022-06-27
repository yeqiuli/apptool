package com.hgy.dbnfc;

import com.tecsun.network.utils.LogUntil;

import java.io.InputStream;
import java.io.OutputStream;

import cn.pda.serialport.SerialPort;
import cn.pda.serialport.Tools;

public class TempUtil {
    public static volatile TempUtil tempUtil;
    public int port = 12;
    public int buad = 9600;
    public SerialPort mSerialPort;
    private InputStream is;
    private OutputStream os;
    private int count = 0;

    public static final String CMD_AA = "AA";//物温
    public static final String CMD_AB = "AB";//腕温
    public static final String CMD_AC = "AC";//额温
    public static final String CHECK_TEMP = "A9A235425558";//测温校准

    public static TempUtil getTempUtil() {
        if (tempUtil == null) {
            synchronized (TempUtil.class) {
                if (tempUtil == null) {
                    tempUtil = new TempUtil();
                }
            }
        }
        return tempUtil;
    }


    public float getTem(String cmdCode) {
        LogUntil.e("测温次数：" + count);
        try {
            byte[] cmd = Tools.HexString2Bytes(cmdCode);
            os.write(cmd);
            int size;
            int available;
            byte[] buffer = new byte[1024];
            if (is == null) {
                return 0;
            }
            Thread.sleep(300);
            available = is.available();
            if (available > 0) {
                size = is.read(buffer);
                if (size > 0) {
                    float tem = onDataReceived(buffer, size);
                    LogUntil.e("测量温度：" + tem);
                    if (tem <= 0) {
                        if (count < 4) {
                            count++;
                            return getTem(cmdCode);
                        } else {
                            count = 0;
                            return tem;
                        }
                    }
                    if (tem > 37.3) {
                        if (count < 4) {
                            count++;
                            return getTem(cmdCode);
                        } else {
                            count = 0;
                            return tem;
                        }
                    }
                    if (tem < 20) {
                        if (count < 4) {
                            count++;
                            return getTem(cmdCode);
                        } else {
                            count = 0;
                            return tem;
                        }
                    }
                    count = 0;
                    return tem;
                }
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            count = 0;
            return 0;
        }
    }

    /**
     * add recv data on UI
     *
     * @param buffer
     * @param size
     */
    private float onDataReceived(byte[] buffer, int size) {
        if (size > 20) {
            return 0;
        }
        String recv = new String(buffer, 0, size);
        if (recv.length() != 9) {
            return 0;
        }
        if (recv.contains("+")) {
            float a = Integer.valueOf(recv.replace("+", "").replace("\r\n", ""));
            return (a + 6) / 10;
        } else if (recv.contains("-")) {
            float a = Integer.valueOf(recv.replace("-", ""));
            return (a + 6) / 10;
        }
        return 0;
    }

    /**
     * Open serial port, and turn on the selected power
     */
    public boolean open() {
        try {
            mSerialPort = new SerialPort(port, buad, 0);
            mSerialPort.power3v3on();
            is = mSerialPort.getInputStream();
            os = mSerialPort.getOutputStream();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Close serial port, and turn off the selected power
     */
    public void close() {
        try {
            if (mSerialPort != null) {
                is.close();
                os.close();
                mSerialPort.power_3v3off();
                mSerialPort.close(port);
                mSerialPort = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
