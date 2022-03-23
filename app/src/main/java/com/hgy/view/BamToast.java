package com.hgy.view;

import android.content.Context;
import android.widget.Toast;

public class BamToast {
    private static Object mToast;

    private BamToast(Context context, CharSequence text, int time, int iconType) {
        if (mToast != null) {
            cancel();
        }
        mToast = BToast.getToast(context, text, time, iconType);

    }


    /**
     * 显示一个带图标的吐司
     *
     * @param context 上下文
     * @param text    显示的文本
     * @param time    持续的时间
     * @param type    BToast.ICONTYPE_ERROR;失败+加图片
     *                BToast.ICONTYPE_SUCCEED;成功+图片
     *                BToast.NOIC_ERROR;失败
     *                BToast.NOIC_SUCCEED;成功
     */
    public static void showText(Context context, CharSequence text, int time, int type) {
        new BamToast(context, text, time, type).show();
    }

    public void show() {
        ((Toast) mToast).show();
    }

    public void cancel() {
        ((Toast) mToast).cancel();
    }
}