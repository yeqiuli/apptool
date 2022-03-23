package com.hgy.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.hgy.aty.R;


public class BToast extends Toast {

    /**
     * Toast单例
     */
    private static BToast toast;

    /**
     * Toast所显示的图片
     */
    private static ImageView toast_img;

    /**
     * 图标状态 显示对号图标
     */
    public final static int ICONTYPE_SUCCEED = 1;
    /**
     * 图标状态 显示叉号图标
     */
    public final static int ICONTYPE_ERROR = 2;
    /**
     * 显示失败
     */
    public final static int NOIC_ERROR = 3;
    /**
     * 显示成功
     */
    public final static int NOIC_SUCCEED = 4;

    /**
     * 构造
     *
     * @param context
     */
    public BToast(Context context) {
        super(context);
    }

    /**
     * 获取Toast对象
     *
     * @param context  上下文
     * @param text     显示的文本
     * @param time     持续的时间
     * @param iconType 图标显示状态【0：不显示图标】【1：显示对号图标】2：显示叉号图标】
     */
    public static BToast getToast(Context context, CharSequence text, int time, int iconType) {
        initToast(context, text, iconType);

        switch (iconType) {
            case ICONTYPE_SUCCEED:
                toast_img.setBackgroundResource(R.drawable.toast_y);
                toast_img.setVisibility(View.VISIBLE);
//                ObjectAnimator.ofFloat(toast_img, "rotationY", 30, 180, 0).setDuration(1000).start();
                ObjectAnimator.ofFloat(toast_img, "scaleX", 0, 1, 1).setDuration(400).start();
                ObjectAnimator.ofFloat(toast_img, "scaleY", 0, 1, 1).setDuration(400).start();
                break;
            case ICONTYPE_ERROR:
                toast_img.setBackgroundResource(R.drawable.toast_n);
                toast_img.setVisibility(View.VISIBLE);
//                ObjectAnimator.ofFloat(toast_img, "rotationY", 30, 180, 0).setDuration(1000).start();
                ObjectAnimator.ofFloat(toast_img, "scaleX", 0, 1, 1).setDuration(400).start();
                ObjectAnimator.ofFloat(toast_img, "scaleY", 0, 1, 1).setDuration(400).start();
                break;
            case NOIC_ERROR:
            case NOIC_SUCCEED:
                toast_img.setVisibility(View.GONE);
                break;
        }

        if (time == Toast.LENGTH_LONG) {
            toast.setDuration(Toast.LENGTH_LONG);
        } else {
            toast.setDuration(Toast.LENGTH_SHORT);
        }

        return toast;
    }

    /**
     * 初始化Toast
     *
     * @param context 上下文
     * @param text    显示的文本
     */
    private static void initToast(Context context, CharSequence text, int iconType) {
        try {
            cancelToast();

            toast = new BToast(context);
            // 获取LayoutInflater对象
            /**
             * Toast引用布局所需要的inflater
             */
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // 由layout文件创建一个View对象
            /**
             * Toast所引用的布局
             */
            View layout = inflater.inflate(R.layout.toast_layout, null);

            // 吐司上的图片
            toast_img = layout.findViewById(R.id.toast_img);

            // 实例化ImageView和TextView对象
            /**
             * Toast显示的文本
             */
            TextView toast_text = layout.findViewById(R.id.toast_text);
            toast_text.setText(text);
            View view = layout.findViewById(R.id.view);

            if (iconType == ICONTYPE_ERROR || iconType == NOIC_ERROR) {
                view.setBackground(ContextCompat.getDrawable(context, R.drawable.toast_bj_red));
            }
            toast.setView(layout);
            toast.setGravity(Gravity.CENTER, 0, 70);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏当前Toast
     */
    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

    public void cancel() {
        try {
            super.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}