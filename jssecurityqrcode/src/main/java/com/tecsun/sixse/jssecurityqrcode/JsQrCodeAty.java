package com.tecsun.sixse.jssecurityqrcode;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class JsQrCodeAty extends AppCompatActivity {

    private TextView tvQrCode;
    private TextView tvQrCodeResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.js_qr_code_aty);
        initView();
    }

    private void initView() {
        tvQrCode = findViewById(R.id.tvQrCode);
        tvQrCodeResult = findViewById(R.id.tvQrCodeResult);
    }
}