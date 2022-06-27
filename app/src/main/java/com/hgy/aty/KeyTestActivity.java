package com.hgy.aty;

import static com.tecsun.network.network.BaseHelper.ThreadTransformer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hgy.base.BaseActivity;
import com.hgy.bean.sqlite.OrganizationBean;
import com.hgy.dbnfc.TempUtil;
import com.hgy.sqlite.RoomDataUtils;
import com.hgy.tool.ScanUtil;
import com.hgy.view.BToast;
import com.tecsun.network.utils.LogUntil;

import rx.Observable;


public class KeyTestActivity extends BaseActivity {


    private TextView tvMsg;
    private TextView tvInitTem;
    private TextView tvTest;
    private final String qrCodeAction = "com.rfid.SCAN";
    private final String keyAction = "android.intent.action.FUN_KEY";
    private final String rfidAction = "android.rfid.FUN_KEY";

    private KeyReceiver keyReceiver;
    private ScanUtil scanUtil;

    @Override
    public int getLayout() {
        return R.layout.activity_keytest;
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        initView();
        registerReceiver();
    }

    @Override
    public void backAty() {

    }

    @Override
    public void gotoCheckIn() {

    }

    private void registerReceiver() {
        keyReceiver = new KeyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(qrCodeAction);
        filter.addAction(rfidAction);
        filter.addAction(keyAction);
        registerReceiver(keyReceiver, filter);
        scanUtil = new ScanUtil(this);
        scanUtil.setScanMode(0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(keyReceiver);
    }

    public void initView() {

        tvMsg = findViewById(R.id.tvMsg);
        tvInitTem = findViewById(R.id.tvInitTem);
        tvTest = findViewById(R.id.tvTest);
        tvInitTem.setOnClickListener(v -> {
            boolean flag = TempUtil.getTempUtil().open();
            LogUntil.e("flag:" + flag);
        });
        tvTest.setOnClickListener(v -> getTemp());
    }

    private void getTemp() {
//        String cmdCode = "AA";//物温
//        String cmdCode = "AB";//额温
        String cmdCode = "AC";//腕温
        Observable.just(cmdCode)
                .map(r -> TempUtil.getTempUtil().getTem(r))
                .compose(ThreadTransformer())
                .subscribe(r -> {
                    String msg = "测温成功" + r;
                    tvMsg.setText(msg);
                    showToast(msg, BToast.ICONTYPE_SUCCEED);
                }, e -> {
                    hideDialog();
                    e.printStackTrace();
                });
    }


    private class KeyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(qrCodeAction)) {
                byte[] data = intent.getByteArrayExtra("data");
                if (data != null) {
                    String barcode = new String(data);
                    LogUntil.e(barcode);
                    tvMsg.setText(barcode);
                    return;
                }
            }
            int keyCode = intent.getIntExtra("keyCode", 0);
            if (keyCode == 0) {
                keyCode = intent.getIntExtra("keycode", 0);
            }
            LogUntil.e("KeyReceiver, keyCode = " + keyCode);
            boolean keyDown = intent.getBooleanExtra("keydown", false);
            if (!keyDown) {
                if (keyCode == 136) {//测温
                    getTemp();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
