package com.hgy.net;

import android.text.TextUtils;

import androidx.lifecycle.LifecycleOwner;

import com.hgy.bean.GroupIdBean;
import com.hgy.bean.QueryIdBean;
import com.hgy.bean.UpResultBean;
import com.hikvision.artemis.sdk.constant.Constants;
import com.hikvision.artemis.sdk.constant.HttpHeader;
import com.hikvision.artemis.sdk.constant.HttpMethod;
import com.tecsun.network.network.ResultBean;
import com.tecsun.network.network.RetrofitManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;


public class HKDataUpUtil {
    public static volatile HKDataUpUtil util;
    /**
     * 单个添加人脸分组接口
     */
    private final String addUrl = "/artemis/api/v1/facegroup/single/add";
    /**
     * 外来人员登记接口
     */
    private final String registerUrl = "/artemis/api/v1/person/register";
    /**
     * 查询id
     */
    private final String queryID = "/artemis/api/v1/facegroup/search";

    //签名Header
    public static final String X_CA_SIGNATURE = "x-ca-signature";
    //所有参与签名的Header
    public static final String X_CA_SIGNATURE_HEADERS = "x-ca-signature-headers";
    //请求时间戳
    public static final String X_CA_TIMESTAMP = "x-ca-timestamp";
    //请求放重放Nonce,15分钟内保持唯一,建议使用UUID
    public static final String X_CA_NONCE = "x-ca-nonce";
    //APP KEY
    public static final String X_CA_KEY = "x-ca-key";

    private String host;

    public String appKey;
    public String appSecret;

    public static HKDataUpUtil getInstance() {
        if (util == null) {
            synchronized (HKDataUpUtil.class) {
                if (util == null) {
                    util = new HKDataUpUtil();
                }
            }
        }
        return util;
    }

    public HKDataUpUtil setHost(String host) {
        this.host = host;
        return this;
    }

    public HKDataUpUtil setAppKey(String appKey) {
        this.appKey = appKey;
        return this;
    }

    public HKDataUpUtil setAppSecret(String appSecret) {
        this.appSecret = appSecret;
        return this;
    }

    public QueryIdBean queryGroupID() {
        return null;
    }

    public GroupIdBean addGroupID() {
        return null;
    }

    public UpResultBean upData() {
        return null;
    }

    public ResultBean<String> doPost(String url, Object object, LifecycleOwner owner) {
        String time = String.valueOf(new Date().getTime());
        String uuid = UUID.randomUUID().toString();
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "*/*");
        headers.put("Content-Type", "application/json");
        headers.put(X_CA_TIMESTAMP, time);
        headers.put(X_CA_NONCE, uuid);
        headers.put(X_CA_KEY, appKey);
        String sign = sign(appSecret, url, headers);
        return RetrofitManager.create()
                .setHostUrl(host)
                .setUrl(url)
                .needLife(owner != null)
                .bindLife(owner)
                .addHeader("Accept", "*/*")
                .addHeader("Content-Type", "application/json")
                .addHeader(X_CA_TIMESTAMP, time)
                .addHeader(X_CA_SIGNATURE_HEADERS, X_CA_KEY + "," + X_CA_NONCE + "," + X_CA_TIMESTAMP)
                .addHeader(X_CA_NONCE, uuid)
                .addHeader(X_CA_KEY, appKey)
                .addHeader(X_CA_SIGNATURE, sign)
                .setDataStr(true)
                .executePostBody(object, String.class);
    }

    public static String sign(String secret, String path,
                              Map<String, String> headers) {
        try {
            Mac macSha256 = Mac.getInstance(Constants.HMAC_SHA256);
            byte[] keyBytes = secret.getBytes(Constants.ENCODING);
            macSha256.init(new SecretKeySpec(keyBytes, 0, keyBytes.length, Constants.HMAC_SHA256));
            byte[] doFinal = macSha256.doFinal(buildStringToSign(path, headers).getBytes(Constants.ENCODING));
            byte[] base64 = Base64.encode(doFinal, Base64.NO_WRAP);
            return new String(base64, Constants.ENCODING);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建待签名字符串
     */
    private static String buildStringToSign(String path, Map<String, String> headers) {
        StringBuilder sb = new StringBuilder();

        sb.append(HttpMethod.POST.toUpperCase()).append(Constants.LF);
        if (null != headers) {
            if (null != headers.get(HttpHeader.HTTP_HEADER_ACCEPT)) {
                sb.append(headers.get(HttpHeader.HTTP_HEADER_ACCEPT));
                sb.append(Constants.LF);
            }

            if (null != headers.get(HttpHeader.HTTP_HEADER_CONTENT_MD5)) {
                sb.append(headers.get(HttpHeader.HTTP_HEADER_CONTENT_MD5));
                sb.append(Constants.LF);
            }

            if (null != headers.get(HttpHeader.HTTP_HEADER_CONTENT_TYPE)) {
                sb.append(headers.get(HttpHeader.HTTP_HEADER_CONTENT_TYPE));
                sb.append(Constants.LF);
            }

            if (null != headers.get(HttpHeader.HTTP_HEADER_DATE)) {
                sb.append(headers.get(HttpHeader.HTTP_HEADER_DATE));
                sb.append(Constants.LF);
            }
        }
        sb.append(buildHeaders(headers));
        sb.append(path);
        return sb.toString();
    }

    /**
     * 构建待签名Http头
     *
     * @param headers 请求中所有的Http头
     * @return 待签名Http头
     */
    private static String buildHeaders(Map<String, String> headers) {
        StringBuilder sb = new StringBuilder();


        if (null != headers) {
            Map<String, String> sortMap = new TreeMap<>(headers);
            StringBuilder signHeadersStringBuilder = new StringBuilder();
            for (Map.Entry<String, String> header : sortMap.entrySet()) {
                if (isHeaderToSign(header.getKey())) {
                    sb.append(header.getKey());
                    sb.append(Constants.SPE2);
                    if (!TextUtils.isEmpty(header.getValue())) {
                        sb.append(header.getValue());
                    }
                    sb.append(Constants.LF);
                    if (0 < signHeadersStringBuilder.length()) {
                        signHeadersStringBuilder.append(Constants.SPE1);
                    }
                    signHeadersStringBuilder.append(header.getKey());
                }
            }
            headers.put(X_CA_SIGNATURE_HEADERS, signHeadersStringBuilder.toString());
        }


        return sb.toString();
    }

    /**
     * Http头是否参与签名 return
     */
    private static boolean isHeaderToSign(String headerName) {
        if (TextUtils.isEmpty(headerName)) {
            return false;
        }
        return headerName.startsWith(Constants.CA_HEADER_TO_SIGN_PREFIX_SYSTEM);
    }
}
