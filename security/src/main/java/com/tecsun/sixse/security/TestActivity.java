package com.tecsun.sixse.security;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tecsun.jni.SSCardBean;
import com.tecsun.jni.ZJReader;

public class TestActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private static UsbDevPermission usbPermission;
    private ReadThread readThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        usbPermission = new UsbDevPermission(this, isOpen -> {
            if (isOpen) {
                Toast.makeText(TestActivity.this, "设备初始化成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(TestActivity.this, "设备初始化失败", Toast.LENGTH_LONG).show();
            }
        });
        initView();
        Log.d("MainActivity", ZJReader.GetLibVer());
    }

    public void start() {
        readThread = new ReadThread();
        readThread.run = true;
        readThread.start();
    }

    public void release() {
        if (readThread != null) {
            readThread.run = false;
            readThread = null;
        }
        try {
            usbPermission.unUsbDevPermission();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void initView() {
        TextView txtView = findViewById(R.id.textView);
        ImageView imageView = findViewById(R.id.ivphoto);
        findViewById(R.id.init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
        Button btn01 = findViewById(R.id.button01);
        btn01.setOnClickListener(v -> {
            String strRcv;
            strRcv = ZJReader.GetLibVer();
            Log.d("MainActivity", strRcv);
            txtView.setText(strRcv);
        });

        Button btn02 = findViewById(R.id.button02);
        btn02.setOnClickListener(v -> {
            String strRcv;
            if (!usbPermission.IsOpenDev()) {
                strRcv = "初始化F4MDS读卡器失败";
                Log.d("MainActivity", strRcv);
                txtView.setText(strRcv);
                return;
            }
            SSCardBean sscardBean = new SSCardBean();
            sscardBean = ZJReader.iReadSSCard(4, sscardBean);
            if (sscardBean.getRetCode() < 0) {
                strRcv = "读社保卡失败！返回码：" + sscardBean.getRetCode() + "，错误信息：" + sscardBean.getErrMsg();
            } else {
                strRcv = "读社保卡成功！社会保障号：" + sscardBean.getCardId() + "\n" +
                        "姓名：" + sscardBean.getCardName() + "\n" +
                        "性别：" + sscardBean.getSex() + "\n";
            }
            Log.d("MainActivity", strRcv);
            txtView.setText(strRcv);


        });

    }


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
                try {
//                    sleep(300);
                    if (!usbPermission.IsOpenDev()) {
                        continue;
                    }
                    SSCardBean iRetInfo = new SSCardBean();
                    sleep(100);
                    iRetInfo = ZJReader.iReadSSCard(4, iRetInfo);
                    if (iRetInfo.getRetCode() < 0) {
                        continue;
                    }
                    String strRcv = "读社保卡成功！社会保障号：" + iRetInfo.getCardId() + "\n" +
                            "姓名：" + iRetInfo.getCardName() + "\n" +
                            "性别：" + iRetInfo.getSex() + "\n";
                    Log.d("MainActivity", strRcv);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}