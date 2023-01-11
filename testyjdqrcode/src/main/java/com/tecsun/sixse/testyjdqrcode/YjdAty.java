package com.tecsun.sixse.testyjdqrcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.tecsun.network.utils.LogUntil;

public class YjdAty extends AppCompatActivity {
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yjd_aty);
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
                Toast.makeText(YjdAty.this, "初始化成功", Toast.LENGTH_SHORT).show();
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
    public boolean dispatchKeyEvent(KeyEvent event) {
        LogUntil.e("getCharacters:" + event.getCharacters());
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QrCodeThread.getRead().stopRead();
        QrCodeUtil.getInstance().readQrCode();
    }

}