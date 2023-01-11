package com.tecsun.sixse.dbnfc;

import android.content.Context;

import com.eidlink.idocr.sdk.EidLinkSE;
import com.eidlink.idocr.sdk.EidLinkSEFactory;
import com.eidlink.idocr.sdk.bean.EidlinkInitParams;

public class ReadCardManager {
    public static EidLinkSE eid;
    public static String tag_need_add_picture = "tag_need_add_picture";

    /**
     * 生产环境配置：
     * appid:请填写生产环境分配的appid
     * 生产环境ip：eidcloudread.eidlink.com
     * 端口：9989
     * envCode：52302
     */

    /**
     * 测试环境配置：
     * appid:请填写测试环境分配的appid
     * 测试环境ip：testeidcloudread.eidlink.com
     * 端口：9989
     * envCode：26814
     */
//    public static String appid="TESTID20220420100847";
    public static String appid="15B1E02";
//    public static String ip = "testeidcloudread.eidlink.com";
    public static String ip = "eidcloudread.eidlink.com";
//    public static int envCode = 26814;
    public static int envCode = 52302;
    public static int port = 9989;

    /**
     * SDK初始化
     */
    public static void initEid(final Context context) {

        eid = EidLinkSEFactory.getEidLinkSE(new EidlinkInitParams(context, appid, ip, port, envCode));

    }
}
