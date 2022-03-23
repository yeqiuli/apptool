package com.hgy.aty;

import static com.tecsun.network.network.BaseHelper.ThreadTransformer;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.widget.EditText;
import android.widget.TextView;

import com.hgy.base.BaseActivity;
import com.hgy.bean.CourtGroupBean;
import com.hgy.bean.GroupIdBean;
import com.hgy.bean.QueryIdBean;
import com.hgy.bean.VisitorBean;
import com.hgy.net.HKDataUpUtil;
import com.hgy.tool.MyFileUtil;
import com.hgy.tool.Tools;
import com.hgy.view.BToast;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.hikvision.artemis.sdk.constant.HttpMethod;
import com.hikvision.artemis.sdk.constant.SystemHeader;
import com.hikvision.artemis.sdk.util.SignUtil;
import com.tecsun.network.network.RetrofitManager;
import com.tecsun.network.utils.AppUtils;
import com.tecsun.network.utils.LogUntil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import rx.Observable;

public class HkDataSynAty extends BaseActivity {
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

    private final String contentType = "application/json";

    private String path;
    private String courtId;
    private String courtName;
    private String groupId = "5ds82633-a4cd-4107-b55e-f21bbdf952f9";
    private EditText edPath;
    private EditText edAppKey;
    private EditText edAppSecret;
    private EditText edCourtId;
    private EditText edCourtName;
    private TextView tvResult;


    @Override
    public int getLayout() {
        return R.layout.aty_hk;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        edPath = findViewById(R.id.edPath);
        edAppKey = findViewById(R.id.edAppKey);
        edAppSecret = findViewById(R.id.edAppSecret);
        edCourtId = findViewById(R.id.edCourtId);
        edCourtName = findViewById(R.id.edCourtName);
        findViewById(R.id.tvInit).setOnClickListener(v -> initHK());
        findViewById(R.id.tvAddGroup).setOnClickListener(v -> queryID());
        TextView tvUpData = findViewById(R.id.tvUpData);
        tvResult = findViewById(R.id.tvResult);
        tvResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvUpData.setOnClickListener(v -> upVisitor());
    }

    @Override
    public void backAty() {

    }

    @Override
    public void gotoCheckIn() {

    }

    private void initHK() {
        String path = edPath.getText().toString();
        String appKey = edAppKey.getText().toString();
        String appSecret = edAppSecret.getText().toString();
        String courtId = edCourtId.getText().toString();
        String courtName = edCourtName.getText().toString();
        if (!AppUtils.getInstance().isURL(path)) {
            showToast("请填写正确的地址", BToast.ICONTYPE_ERROR);
            return;
        }
        if (Tools.getInstance().isNull(appKey, appSecret, courtId, courtName)) {
            showToast("所有参数必填", BToast.ICONTYPE_ERROR);
            return;
        }
        ArtemisConfig.host = Tools.tools.getHost(path); // 平台的ip端口
        ArtemisConfig.appKey = appKey;  // 密钥appkey
        ArtemisConfig.appSecret = appSecret;// 密钥appSecret
        this.path = path;
        this.courtId = courtId;
        this.courtName = courtName;
        showToast("初始化成功", BToast.ICONTYPE_SUCCEED);
        testSign(appSecret, appKey);
    }

