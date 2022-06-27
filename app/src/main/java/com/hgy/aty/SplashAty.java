package com.hgy.aty;

import static com.tecsun.network.network.BaseHelper.ThreadTransformer;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.hgy.MyApp;
import com.hgy.base.BaseActivity;
import com.hgy.server.AndWebServer;
import com.hgy.tool.MyFileUtil;
import com.hgy.tool.PermissionTool;
import com.hgy.view.BToast;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

import rx.Observable;

public class SplashAty extends BaseActivity {
    private boolean isCheck = false;
    private final String[] list = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    @Override
    public int getLayout() {
        return R.layout.aty_sp;
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        if (!isCheck) {
            isCheck = true;
            if (PermissionTool.checkPermission(this, list)) {
                checkFilePermissions();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isCheck) {
            isCheck = true;
            if (PermissionTool.checkPermission(this, list)) {
                checkFilePermissions();
            }
        }
    }

    @Override
    public void backAty() {

    }

    @Override
    public void gotoCheckIn() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionTool.requestCheckPermissionResult(SplashAty.this, requestCode,
                permissions, grantResults, list, new PermissionTool.PermissionResult() {
                    @Override
                    public void isStartActivity(boolean start) {
                        isCheck = start;
                    }

                    @Override
                    public void getAllPermissionSuccess(boolean success) {
                        if (success) {
                            checkFilePermissions();
                        }
                    }
                });
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void checkFilePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            XXPermissions.with(this)
                    .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                    .request(new OnPermissionCallback() {

                        @Override
                        public void onGranted(List<String> permissions, boolean all) {
                            if (all) {
                                createFile();
                            }
                        }

                        @Override
                        public void onDenied(List<String> permissions, boolean never) {
                            if (never) {
                                showToast("被永久拒绝授权，请手动授予存储权限", BToast.NOIC_ERROR);
                                // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                XXPermissions.startPermissionActivity(SplashAty.this, permissions);
                            } else {
                                showToast("获取存储权限失败", BToast.NOIC_ERROR);
                            }
                        }
                    });
        } else {
            XXPermissions.with(this)
                    .permission(Permission.Group.STORAGE)
                    .request(new OnPermissionCallback() {
                        @Override
                        public void onGranted(List<String> permissions, boolean all) {
                            if (all) {
                                createFile();
                            }
                        }

                        @Override
                        public void onDenied(List<String> permissions, boolean never) {
                            if (never) {
                                showToast("被永久拒绝授权，请手动授予存储权限", BToast.NOIC_ERROR);
                                // 如果是被永久拒绝就跳转到应用权限系统设置页面
                                XXPermissions.startPermissionActivity(SplashAty.this, permissions);
                            } else {
                                showToast("获取存储权限失败", BToast.NOIC_ERROR);
                            }
                        }
                    });
        }
    }


    private void createFile() {
        showDia("初始化资源。。。");
        startService(new Intent(MyApp.getInstances(), AndWebServer.class));
        Observable.just(0)
                .map(r -> {
                    MyFileUtil.createDir();
                    return r;
                })
                .compose(ThreadTransformer())
                .subscribe(r -> {
                    hideDialog();
                    toHome();
                }, e -> {
                    hideDialog();
                    e.printStackTrace();
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopService(new Intent(MyApp.getInstances(), AndWebServer.class));
    }


    private void toHome() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
