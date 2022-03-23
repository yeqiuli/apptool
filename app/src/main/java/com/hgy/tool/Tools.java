package com.hgy.tool;

import android.net.Uri;
import android.text.TextUtils;

import com.tecsun.network.utils.LogUntil;

public class Tools {

    public static volatile Tools tools;

    public static Tools getInstance() {
        if (tools == null) {
            synchronized (Tools.class) {
                if (tools == null) {
                    tools = new Tools();
                }
            }
        }
        return tools;
    }

    /**
     * 判断字符串空
     *
     * @param strings String
     * @return true 表示字符串存在空，false表示字符串非空
     */
    public boolean isNull(String... strings) {
        boolean flag = false;
        for (String str : strings) {
            if (TextUtils.isEmpty(str)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    public String getHost(String path) {
        try {
            Uri mUri = Uri.parse(path);
            String host =  mUri.getAuthority();
            LogUntil.e("host:" + host);
            return host;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getScheme(String path) {
        try {
            Uri mUri = Uri.parse(path);
            String scheme = mUri.getScheme()+"://";
            LogUntil.e("scheme:" + scheme);
            return scheme;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getUrl(String path) {
        try {
            Uri mUri = Uri.parse(path);
            String host = mUri.getPath();
            LogUntil.e("getPath:" + host);
            return host;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
