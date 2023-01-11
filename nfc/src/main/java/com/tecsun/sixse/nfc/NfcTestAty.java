package com.tecsun.sixse.nfc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.eidlink.idocr.sdk.EidDeviceType;
import com.eidlink.idocr.sdk.EidLinkSE;
import com.eidlink.idocr.sdk.EidLinkSEFactory;
import com.eidlink.idocr.sdk.bean.EidlinkInitParams;
import com.eidlink.idocr.sdk.bean.EidlinkResult;
import com.eidlink.idocr.sdk.listener.OnGetResultListener;
import com.tecsun.network.aty.BaseAty;
import com.tecsun.sixse.dbnfc.MyNfcManager;
import com.tecsun.sixse.dbnfc.NfcListener;

public class NfcTestAty extends BaseAty {

    public static String appId = "15B1E02";
    public static String ip = "eidcloudread.eidlink.com";
    public static int envCode = 52302;
    public static int port = 9989;
    private TextView tvResult;
    public static EidLinkSE eid;
    private long starttime, endtime;
    private ImageView ivBmp;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvResult = findViewById(R.id.tvResult);
        ivBmp = findViewById(R.id.ivBmp);
        initDBNfc();
//        findViewById(R.id.initNfc).setOnClickListener(v -> initDBNfc());
    }

    private void initDBNfc() {
        eid = EidLinkSEFactory.getEidLinkSE(new EidlinkInitParams(this, appId, ip, port, envCode));
        if (eid != null) {
            eid.setGetDataFromSdk(true);//读取身份证信息
            eid.setDeviceType(EidDeviceType.IMEI);//启用IMIE
            eid.setReadPicture(true);//读取照片
            tvResult.setText("初始化成功,当前账号:" + appId);
        }
    }

    private NfcAdapter mNfcAdapter;
    private NfcListener mNfcListener = new NfcListener() {
        @Override
        public void onNfcEvent(Tag tag) {
            if (eid != null) {
                eid.readIDCard(tag, mResultListener);
            }
        }

        @Override
        public void onNfcError(boolean has) {
            if (has) {
                showAlertDialog();
            } else {
                tvResult.setText("设备不支持NFC");
            }
        }
    };

    private OnGetResultListener mResultListener = new OnGetResultListener() {

        @Override
        public void onSuccess(EidlinkResult result) {
            endtime = System.currentTimeMillis() - starttime;
            tvResult.setText("读卡成功  " + result.toString() + "\n耗时: " + endtime + "ms");
            Log.e("result", result.toString());
            if (result.getIdentity() != null && !TextUtils.isEmpty(result.getIdentity().getPicture())) {
//                Bitmap bt = IDCardPhoto.getIDCardPhoto(result.getIdentity().getPicture());
//                if (bt != null) {
//                    ivBmp.setImageBitmap(bt);
//                }
            }
        }

        @Override
        public void onFailed(int code, String msg) {
            tvResult.setText("读卡失败: " + code + "," + msg);
        }

        @Override
        public void onStart() {
            super.onStart();
            tvResult.setText("开始读卡");
            starttime = System.currentTimeMillis();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mNfcAdapter = MyNfcManager.enableReaderMode(this, mNfcListener);
    }

    public void showAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("当前NFC未启用，点击前往设置NFC页面!").setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                    }
                });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyNfcManager.disableReaderMode(this, mNfcAdapter);
    }
}