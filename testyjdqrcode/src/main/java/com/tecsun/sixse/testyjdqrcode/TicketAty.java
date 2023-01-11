package com.tecsun.sixse.testyjdqrcode;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tecsun.network.utils.LogUntil;

public class TicketAty extends AppCompatActivity {
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
        });
    }

    private void initDev() {
        TicketQrCodeUtil.getInstance().init("/dev/ttyUSB0", new TicketQrCodeUtil.InitCallBack() {
            @Override
            public void onSuccess() {
                TicketQrCodeUtil.getInstance().start();
                TicketQrCodeUtil.getInstance().startRead();

            }

            @Override
            public void onFailed(String msg) {
                LogUntil.e(msg);
            }
        }, qrCode -> {
            LogUntil.e("qrCode" + qrCode);
            runOnUiThread(() -> tvResult.setText(qrCode));

        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        LogUntil.e("getCharacters:" + event.getCharacters());
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TicketQrCodeUtil.getInstance().isShow(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TicketQrCodeUtil.getInstance().isShow(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TicketQrCodeUtil.getInstance().readQrCode();
    }

}