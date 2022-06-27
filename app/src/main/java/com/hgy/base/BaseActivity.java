package com.hgy.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.hgy.aty.R;
import com.hgy.view.BamToast;
import com.tecsun.network.utils.ActivityStackManager;
import com.tecsun.network.utils.LogUntil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

/**
 * Created by hgy on 17/11/3.
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    private ZLoadingDialog dialog;


    private final static int MSG_CODE = 100;

    public abstract int getLayout();

    private AlertDialog advertDialog;

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            LogUntil.e("handleMessage：" + msg.what);
//            if (msg.what == MSG_CODE) {
//                List<Bitmap> bitmaps = AdvertUtil.getUtil().getImageViews();
//                if (bitmaps != null) {
//                    boolean isOpen = KeyboardVisibilityObserver.getInstance().isWasOpened();
//                    LogUntil.e("软键盘状态" + isOpen);
//                    if (isOpen) {
//                        showAdvert();
//                        return;
//                    }
//                    if (SPUtil.getInstance().getBoolean(ParameterKey.CLOSE_ADVERT)) {
//                        showAdvert();
//                        return;
//                    }
//                    advertDialog = DialogUtils.showAdvert(BaseActivity.this, bitmaps, () -> showAdvert());
//                }
//            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStackManager.getInstance().add(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getLayout());
        dialog = new ZLoadingDialog(this);

        initView(savedInstanceState);
    }


    public void hideKeyboard(Activity activity, IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {        //down的时候,移除消息
                handler.removeMessages(MSG_CODE);
                break;
            }
            case MotionEvent.ACTION_UP: {        //up的时候，发送延迟消息
                handler.sendEmptyMessageDelayed(MSG_CODE, getTimes() * 1000L);
                break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public synchronized void disViewFlipper() {
        if (advertDialog != null) {
            if (advertDialog.isShowing()) {
                advertDialog.dismiss();
            }
        }
    }

    private int getTimes() {
//        int time = SPUtil.getInstance().getInteger(ParameterKey.ADVERT_TIME);
//        if (time == 0) {
//            time = 60;
//            SPUtil.getInstance().setInteger(ParameterKey.ADVERT_TIME, time);
//        }
//        return time;
        return 20;
    }


    public abstract void initView(Bundle savedInstanceState);

    public abstract void backAty();

    public abstract void gotoCheckIn();

    public void setTitleShow(String str, boolean isShow) {

    }

    private void showAdvert() {
        handler.removeMessages(MSG_CODE);
        handler.sendEmptyMessageDelayed(MSG_CODE, getTimes() * 1000L);
    }


    public synchronized void showDia(String msg) {
        dialog.setLoadingBuilder(Z_TYPE.LEAF_ROTATE)//设置类型
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setLoadingColor(getResources().getColor(R.color.purple_200))//颜色
                .setHintText(msg)
                .setHintTextSize(20) // 设置字体大小 dp
                .setHintTextColor(Color.WHITE)  // 设置字体颜色
                .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                .setDialogBackgroundColor(Color.WHITE) // 设置背景色，默认白色
                .show();
    }


    public void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void showToast(String str, int errOrOk) {
        BamToast.showText(this, str, Toast.LENGTH_LONG, errOrOk);
    }

    public void showMsg(String msg) {
        Snackbar.make(getWindow().getDecorView().getRootView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        //for new api versions.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showAdvert();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeMessages(MSG_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(MSG_CODE);
        ActivityStackManager.getInstance().remove(this);
    }
}
