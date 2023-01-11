package com.hgy.aty;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hgy.base.BaseActivity;
import com.hgy.bean.JsDataInfo;
import com.hgy.bean.QrCodeInfo;
import com.hgy.bean.XmlHeadInfo;
import com.hgy.tool.AESUtils;
import com.hgy.tool.MyFileUtil;
import com.hgy.tool.MyTimeUtil;
import com.hgy.tool.RSA2Utils;
import com.hgy.tool.SPUtil;
import com.qrcode.zxing.activity.CaptureActivity;
import com.tecsun.devgateway.CheckDevAty;
import com.tecsun.network.utils.AppUtils;
import com.tecsun.network.utils.LogUntil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import httpclient.org.apache.http.client.ClientProtocolException;
import httpclient.org.apache.http.client.HttpClient;
import httpclient.org.apache.http.client.entity.UrlEncodedFormEntity;
import httpclient.org.apache.http.client.methods.HttpPost;
import httpclient.org.apache.http.impl.client.DefaultHttpClient;
import httpcore.org.apache.http.HttpEntity;
import httpcore.org.apache.http.HttpResponse;
import httpcore.org.apache.http.NameValuePair;
import httpcore.org.apache.http.message.BasicNameValuePair;
import httpcore.org.apache.http.util.EntityUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class XaNetIDAty extends BaseActivity {
    /**
     * 雄安电子身份证
     */

    private TextView tvQrCode;
    private TextView tvQrCodeResult;
    private EditText edQrCode;
    private EditText edPath;
    private TextView tvQuery;
    private int cameraID = 0;
    private int cameraRote = 0;

    // 调用方公钥，用于请求签名时计算数据hash值（平台分配）
    String publicKey = "bbc6d864ca884249a3ec351951627ce54d57c353b55b4917b5fd450a23900204";
    // 调用方私钥，用于请求签名（平台分配）
    String privKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCMO9GVLNicOYjC9I47SQugJY8hHb2EkMftcWaMZbj0iY0iruCltYgiFGvol8V/pj1T2CXbncAReyLh8yzc3k8imrAHDPBw5QNH0+c7jIvPo/ZMuFwGoSXIZ2FB2j6tGZhA6lUsM1yjtFy1DpNjBqjUati3h/9A1FaRh5gu0rK3clNHWQmpA+7a8j8abwdabgYVXxl4kUQM9bVmmCMy73OBCO/ojs/2StS0tQOvu1GfVkLGeUqu2fHwfuvFdLuIhlx5b9nlN7BO5mkHgQevfpoeojv5pYLrsrqiqqHxrHV4uvHoCDryTCfL7AP6xxNncwrgKaEBydOsTsajsyx8xTL5AgMBAAECggEAZh2tKU7F0UVIJIcHB37Se2S+TCQm/GCdZXc9cKEmRNPhNU9ZAPIm1oI+bdoPFDwOzn5IWxsNYO1k4lo84fz0bUNtSUUP1XW+pNBWwpM+wA4qbYWWZbF1HPDC7rwpBc3sg+Df8tbX79GH7MRXaTXRPRUtCMB8jE3TWBgAJqCBSXlftLa6NZhSRiWWpkCM6CpaaBe34IfJTPYoKBFHosAfd6gKp+z/C7OKkHbwr2+yO5YXHwgjiHFerhqQM9aTmRRPo+TtNrnH8D5Bl5TezGZGFlK9jAJntHaB+WTHDWioFhxJp0gTVDvQAy3qiF6K13XLftr+k0DYKnE6t3KwSJHsgQKBgQDEJxmbHYpd3SJjHJR9+Ox7v5/5v0A0qv1+Rwy2YtCHlQgTcuNNEwFJeGz4E/cTELu3fvIv2SFIcL15o+sOvn9YVvQ+n2Igf5uiPVIdT5uxbfzd5KisxzmjCXit0liCNA/eux7lhX0ZZOu7Nksnb8C/doODh9JYKQsYUWbBljUeSQKBgQC3BQxq9xGmafZOaatg2Y6zhmJIyUQ4CN3EzFu2Fsku1lQy+D/W441mlixpv+46IUcZRuCVYQFxsnlEhYuhbfBq+64NPo4OvUs6XqB0PEBBdeAwCUhz+2cis/mZbsTuJuZaAKF1rGmVg0QUZliCzPdrR0hZtJHAeBrC49A8V7MvMQKBgQC8+DI59raTgPOc6i7AQayJ921GLJLNaqG2IvrDRuVAiTnziq4iVZnazxKj2JAiJO/DcqAdqp/e6wYPBvTwCmQilfrNzby6NFWaclsGc+g5gg2nM5+wfGoxgHFrfAbawQ8886ZrVjPT4B8eB8tVXdsiWSmP8KybVEclEV7eRzg7SQKBgD6p3NmW5JUs/KdWaNZNRx3Sqj4vo0roRj5GljQfUxzVR5j4BKun19dDHcvLal6+3CAcc3LR9vOd3wWGEAvin4mRyMNCItOLHoHKundx1bJUEMGBvCvx7RslshVFpum/qekxBBdNRA1sF1hAvpdyZFh33J7SQ4E53jfSVo8CnBhRAoGAAT9HqzS2KiGuX6WI9GKogjm3y8Bl3jjKGMScFR40pjcvLLPVCwTM0cMaHOcLXO1C17NRzjJioFUcT3YuQ+SK9qQXvSujiq5Vx5LN/oafy2l8F66vV7KZpoBao63nrV/6gWHMYzUc9Vwi0lHXK1eb/D3ZF8WLe0rsebBO74o3f1w=";
    // 开放平台公钥，用于响应验签（平台分配）
    String publicKeyGateway = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmP0ag59TmO3jiSN265UCOhVDc0UDFeoeTZevRIuft+hIUA7ZEPziYudyBvt51qFq5Gfp4KOVGscpAgcU/8IyNTCMk2t9NpOe710L+W9xRzTPZR9NKbAm7cOE6Wk0tjDstuyUXIke22gk0lBy1izlhPQw1uaByC7RMMKu3bRE9GzdDvNbdcAnKYTQ3nc68npejGP/SpQ18hsf0ELT51PBdF23B7m3W275J5tuag9Zbg6jzsIAx9dzgdNi1/xrq7Tae4yyBfB989UTt5wQbddOqieFaEAzGFn7K05EPnQDQJRojVb0RNDrhr/bMHz3hl7UwY6THFL2CpI/BBy5VD9HXQIDAQAB";

    String appID = "20220805180755000059";

    @Override
    public int getLayout() {
        return R.layout.js_qr_code_aty;
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        initView();
    }

    @Override
    public void backAty() {

    }

    @Override
    public void gotoCheckIn() {

    }

    private void initView() {
        Spinner spQrCamera = findViewById(R.id.spQrCamera);
        Spinner spQrRote = findViewById(R.id.spQrRote);
        edPath = findViewById(R.id.edPath);
        edQrCode = findViewById(R.id.edQrCode);
        tvQuery = findViewById(R.id.tvQuery);
        tvQrCode = findViewById(R.id.tvQrCode);
        tvQrCodeResult = findViewById(R.id.tvQrCodeResult);
        tvQrCode.setOnClickListener(v -> {
            tvQrCodeResult.setText("");
            Intent intent = new Intent(XaNetIDAty.this, CaptureActivity.class);
            intent.putExtra(CheckDevAty.CAMERA_ID, cameraID);
            intent.putExtra(CheckDevAty.CAMERA_ROTE, cameraRote);

            startActivityForResult(intent, 5);
        });
        List<String> lists = getCameraList();
        String[] a = lists.toArray(new String[lists.size()]);
        ArrayAdapter<String> cameras = new ArrayAdapter<>(this, R.layout.lay_spinner_w, a);
        cameras.setDropDownViewResource(R.layout.lay_spinner);

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.camera, R.layout.lay_spinner_w);
        arrayAdapter.setDropDownViewResource(R.layout.lay_spinner);
        spQrCamera.setAdapter(cameras);
        spQrRote.setAdapter(arrayAdapter);
        spQrCamera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cameraID = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spQrRote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        cameraRote = 0;
                        break;
                    case 1:
                        cameraRote = 90;
                        break;
                    case 2:
                        cameraRote = 180;
                        break;
                    case 3:
                        cameraRote = 270;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvQuery.setOnClickListener(v -> {
            String qrCode = edQrCode.getText().toString();
            if (TextUtils.isEmpty(qrCode)) {
                return;
            }
//            toCheckQrCode(qrCode);
        });
    }


    //查找摄像头Id
    public List<String> getCameraList() {
        int numberOfCameras = Camera.getNumberOfCameras();
        List<String> fontNumList = new ArrayList<>();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            String info = "摄像头" + i;
            Camera.getCameraInfo(i, cameraInfo);
            fontNumList.add(info);
        }
        return fontNumList;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUntil.e("requestCode:" + requestCode);
        LogUntil.e("resultCode:" + resultCode);
        if (requestCode != 5) {
            Toast.makeText(this, "扫码出错", Toast.LENGTH_SHORT).show();
            return;
        }
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "用户取消操作", Toast.LENGTH_SHORT).show();
            return;
        }
        if (resultCode == 12) {
            Toast.makeText(this, "扫码失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if (resultCode == RESULT_OK) {
            String qrCode = data.getExtras().getString("data");
            if (TextUtils.isEmpty(qrCode)) {
                Toast.makeText(this, "扫码失败", Toast.LENGTH_SHORT).show();
                return;
            }
            upTextViewMsg("扫码数据：" + qrCode);
            edQrCode.setText(qrCode);
        } else {
            Toast.makeText(this, "扫码失败", Toast.LENGTH_SHORT).show();
        }
    }

