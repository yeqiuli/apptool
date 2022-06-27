package com.tecsun.sixse.security;

import android.device.IccManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.stetho.common.LogUtil;
import com.tecsun.jni.ZJReader;
import com.ubx.usdk.ssc.aidl.IReaderCallback;
import com.ubx.usdk.ssc.aidl.PersonnelInfor;
import com.ubx.usdk.ssc.aidl.USDKManager;

public class IActivity extends AppCompatActivity {
    private String TAG = "IActivity";
    private ReadThread readThread;
    private TextView textView;
    public boolean isRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i);
        bindService();
        initView();
        Log.d(TAG, ZJReader.GetLibVer());
    }

    /**
     * 绑定服务
     */
    private void bindService() {
        USDKManager.getInstance().getSSCReaderManager(IActivity.this, new USDKManager.InitListener() {
            @Override
            public void onStatus(USDKManager.STATUS status) {
                LogUtil.e("onStatus()  status == " + status.toString());
                if (status == USDKManager.STATUS.SUCCESS) {
                    Toast.makeText(IActivity.this, "服务绑定成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(IActivity.this, "服务绑定失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void start() {
        readThread = new ReadThread();
        readThread.run = true;
        readThread.start();
        isRead = true;
    }

    private void stop() {
        isRead = false;
    }

    private void startRead() {
        isRead = true;
    }

    public void release() {
        if (readThread != null) {
            readThread.run = false;
            readThread = null;
        }
        try {
            USDKManager.getInstance().release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initView() {
        textView = findViewById(R.id.textView);
        findViewById(R.id.init).setOnClickListener(view -> {//初始化
            start();
        });
        findViewById(R.id.button01).setOnClickListener(v -> {//开始读卡
            startRead();
        });
        findViewById(R.id.button02).setOnClickListener(v -> {//停止读卡
            stop();
        });
        findViewById(R.id.button03).setOnClickListener(v -> {//关闭读卡器
            release();
        });
    }


    private IReaderCallback mCallback = new IReaderCallback.Stub() {
        @Override
        public void result(PersonnelInfor persInfor) {
            isRead = false;
            String stringBuilder = "姓名:" + persInfor.getName() + "\n" +
                    "社保卡号码:" + persInfor.getSocialSecurityNumber() + "\n" +
                    "社保卡地区码:" + persInfor.getCityCode() + "\n" +
                    "卡号:" + persInfor.getCardNumber() + "\n" +
                    "发卡时间:" + persInfor.getIssuingDate() + "\n" +
                    "有效期:" + persInfor.getCardValid() + "\n" +
                    "机构编号:" + persInfor.getAgencyNumber() + "\n\n" +
                    "内部卡号:" + persInfor.getInCardNo();

            runOnUiThread(() -> textView.setText(stringBuilder));
            USDKManager.getInstance().close();
        }

        @Override
        public void readerErrCode(int code, String msg) {
            Log.v(TAG, "USDKManager.getInstance().readerErrCode()    code == " + code);
            runOnUiThread(() -> textView.setText("读取社保卡信息失败:" + msg + " " + code));
            USDKManager.getInstance().close();
        }

        @Override
        public void checkCard(boolean isPsam, int code, String msg) {
            Log.v(TAG, "USDKManager.getInstance().checkCard()    code == " + code);
            if (code == 200) {
                isRead = false;
                USDKManager.getInstance().readInfo();
                runOnUiThread(() -> textView.setText("检卡成功"));
            } else {
                USDKManager.getInstance().close();
                runOnUiThread(() -> textView.setText("检卡失败:" + msg + " " + code));
            }
            runOnUiThread(() -> textView.setText(""));
        }

    };


    @Override
    protected void onDestroy() {
        release();
        super.onDestroy();
    }

    private class ReadThread extends Thread {
        public boolean run;

        @Override
        public void run() {
            while (run) {
                if (isRead) {
                    try {
                        Thread.sleep(300);
                        IccManager iccManager = new IccManager();
                        int status = iccManager.open((byte) 0, (byte) 1, (byte) 1);
                        if (status != 0) {
                            continue;
                        }
                        Thread.sleep(300);
                        byte[] atr = new byte[64];
                        int retLen = iccManager.activate(atr);
                        if (retLen <= 0) {
                            continue;
                        }
                        Thread.sleep(100);
                        iccManager.close();
                        Thread.sleep(300);
                        USDKManager.getInstance().init(0, 2, mCallback);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}