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
    }

    @Override
    public void backAty() {

    }

    @Override
    public void gotoCheckIn() {

    }
}