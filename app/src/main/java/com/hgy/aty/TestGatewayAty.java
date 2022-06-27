package com.hgy.aty;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.tecsun.devgateway.CheckDevAty;
import com.tecsun.devgateway.bean.AiTokenBean;
import com.tecsun.devgateway.net.AiPlatformUtil;
import com.tecsun.network.aty.BaseAty;
import com.tecsun.network.network.CallBack;
import com.tecsun.network.network.HttpExceptionBean;
import com.tecsun.network.utils.LogUntil;

public class TestGatewayAty extends BaseAty {

    @Override
    protected int getLayout() {
        return R.layout.aty_gateway;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        findViewById(R.id.tvTest).setOnClickListener(v -> {
            Intent intent = new Intent(TestGatewayAty.this, CaptureActivity.class);

            intent.putExtra(CheckDevAty.CAMERA_ID, 1);
            intent.putExtra(CheckDevAty.CAMERA_ROTE, 0);

            startActivityForResult(intent, 5);
        });
        initGateway(false);
    }

    private void initGateway(boolean flag) {
        AiPlatformUtil.getInstance().setDefaultPath("https://dev.d-cos.com").setDevID("121414112412");
        if (flag) {
            AiPlatformUtil.getInstance()
                    .setSecret("39a27104ac3d9ca9d9f2a56cb45604800cd3d139")
                    .setDevServerID("7634581415194624")
                    .accessToken(new CallBack<AiTokenBean>() {
                        @Override
                        public void onSuccess(AiTokenBean aiTokenBean) {
                            //继续下一步流程
                        }

                        @Override
                        public void onFailure(HttpExceptionBean exceptionBean) {
                            //登录失败
                            Toast.makeText(TestGatewayAty.this, "授权失败" + exceptionBean.getErrorBody(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Intent intent = new Intent(TestGatewayAty.this, CheckDevAty.class);
            intent.putExtra(CheckDevAty.CAMERA_ID, 0);
            intent.putExtra(CheckDevAty.CAMERA_ROTE, 0);
            startActivityForResult(intent, 5);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUntil.e("requestCode:" + requestCode);
        LogUntil.e("resultCode:" + resultCode);
        if (requestCode != 5) {
            Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "用户取消操作", Toast.LENGTH_SHORT).show();
            return;
        }
        if (resultCode == RESULT_OK) {
            String areaAddress = data.getStringExtra(CheckDevAty.AREA_ADDRESS);
            String areaName = data.getStringExtra(CheckDevAty.AREA_NAME);
            String devServerId = data.getStringExtra(CheckDevAty.DEV_SERVER_ID);
            String deviceName = data.getStringExtra(CheckDevAty.DEVICE_NAME);
            String secret = data.getStringExtra(CheckDevAty.SECRET);
            String tenantName = data.getStringExtra(CheckDevAty.TENANT_NAME);
            LogUntil.e(areaAddress);
            LogUntil.e(areaName);
            LogUntil.e(devServerId);
            LogUntil.e(deviceName);
            LogUntil.e(secret);
            LogUntil.e(tenantName);
            Toast.makeText(this, "授权成功：" + areaAddress, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
        }

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        LogUntil.e("requestCode:" + requestCode);
//        LogUntil.e("resultCode:" + resultCode);
//        if (requestCode != 5) {
//            showMsg("扫码出错!");
//            return;
//        }
//        if (resultCode == RESULT_CANCELED) {
//            showMsg("用户取消操作!");
//            return;
//        }
//        if (resultCode == 12) {
//            showMsg("扫码失败!");
//            return;
//        }
//        if (resultCode == RESULT_OK) {
//            LogUntil.e(data.getExtras().getString("data"));
//        } else {
//            showMsg("扫码失败!");
//        }
//
//    }

}
