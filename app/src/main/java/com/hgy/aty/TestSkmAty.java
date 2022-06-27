package com.hgy.aty;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hgy.bean.SkmBean;
import com.tecsun.network.aty.BaseAty;
import com.tecsun.network.network.CallBack;
import com.tecsun.network.network.HttpExceptionBean;
import com.tecsun.network.network.RetrofitManager;
import com.tecsun.network.utils.LogUntil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class TestSkmAty extends BaseAty {

    private String path = "https://skrio.tgovcloud.com";
    private String qrCodePath = "/ebus/wyj/healthy-thirdapi/qrc-code-status/";
    private String qrCodeInfo = "/ebus/wyj/prominent-citizens/v1/api/thirtparty/qrccode/infobycode/";
    private String id = "zsjyx";
    private String token = "MGO0J0e9yQ7KRw8D7JA1L4rqJxBa8XFJ";
    private EditText edQrCode;
    private TextView tvResult;

    @Override
    protected int getLayout() {
        return R.layout.aty_skm;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        edQrCode = findViewById(R.id.edQrCode);
        tvResult = findViewById(R.id.tvResult);

        findViewById(R.id.tvTestSkm).setOnClickListener(v -> {
            String data = edQrCode.getText().toString();
            getSkm(data);
        });
        findViewById(R.id.tvTestSkmInfo).setOnClickListener(v -> {
            String data = edQrCode.getText().toString();
            getSkmInfo(data);
        });
    }

    private void getSkm(String data) {
        SkmBean skmBean = RetrofitManager.get().getGson().fromJson(data, SkmBean.class);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String time = System.currentTimeMillis() / 1000 + "";
        RetrofitManager.create()
                .setUrl(path + qrCodePath + skmBean.getCodeId())
                .setDataStr(true)
                .needLife(false)
                .addHeader("x-tif-signature", getSHA256(time + token + uuid + time))
                .addHeader("x-tif-paasid", id)
                .addHeader("x-tif-timestamp", time)
                .addHeader("x-tif-nonce", uuid)
                .addParam("color", "true")
                .postBody(null, String.class, new CallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        String msg = "穗康码状态：" + s;
                        LogUntil.e(msg);
                        tvResult.setText(msg);
                    }

                    @Override
                    public void onFailure(HttpExceptionBean exceptionBean) {

                    }
                });
    }

    private void getSkmInfo(String data) {
        SkmBean skmBean = RetrofitManager.get().getGson().fromJson(data, SkmBean.class);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String time = System.currentTimeMillis() / 1000 + "";
        RetrofitManager.create()
                .setUrl(path + qrCodeInfo + skmBean.getCodeId())
                .setDataStr(true)
                .needLife(false)
                .addHeader("x-tif-signature", getSHA256(time + token + uuid + time))
                .addHeader("x-tif-paasid", id)
                .addHeader("x-tif-timestamp", time)
                .addHeader("x-tif-nonce", uuid)
                .postBody(null, String.class, new CallBack<String>() {
                    @Override
                    public void onSuccess(String s) {
                        String msg = "穗康码信息：" + s;
                        LogUntil.e(msg);
                        tvResult.setText(msg);
                    }

                    @Override
                    public void onFailure(HttpExceptionBean exceptionBean) {

                    }
                });
    }

    /**
     * 利用java原生的类实现SHA256加密
     *
     * @param str 参数拼接的字符串
     * @return
     */
    public static String getSHA256(String str) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr.toUpperCase();
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                // 1得到一位的进行补0操作
                sb.append("0");
            }
            sb.append(temp);
        }
        return sb.toString();
    }


}
