package com.hgy.aty;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hgy.bean.SkmBean;
import com.ivsign.android.IDCReader.IdentityCard;
import com.tecsun.network.aty.BaseAty;
import com.tecsun.network.network.CallBack;
import com.tecsun.network.network.HttpExceptionBean;
import com.tecsun.network.network.RetrofitManager;
import com.tecsun.network.utils.LogUntil;
import com.yishu.YSBluetoothCardReader.BluetoothReader;
import com.yishu.YSNfcCardReader.NfcCardReader;
import com.yishu.YSOtgCardReader.OTGCardReader;
import com.yishu.util.ByteUtil;
import com.yishu.util.SPUtil;

import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class TestNfcReadAty extends BaseAty {

    private NfcCardReader nfcCardReaderAPI;
    private BluetoothReader bluetoothReaderAPI;
    private static OTGCardReader otgCardReaderAPI;
    private static Intent thisIntent;
    private TextView tvResult;
    private Handler mHandler;

    @Override
    protected int getLayout() {
        return R.layout.aty_nfc_read;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvResult = findViewById(R.id.tvResult);
        mHandler = new MyHandler();
        nfcCardReaderAPI = new NfcCardReader(mHandler, this);
        bluetoothReaderAPI = new BluetoothReader(this, mHandler);
        otgCardReaderAPI = new OTGCardReader(mHandler, this);
        otgCardReaderAPI.setTheServer("103.21.119.78", 17278);
        findViewById(R.id.initNfc).setOnClickListener(v -> initDev());
        mHandler.sendEmptyMessage(ByteUtil.MESSAGE_VALID_NFCSTART);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcCardReaderAPI.startNFCListener();
        Intent intent = getIntent();
        String action = intent.getAction();
        if ("android.nfc.action.TECH_DISCOVERED".equals(action)) {
            if (thisIntent == null) {
                mHandler.sendEmptyMessage(ByteUtil.MESSAGE_VALID_NFCSTART);
                thisIntent = intent;
                mHandler.sendEmptyMessage(ByteUtil.MESSAGE_VALID_NFCBUTTON);
            }
        }
        Toast.makeText(TestNfcReadAty.this, "初始化", Toast.LENGTH_SHORT).show();
    }

    private void initDev() {

    }

    private class MyHandler extends Handler {
        //蓝牙nfc初始化之后不再初始化
        private boolean nfcInit = false;
        private boolean btInit = false;


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int org = msg.what;
            LogUntil.e("org:" + org);
            switch (org) {
                case ByteUtil.MESSAGE_VALID_NFCSTART:
                    Log.i("Info", "enter MESSAGE_VALID_NFCSTART");
                    if (nfcInit) {
                        break;
                    }
                    boolean enabledNFC = nfcCardReaderAPI.enabledNFCMessage();
                    if (enabledNFC) {
                        nfcInit = true;
                    } else {
                        Toast.makeText(TestNfcReadAty.this, "NFC初始化失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ByteUtil.MESSAGE_VALID_BTSTART:
                    Log.i("Info", "enter MESSAGE_VALID_BTSTART");
                    if (btInit) {
                        break;
                    }
                    if (bluetoothReaderAPI.checkBltDevice()) {
                        btInit = true;
                    } else {
                        Toast.makeText(TestNfcReadAty.this, "当前设备无蓝牙或者蓝牙未开启", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ByteUtil.BLUETOOTH_CONNECTION_SUCCESS:
                    Log.i("Info", "enter BLUETOOTH_CONNECTION_SUCCESS");
                    BluetoothDevice device = (BluetoothDevice) msg.obj;
                    new SPUtil(TestNfcReadAty.this).putConnBtDevice(device.getAddress());
                    break;
                case ByteUtil.BLUETOOTH_CONNECTION_FAILED:
                    Log.i("Info", "enter BLUETOOTH_CONNECTION_FAILED");
                    Toast.makeText(TestNfcReadAty.this, "设备连接失败，请重新连接", Toast.LENGTH_SHORT).show();
                    break;
                case ByteUtil.MESSAGE_VALID_NFCBUTTON:
                    Log.i("Info", "enter MESSAGE_VALID_NFCBUTTON");
                    boolean isNFC = nfcCardReaderAPI.isNFC(thisIntent);
                    if (isNFC) {
                        nfcCardReaderAPI.CreateCard(thisIntent);
                    } else {
                        Toast.makeText(TestNfcReadAty.this, "获取nfc失败", Toast.LENGTH_SHORT).show();
                    }
                    thisIntent = null;
                    break;
                case ByteUtil.MESSAGE_VALID_BTBUTTON:
                    Log.i("Info", "enter MESSAGE_VALID_BTBUTTON");
                    break;
                case ByteUtil.READ_CARD_START:
                    Log.i("Info", "enter READ_CARD_START");
                    break;
                case ByteUtil.READ_CARD_FAILED:
                    Log.i("Info", "enter READ_CARD_FAILED");
                    if (78 != nfcCardReaderAPI.getErrorFlag()) {
                        String message = nfcCardReaderAPI.getMessage();
                        if (!("".equals(message))) {
                            Toast.makeText(TestNfcReadAty.this, message, Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    Toast.makeText(TestNfcReadAty.this, "读卡失败:" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case ByteUtil.READ_CARD_SUCCESS:
                    Log.i("Info", "enter READ_CARD_SUCCESS");
                    IdentityCard card = (IdentityCard) msg.obj;
                    if (card != null) {
                        String name = card.getNameText();
                        String sex = card.getSexText();
                        String birthday = card.getBirthdayText();
                        String nation = card.getMingZuText();
                        String address = card.getAddressText();
                        String number = card.getNumberText();
                        String qianfa = card.getQianfaText();
                        String effdate = card.getEffectiveDate();
                        Bitmap head = card.getImage();
                        if (head == null) {
                            Toast.makeText(TestNfcReadAty.this, "头像读取失败", Toast.LENGTH_SHORT).show();
                        }
                        String idInfo = "Info:" + name + "\n" + sex + "\n" + birthday + "\n" + nation + "\n" + address + "\n" + number + "\n" + qianfa + "\n" + effdate + "\n";
                        tvResult.setText(idInfo);
                        LogUntil.e(idInfo);
                    }
                    break;
                case ByteUtil.MESSAGE_VALID_OTGSTART:
                    Log.i("Info", "enter MESSAGE_VALID_OTGSTART");
                    bluetoothReaderAPI.closeBlueTooth();
                    break;
                case ByteUtil.BTREAD_BUTTON_ENABLED:
                    Log.i("Info", "enter BTREAD_BUTTON_ENABLED");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("onPause", "@@ enter onPause");
        if (isFinishing()) {
            bluetoothReaderAPI.closeBlueTooth();
            if (nfcCardReaderAPI != null) {
                nfcCardReaderAPI.endNFCListener();
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i("Info", "enter onNewIntent");
        super.onNewIntent(intent);
        thisIntent = intent;
        mHandler.sendEmptyMessage(ByteUtil.MESSAGE_VALID_NFCBUTTON);
    }

}
