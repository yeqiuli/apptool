package com.hgy.aty;

import android.content.Intent;
import android.os.Bundle;

import com.hgy.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        findViewById(R.id.tv1).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HkDataSynAty.class)));
        findViewById(R.id.tv2).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, JsonTestAty.class)));
        findViewById(R.id.tv3).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TestGatewayAty.class)));
        findViewById(R.id.tv4).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TestSkmAty.class)));
        findViewById(R.id.tv5).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TestNfcReadAty.class)));
        findViewById(R.id.tv6).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TestQrCodeAty.class)));
        findViewById(R.id.tv7).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TestDBNfcAty.class)));
        findViewById(R.id.tv8).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, KeyTestActivity.class)));
        findViewById(R.id.tv9).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TestYctAty.class)));
        findViewById(R.id.tv10).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, TestTabAty.class)));
    }

    @Override
    public void backAty() {

    }

    @Override
    public void gotoCheckIn() {

    }
}