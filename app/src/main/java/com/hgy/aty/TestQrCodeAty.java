package com.hgy.aty;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hgy.bean.SkmBean;
import com.hgy.read.QrCodeThread;
import com.hgy.read.QrCodeUtil;
import com.tecsun.network.aty.BaseAty;
import com.tecsun.network.network.CallBack;
import com.tecsun.network.network.HttpExceptionBean;
import com.tecsun.network.network.RetrofitManager;
import com.tecsun.network.utils.LogUntil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class TestQrCodeAty extends BaseAty {


    private TextView tvResult;

    @Override
    protected int getLayout() {
        return R.layout.aty_qr_code;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        tvResult = findViewById(R.id.tvResult);

        findViewById(R.id.tvInitQrCode).setOnClickListener(v -> {
            initDev();
        });
        findViewById(R.id.tvStartRead).setOnClickListener(v -> {
            QrCodeThread.getRead().startRead();
            QrCodeThread.getRead().start();
            QrCodeThread.getRead().isShow(true);
        });
    }

    private void initDev() {
        QrCodeUtil.getInstance().init(new QrCodeUtil.InitCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(TestQrCodeAty.this, "初始化成功", Toast.LENGTH_SHORT).show();
                LogUntil.e("onSuccess");
                QrCodeThread.getRead().setDataCallback(new QrCodeUtil.QrCallBack() {
                    @Override
                    public void readOk(String qrCode) {
                        LogUntil.e(qrCode);
                        runOnUiThread(() -> tvResult.setText(qrCode));
                    }

                    @Override
                    public void err() {

                    }
                });

            }

            @Override
            public void onFailed(String msg) {
                LogUntil.e(msg);
            }
        }, "/dev/ttyS1");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QrCodeThread.getRead().stopRead();
        QrCodeUtil.getInstance().readQrCode();
    }
}