//    private void toCheckQrCode(String qrCode) {
//        showDia("请求数据。。。。。");
//        String path = edPath.getText().toString();
//        Observable.just(qrCode)
//                .map(r -> {
//                    try {
//                        Service service = new Service();
//                        byte[] buffer = messageByte(r);
//                        if (buffer == null) {
//                            return null;
//                        }
//                        Call call = service.createCall();
//                        call.setOperationName(new QName("http://jws.wsscclib.wondersgroup.com", "dispatch"));
//                        call.setTargetEndpointAddress(path);
//                        call.addParameter("request", XMLType.XSD_BASE64, ParameterMode.IN);
//                        call.setReturnType(XMLType.XSD_BASE64);
//                        return (byte[]) call.invoke(new Object[]{buffer});
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return null;
//                    }
//
//                })
//                .compose(ThreadTransformer())
//                .subscribe(r -> {
//                    hideDialog();
//                    echoBytes(r);
//                }, e -> {
//                    hideDialog();
//                    upTextViewMsg("请求错误:" + e.getMessage());
//                    e.printStackTrace();
//                });
//    }

    public void echoBytes(byte[] req) {

        ByteBuf byteBuf = Unpooled.wrappedBuffer(req);
        short headlen = byteBuf.readShort();
        int bodylen = byteBuf.readInt();
        byte flag = byteBuf.readByte();
        byte[] head = new byte[headlen];
        byte[] body = new byte[bodylen];
        byte[] sign;
        byteBuf.readBytes(head);
        byteBuf.readBytes(body);
        if ((flag & 0x01) == 0x01) {
            sign = new byte[128];
            byteBuf.readBytes(sign);
        } else {
            sign = new byte[0];
        }
        String backHead = new String((byte[]) head, StandardCharsets.UTF_8);
        String backBody = new String((byte[]) body, StandardCharsets.UTF_8);
        upTextViewMsg("请求结果Head：" + backHead + "\n请求结果Body：" + backBody);
    }

    private String getSerialNumber() {
        String time = SPUtil.getInstance().getData("TIMES");
        String newTime = MyTimeUtil.formatDatetimeName(new Date());
        int id;
        if (time.equals(newTime)) {//同一天
            id = SPUtil.getInstance().getInteger("COUNTS") + 1;
        } else {//不是同一天
            id = 1;
            SPUtil.getInstance().setData("TIMES", newTime);
        }
        SPUtil.getInstance().setInteger("COUNTS", id);
        String serialNumber;
        if (id < 10000000) {
            serialNumber = getNum(id + "");
        } else {
            serialNumber = id + "";
        }
        return "I90019" + newTime + serialNumber;
    }

    private String getNum(String id) {
        int a = 8 - (id.length());
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < a; i++) {
            buffer.append("0");
        }
        return buffer + id;
    }

    private void upTextViewMsg(String msg) {
        MyFileUtil.saveLog(msg);
        String str = tvQrCodeResult.getText().toString();
        if (TextUtils.isEmpty(str)) {
            tvQrCodeResult.setText(msg);
        } else {
            String data = str + "\n" + msg;
            tvQrCodeResult.setText(data);
        }
    }


    /**
     * 测试报文
     */
    public byte[] messageByte(String qrCode) {
        String headXml = getHeadXml();
        String bodyXml = getBodyXml(qrCode);
        if (bodyXml == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        try {
            byte[] headByte = headXml.getBytes(StandardCharsets.UTF_8);
            byte[] bodyByte = bodyXml.getBytes(StandardCharsets.UTF_8);
            out.writeShort(headByte.length);
            out.writeInt(bodyByte.length);
            out.writeByte(0);
            out.write(headByte);
            out.write(bodyByte);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    private String getHeadXml() {
        XmlHeadInfo headInfo = new XmlHeadInfo();
        headInfo.setVersion("1.0.0");
        String id = getSerialNumber();
        headInfo.setRef(id);
        headInfo.setSysCode("I90019");
        headInfo.setBusCode("I903K00053");
        headInfo.setTradeSrc("O");
        headInfo.setSender(appID);
        headInfo.setReciver("I90019");
        headInfo.setDate(MyTimeUtil.formatDatetimeName(new Date()));
        headInfo.setTime(MyTimeUtil.formatTimeName(new Date()));
        headInfo.setReSnd("N");
        XmlHeadInfo.Rst rst = new XmlHeadInfo.Rst();
        rst.setTradeCode("99");
        headInfo.setRst(rst);
        String head = headInfo.toString();
        upTextViewMsg("请求头部headXML:" + head);
        return head;
    }

    private String getBodyXml(String qrCode) {
        try {
            QrCodeInfo codeInfo = new QrCodeInfo();
            codeInfo.setBz("");
            codeInfo.setAaa030("E");
            codeInfo.setCard_str(qrCode);
//        codeInfo.setDevice_id_in_app(AppUtils.getInstance().getCPUSerial(this));
            codeInfo.setDevice_id_in_app("869092032427152");

            JsDataInfo dataInfo = new JsDataInfo();
            dataInfo.setCharset("UTF-8");
            dataInfo.setJson(AppUtils.getInstance().toJson(codeInfo));
            dataInfo.setSign_type("RSA2");

            String dataInfoStr = dataInfo.toString1();
            upTextViewMsg("json数据：" + dataInfoStr);

            String json = URLEncoder.encode(dataInfoStr, "utf-8");
            upTextViewMsg("URLEncoder：" + json);

            String sign = RSA2Utils.encrypt(json);
            upTextViewMsg("SHA256WithRSA：" + json);

            String urlSign = URLEncoder.encode(sign, "utf-8");
            upTextViewMsg("urlSign：" + urlSign);
            dataInfo.setSign(urlSign);
            String resData = AESUtils.encrypt(dataInfo.toString2(), publicKey);

            String bodyXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<data>" +
                    "<JSON>" + resData + "</JSON>" +
                    "</data>";
            upTextViewMsg("请求数据Body：" + bodyXml);
            return bodyXml;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String postData(final String url, final Map<String, String> paramsValue) {

        //用HttpClient发送请求，分为五步
        //第一步：创建HttpClient对象
        HttpClient httpCient = new DefaultHttpClient();
        //第二步：创建代表请求的对象,参数是访问的服务器地址
        String response = "";
        try {
            //第三步：执行请求，获取服务器发还的相应对象

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (String key : paramsValue.keySet()) {
                nvps.add(new BasicNameValuePair(key, paramsValue.get(key)));
            }

            String str = EntityUtils.toString(new UrlEncodedFormEntity(nvps, "utf-8"));

            String absoluteURL = url + "?" + str;
            Log.i("Test", "absoluteURL:" + absoluteURL);

            HttpPost httpPost = new HttpPost(absoluteURL);
            //.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            HttpResponse httpResponse = httpCient.execute(httpPost);
            //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                //第五步：从相应对象当中取出数据，放到entity当中
                HttpEntity entity = httpResponse.getEntity();
                response = EntityUtils.toString(entity, "utf-8");//将entity当中的数据转换为字符串

                Log.i("Test", "response:" + response);
            } else {
                //TODO
            }

        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        } catch (Exception e) {
        }

        return response;

    }
}