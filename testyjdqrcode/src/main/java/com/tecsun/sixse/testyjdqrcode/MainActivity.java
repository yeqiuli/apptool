package com.tecsun.sixse.testyjdqrcode;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ScansManager;
import android.app.ThermometryManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MainActivity extends Activity implements View.OnClickListener, ThermometryManager.ThermometryCallBack {

    private ScansManager mScansManager = null;
    private ThermometryManager mThermometryManager = null;

    private Button allowscan_button = null;
    private Button stopsystemscan_button = null;

    private Button allowe_dropdown_button = null;
    private Button notallowe_dropdown_button = null;

    private Button ctpkey_open_button = null;
    private Button ctpkey_close_button = null;

    private Button softscan_button = null;
    private Button takeir = null;

    private Button open_scankey_report = null;
    private Button close_scankey_report = null;


    private void initView(){
        allowscan_button = findViewById(R.id.allowscan_button);
        allowscan_button.setOnClickListener(this);
        stopsystemscan_button = findViewById(R.id.stopsystemscan_button);
        stopsystemscan_button.setOnClickListener(this);
        allowe_dropdown_button = findViewById(R.id.allowe_dropdown_button);
        allowe_dropdown_button.setOnClickListener(this);
        notallowe_dropdown_button = findViewById(R.id.notallowe_dropdown_button);
        notallowe_dropdown_button.setOnClickListener(this);

        ctpkey_close_button = findViewById(R.id.ctpkey_close_button);
        ctpkey_close_button.setOnClickListener(this);
        ctpkey_open_button = findViewById(R.id.ctpkey_open_button);
        ctpkey_open_button.setOnClickListener(this);

        softscan_button = findViewById(R.id.softscan_button);
        softscan_button.setOnClickListener(this);
        takeir = findViewById(R.id.takeir);
        takeir.setOnClickListener(this);

        open_scankey_report = findViewById(R.id.open_scankey_report);
        open_scankey_report.setOnClickListener(this);
        close_scankey_report = findViewById(R.id.close_scankey_report);
        close_scankey_report.setOnClickListener(this);
    }

    @SuppressLint("WrongConstan")
    private void initdata() {
        mScansManager  = (ScansManager) getSystemService("scans");

        mScansManager.setInputMode(0);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initdata();
        initView();
        //mThermometryManager = new ThermometryManager(getApplicationContext());//去掉这里的创建
    }
    private boolean iskeyup = true;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(!iskeyup)
            return true;

       Toast.makeText(getApplicationContext(),"keycode:"+keyCode,Toast.LENGTH_SHORT).show();
        iskeyup = false;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        iskeyup = true;
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onResume() {
        if(mThermometryManager == null) {
            mThermometryManager = new ThermometryManager(getApplicationContext());
            mThermometryManager.setThermometryListenner(this);
        }

        initBroadcast();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(mThermometryManager != null) {
            mThermometryManager.setThermometryListenner(null);
            mThermometryManager = null;//设置回调为null之后需要释放对象，否则会引起下次拿不到温度
        }

        uninitBroadcast();
        super.onPause();
    }
    private final static int HOLD_TIMEOUT = 1;
    private boolean softtriger = false;
    Handler scanholdheander = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {

            int what = msg.what;
            switch (what){
                case HOLD_TIMEOUT:
                    mScansManager.stopScan();
                    softtriger =false;
                    break;
            }

            super.handleMessage(msg);
        }
    };
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.allowscan_button:
                mScansManager.setSystemTrigerScan(true);
                break;
            case R.id.stopsystemscan_button:
                mScansManager.setSystemTrigerScan(false);
                break;
            case R.id.notallowe_dropdown_button:
                mScansManager.setAllowStatusDrop(true);
                break;
            case R.id.allowe_dropdown_button:
                mScansManager.setAllowStatusDrop(false);
                break;
            case R.id.ctpkey_close_button:
                mScansManager.setCtpKeypadSwitch(false);
                break;
            case R.id.ctpkey_open_button:
                mScansManager.setCtpKeypadSwitch(true);
                break;
            case R.id.softscan_button:
                if(softtriger)
                    break;
                softtriger = true;
                mScansManager.startScan();
                scanholdheander.sendEmptyMessageDelayed(HOLD_TIMEOUT,30000);
                break;
            case R.id.takeir:
                mThermometryManager.TakeTemperature();
                break;
            case R.id.open_scankey_report:
                mScansManager.setReportScanKey(true);//设置扫码按键值上报，建议监听之前先设置上报按键值
                break;
            case R.id.close_scankey_report:
                mScansManager.setReportScanKey(false);//关闭扫码按键值上报
                break;
        }
    }

    @Override
    public void onTestCompelete(ThermometryManager.TemperatureMessage temperatureMessage) {
        Toast.makeText(getApplicationContext(),"温度"+temperatureMessage.BodyTemperature+"℃",Toast.LENGTH_SHORT).show();
    }

    private MyReceiver broadcastReceiver = null;

    private void initBroadcast(){
        if(broadcastReceiver == null){
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.scanservice.action.UPLOAD_BARCODE_DATA");
            broadcastReceiver = new MyReceiver();
            getApplicationContext().registerReceiver(broadcastReceiver, filter);
        }
    }
    private void uninitBroadcast(){
        if(broadcastReceiver != null){
            getApplicationContext().unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("android.scanservice.action.UPLOAD_BARCODE_DATA")){
                Toast.makeText(getApplicationContext(),intent.getStringExtra("barcode"),Toast.LENGTH_SHORT).show();
                if(softtriger) {
                    scanholdheander.removeMessages(HOLD_TIMEOUT);
                    mScansManager.stopScan();
                    softtriger = false;
                }
            }
        }
    }
}