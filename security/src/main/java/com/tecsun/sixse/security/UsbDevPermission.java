package com.tecsun.sixse.security;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.tecsun.jni.ZJReader;

import java.util.Iterator;

public class UsbDevPermission {

    private static String TAG = "[YFSignPad]UsbDevPermission";
    private static UsbManager mUsbManager;
    private static UsbDeviceConnection mDeviceConnection;
    private static PendingIntent mPermissionIntent;
    private static Context mContext;
    private static boolean isOpen = false;
    private static final int[] pid_vid = {0x000B, 0x000B};
    onDevOpenListener listener;
    // 注1：UsbManager.ACTION_USB_DEVICE_ATTACHED对应的广播在USB每次插入时都能监听到，所以用这个就可以监听USB插入。
    // 注2：UsbManager.ACTION_USB_DEVICE_DETACHED用来监听USB拔出广播。

    public UsbDevPermission(Context context, onDevOpenListener listener) {
        mContext = context;
        mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent("com.android.example.USB_PERMISSION"), 0);
        // 注册USB设备权限管理广播
        IntentFilter filter = new IntentFilter("com.android.example.USB_PERMISSION");
        filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        mContext.registerReceiver(mUsbReceiver, filter);

        this.listener = listener;
        initUsbDev();
    }

    public void unUsbDevPermission() {
        if (isOpen) {
            mDeviceConnection.close();
            ZJReader.iCloseReader();
            isOpen = false;
            Log.d(TAG, "-----------CloseDevice-----------");
        }
        if (mContext != null)
            mContext.unregisterReceiver(mUsbReceiver);
    }

    //USB授权
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            String action = intent.getAction();
            //USB设备插入
            if (action.equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
                Log.d(TAG, "-------USB_DEVICE_ATTACHED--------");
                if (device.getVendorId() == pid_vid[0] && device.getProductId() == pid_vid[1]) {
                    initUsbDev();
                }
            }
            //USB设备拔出
            else if (action.equals("android.hardware.usb.action.USB_DEVICE_DETACHED")) {
                Log.d(TAG, "-------USB_DEVICE_DETACHED--------");
                if (device.getVendorId() == pid_vid[0] && device.getProductId() == pid_vid[1]) {
                    isOpen = false;
                    listener.onListener(isOpen);
                }
            } else if (action.equals("com.android.example.USB_PERMISSION")) {
                synchronized (this) {
                    if ((intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) && (device != null)) {
                        Log.d(TAG, "permission request get");
                        isOpen = OpenDevice(device);
                        listener.onListener(isOpen);
                    } else {
                        Log.d(TAG, "permission denied for device ");
                    }
                }
            }
        }
    };

    public void initUsbDev() {
        UsbDevice device = null;
        mUsbManager = ((UsbManager) mContext.getSystemService(Context.USB_SERVICE));
        Iterator deviceIterator = mUsbManager.getDeviceList().values().iterator();
        while (deviceIterator.hasNext()) {
            device = (UsbDevice) deviceIterator.next();
            Log.d(TAG, "getUsbDevPermission VendorId=[" + device.getVendorId() + "]ProductId=" + device.getProductId());
            if (device.getVendorId() == pid_vid[0] && device.getProductId() == pid_vid[1]) {
                // 判断下设备权限，如果没有权限，则请求权限
                if (!mUsbManager.hasPermission(device)) {
                    Log.d(TAG, "getUsbDevPermission to requestPermission");
                    mUsbManager.requestPermission(device, mPermissionIntent);
                } else {
                    isOpen = OpenDevice(device);
                    listener.onListener(isOpen);
                }
                break;
            }
        }
    }

    public boolean OpenDevice(UsbDevice device) {
        UsbInterface usbInterface = null;
        for (int i = 0; i < device.getInterfaceCount(); ) {
            // 一般来说一个设备都是一个接口，你可以通过getInterfaceCount()查看接口的个数
            // 这个接口上有两个端点，分别对应OUT 和 IN
            Log.d(TAG, " OpenDevice getInterfaceCount=" + i);
            usbInterface = device.getInterface(i);
            break;
        }
        if (usbInterface == null) {
            Log.d(TAG, " OpenDevice usbInterface null");
            return false;
        }
        mDeviceConnection = mUsbManager.openDevice(device);
        if (mDeviceConnection == null) {
            Log.d(TAG, " OpenDevice mDeviceConnection null");
            return false;
        }
        if (mDeviceConnection.claimInterface(usbInterface, true)) {
            //用UsbDeviceConnection 与 UsbInterface 进行端点设置和通讯
            UsbEndpoint usbEpOut = usbInterface.getEndpoint(1);
            UsbEndpoint usbEpIn = usbInterface.getEndpoint(0);
            if (usbEpOut == null) {
                Log.d(TAG, " OpenDevice usbEpOut null");
                mDeviceConnection.close();
                return false;
            }
            if (usbEpIn == null) {
                Log.d(TAG, " OpenDevice usbEpIn null");
                mDeviceConnection.close();
                return false;
            }
            Log.d(TAG, " usbEpOut getAddress=[" + usbEpOut.getAddress() + "] usbEpIn getAddress=" + usbEpIn.getAddress());
            long iRet = ZJReader.iInitReader(mDeviceConnection, usbEpIn, usbEpOut);
            Log.d(TAG, " OpenDevice iInitSSAppF4MDS return:" + iRet);
            if (iRet == 0) {
                return true;
            } else {
                mDeviceConnection.close();
                return false;
            }
        } else {
            Log.d(TAG, " OpenDevice mDeviceConnection claimInterface fail");
            mDeviceConnection.close();
            return false;
        }
    }

    public boolean IsOpenDev() {
        return isOpen;
    }

    public interface onDevOpenListener {
        void onListener(boolean isOpen);
    }
}
