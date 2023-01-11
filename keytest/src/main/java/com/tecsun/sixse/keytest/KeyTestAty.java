package com.tecsun.sixse.keytest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.seuic.irsnssvc.IInfraredTempCallback;
import com.seuic.irsnssvc.InfraredTemp;
import com.seuic.scankey.IKeyEventCallback;
import com.seuic.scankey.ScanKeyService;
import com.seuic.scanner.DecodeInfo;
import com.seuic.scanner.DecodeInfoCallBack;
import com.seuic.scanner.Scanner;
import com.seuic.scanner.ScannerFactory;

public class KeyTestAty extends AppCompatActivity {

    private InfraredTemp infraredTemp;
    private ScanKeyService scanKeyService;
    private Scanner scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTem();
        initKey();
        initQrCode();
    }

    private void initQrCode() {
        scanner = ScannerFactory.getScanner(this);
        scanner.open();
        scanner.setDecodeInfoCallBack(back);
    }

    private DecodeInfoCallBack back = decodeInfo -> {
        System.out.println("decodeInfo：" + decodeInfo.barcode);
        System.out.println("decodeInfo：" + decodeInfo.codetype);
        System.out.println("decodeInfo：" + decodeInfo.length);
    };

    private void initKey() {
        scanKeyService = ScanKeyService.getInstance();
        scanKeyService.registerCallback(keyEventCallback, "248,249,250");
    }

    private IKeyEventCallback keyEventCallback = new IKeyEventCallback.Stub() {
        @Override
        public void onKeyDown(int keyCode) {
            System.out.println("onKeyDown：" + keyCode);

            if (keyCode == 249) {
                infraredTemp.measure();
            } else {
                scanner.startScan();
            }
        }

        @Override
        public void onKeyUp(int keyCode) {
            System.out.println("onKeyUp：" + keyCode);
            if (keyCode != 249) {
                scanner.stopScan();
            }
        }
    };

    private void initTem() {
        infraredTemp = InfraredTemp.getInstance();
        infraredTemp.setMode(InfraredTemp.MODE_PERSON);
        infraredTemp.registerCallback(tempCallback);
    }

    private IInfraredTempCallback tempCallback = new IInfraredTempCallback.Stub() {
        @Override
        public void onSuccess(int i, float v, float v1) {
            System.out.println("onSuccess：=" + i + " v=" + v + " v1=" + v1);
        }

        @Override
        public void onFailed(int i, String s) {
            System.out.println("onFailed：=" + i + " s=" + s);
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        infraredTemp.unregisterCallback(tempCallback);
        scanKeyService.unregisterCallback(keyEventCallback);
        scanner.setDecodeInfoCallBack(null);
        scanner.close();
    }
}