    private void testSign(String appSecret, String appKey) {
        Observable.just(appSecret)
                .map(r -> {
                    String time = String.valueOf(new Date().getTime());
                    String uuid = UUID.randomUUID().toString();
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Accept", "*/*");
                    headers.put("Content-Type", "application/json");
                    headers.put(SystemHeader.X_CA_TIMESTAMP, time);
                    headers.put(SystemHeader.X_CA_NONCE, uuid);
                    headers.put(SystemHeader.X_CA_KEY, appKey);
                    String sign1 = SignUtil.sign(r, HttpMethod.POST, addUrl, headers, null, null, null);
                    String sign2 = HKDataUpUtil.sign(r, addUrl, headers);
                    String sign3 = SignUtil.sign(r, HttpMethod.POST, addUrl, headers, null, null, null);
                    LogUntil.e("sign1=" + sign1);
                    LogUntil.e("sign2=" + sign2);
                    LogUntil.e("sign3=" + sign3);
                    return 0;
                })
                .compose(ThreadTransformer())
                .subscribe(r -> {

                }, e -> {
                    hideDialog();
                    e.printStackTrace();
                });
    }

    private void queryID() {
        showDia("查询分组。。。");
        Observable.just(0)
                .map(r -> {
                    CourtGroupBean groupBean = new CourtGroupBean();
                    groupBean.setGroupType("visitor");
                    groupBean.setName(courtName + courtId);
                    String body = RetrofitManager.get().getGson().toJson(groupBean);
                    MyFileUtil.saveLog(LogUntil.e("查询法院分组ID数据:" + body));
                    String result = ArtemisHttpUtil.doPostStringArtemis(getMapPath(Tools.getInstance().getScheme(path), queryID), body, null, null, contentType, null);// post请求application/json类型参数
                    MyFileUtil.saveLog(LogUntil.e("查询法院分组ID结果:" + result));
                    return result;
                })
                .compose(ThreadTransformer())
                .subscribe(r -> {
                    hideDialog();
                    showResultMsg("查询法院分组ID" + r);
                    checkID(r);
                }, e -> {
                    hideDialog();
                    e.printStackTrace();
                });
    }

    private void checkID(String data) {
        try {
            QueryIdBean queryIdBean = RetrofitManager.get().getGson().fromJson(data, QueryIdBean.class);
            if (queryIdBean.getCode().equals("0")) {
                List<QueryIdBean.DataDTO> list = queryIdBean.getData();
                if (list != null) {
                    if (list.size() >= 1) {
                        QueryIdBean.DataDTO dataDTO = list.get(0);
                        this.groupId = dataDTO.getGroupId();
                        return;
                    }
                }
            }
            addGroup();
        } catch (Exception e) {
            e.printStackTrace();
            showResultMsg(e.getLocalizedMessage());
            addGroup();
        }

    }

    private void addGroup() {
        showDia("添加分组。。。");
        Observable.just(0)
                .map(r -> {
                    CourtGroupBean groupBean = new CourtGroupBean();
                    groupBean.setGroupType("visitor");
                    groupBean.setName(courtName + courtId);
                    groupBean.setCourtNo(courtId);
                    String body = RetrofitManager.get().getGson().toJson(groupBean);
                    MyFileUtil.saveLog(LogUntil.e("添加分组数据:" + body));
                    String result = ArtemisHttpUtil.doPostStringArtemis(getMapPath(Tools.getInstance().getScheme(path), addUrl), body, null, null, contentType, null);// post请求application/json类型参数
                    MyFileUtil.saveLog(LogUntil.e("添加分组结果:" + result));
                    return result;
                })
                .compose(ThreadTransformer())
                .subscribe(r -> {
                    hideDialog();
                    showResultMsg(r);
                    getGroupID(r);
                }, e -> {
                    hideDialog();
                    e.printStackTrace();
                });
    }

    private void getGroupID(String r) {
        try {
            GroupIdBean groupIdBean = RetrofitManager.get().getGson().fromJson(r, GroupIdBean.class);
            if (groupIdBean.getCode().equals("0")) {
                this.groupId = groupIdBean.getData().getGroupId();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showResultMsg(e.getLocalizedMessage());
        }

    }


    private void upVisitor() {
        showDia("上传测试数据。。。");
        Observable.just(0)
                .map(r -> {
                    String body = RetrofitManager.get().getGson().toJson(getVisitorBean(groupId));
                    MyFileUtil.saveLog(LogUntil.e("上传访客数据:" + body));
                    String result = ArtemisHttpUtil.doPostStringArtemis(getMapPath(Tools.getInstance().getScheme(path), registerUrl), body, null, null, contentType, null);// post请求application/json类型参数
                    MyFileUtil.saveLog(LogUntil.e("数据上传结果:" + result));
                    return result;
                })
                .compose(ThreadTransformer())
                .subscribe(r -> {
                    hideDialog();
                    showResultMsg(r);
                }, e -> {
                    hideDialog();
                    e.printStackTrace();
                });
    }

    private VisitorBean getVisitorBean(String groupId) {
        VisitorBean bean = new VisitorBean();
        bean.setName("张红叶");
        bean.setGender("2");
        bean.setCertificateType("111");
        bean.setCertificateNumber("130423198811184328");
        String base64 = bmpToString("test.jpg");
        String base642 = bmpToString("takeBmp.png");
        bean.setFacePhotoUrl(base642);
        bean.setCardPhotoUrl(base64);
        bean.setRole("other");
        bean.setGroupId(groupId);
        bean.setPhone("19928345873");
        bean.setNation(1);
        bean.setAddress("河北省邯郸市临漳县称勾镇称勾东村复兴路25号");
        bean.setMatchResult(0);
        bean.setVisitReason("测试");
        String devId = AppUtils.getInstance().getCPUSerial(this);
        bean.setDeviceIndexCode(devId);
        bean.setChannelIndexCode(devId + "123456");
        bean.setPersonType(5);
        return bean;
    }

    /**
     * bitmapToBase64  DEFAULT
     */
    private String bmpToString(String name) {
        Bitmap bitmap = getImageFromAssetsFile(name);
        if (bitmap == null) {
            return "";
        }
        ByteArrayOutputStream b = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, b);
        byte[] bb = b.toByteArray();// 转为byte数组
        return Base64.encodeToString(bb, Base64.NO_WRAP);
    }


    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


    private Map<String, String> getMapPath(String type, String url) {
        Map<String, String> map = new HashMap<>();
        map.put(type, url);
        return map;
    }

    private void showResultMsg(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        String mag = tvResult.getText().toString();
        StringBuilder data = new StringBuilder();
        if (!TextUtils.isEmpty(mag)) {
            data.append(mag).append("\n").append(str);
        } else {
            data.append(str);
        }
        tvResult.setText(data.toString());
    }
}
