package com.hgy.tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;


public class PermissionTool {
    /*
    检查并申请权限
     */
    private static final int REQUEST_CODE = 1;
    private static final String CALL_ONE_STRING = "为了程序正常运行，请授予程序使用所需权限";
    private static final String CALL_TWO_STRING = "为了程序正常运行，请授予程序使用所需权限";

    public static boolean checkPermission(Activity context, String pList[]) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> list = new ArrayList<>();
            for (String aPList : pList) {
                if (!(ContextCompat.checkSelfPermission(context, aPList) == PackageManager.PERMISSION_GRANTED)) {
                    list.add(aPList);
                }
            }
            if (list.size() > 0) {
                ActivityCompat.requestPermissions(context, list.toArray(new String[list.size()]), REQUEST_CODE);
                return false;
            }
        }
        return true;
    }

    public static void requestCheckPermissionResult(Activity activity, int requestCode, String[] permissions, int[] grantResults, String list[], PermissionResult lis) {
        if (requestCode == REQUEST_CODE) {
            if (permissions != null && grantResults != null) {
                List<String> mList = new ArrayList<>();
                for (int i = 0; i < permissions.length; i++) {
                    if (!(grantResults[i] == PackageManager.PERMISSION_GRANTED)) {
                        mList.add(permissions[i]);
                    }
                }
                if (mList.size() == 0) {
                    lis.getAllPermissionSuccess(true);
                    return;
                }
                for (int i = 0; i < mList.size(); i++) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, mList.get(i))) {
                        AlertDialog dialog = new AlertDialog.Builder(activity)
                                .setMessage(CALL_ONE_STRING)
                                .setPositiveButton("设置", (dialogInterface, is) -> {
                                    getAppDetailSettingIntent(activity);
                                    lis.isStartActivity(true);
                                })
                                .setNegativeButton("取消", (dialogInterface, is) -> System.exit(0)).create();
                        dialog.show();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, mList.get(i))) {
                            AlertDialog dialog = new AlertDialog.Builder(activity)
                                    .setMessage(CALL_TWO_STRING)
                                    .setPositiveButton("允许", (dialogInterface, is) -> checkPermission(activity, list))
                                    .setNegativeButton("取消", (dialogInterface, is) -> System.exit(0)).create();
                            dialog.show();
                        }
                    }
                }
            }
        }
    }

    public interface PermissionResult {
        void isStartActivity(boolean start);

        void getAllPermissionSuccess(boolean success);
    }

    private static void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(localIntent);
    }
}
