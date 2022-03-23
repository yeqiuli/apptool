package com.hgy.tool;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * 基础参数的操作
 */
public class SPUtil {

    private static final String DB_NAME = "toolConfig";
    private static SPUtil instance;
    private static SharedPreferences sp;

    public static void initContext(Context mContext) {
        sp = mContext.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
    }

    public static SPUtil getInstance() {
        if (instance == null) {
            synchronized (SPUtil.class) {
                if (instance == null) {
                    instance = new SPUtil();
                    if (sp == null) {
                        throw new Error("SharedPreferences 未初始化");
                    }
                }
            }
        }
        return instance;
    }

    public String getData(String key) {
        return sp.getString(key, "");
    }

    public int getInteger(String key) {
        return sp.getInt(key, 0);
    }

    public void setInteger(String key, int data) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, data);
        editor.apply();
    }


    public void setData(String key, String data) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, data);
        editor.apply();
    }

    /**
     * 清空配置
     */
    public void clear() {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 储存Boolean值
     */
    public void setBoolean(String key, Boolean data) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, data);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

    public boolean isFirst() {
        return getBoolean("isFirst");
    }

    public void setFirst() {
        setBoolean("isFirst", true);
    }

}
