package com.tecsun.testread;

import static com.tecsun.network.network.BaseHelper.ThreadTransformer;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cmiot.onenet.studio.mqtt.MqttClient;
import com.cmiot.onenet.studio.mqtt.MqttClientCallback;
import com.tecsun.bean.DevBackInfo;
import com.tecsun.bean.DevInfo;
import com.tecsun.bean.ProductList;
import com.tecsun.bean.SqlDevInfo;
import com.tecsun.mqtt.MqttClientUtilsImpl;
import com.tecsun.network.aty.BaseAty;
import com.tecsun.network.network.CallBack;
import com.tecsun.network.network.HttpExceptionBean;
import com.tecsun.network.network.ResultBean;
import com.tecsun.network.network.RetrofitManager;
import com.tecsun.network.utils.LogUntil;
import com.tecsun.sqlite.RoomDataUtils;
import com.tecsun.utils.OneNetToken;
import com.tecsun.utils.PermissionTool;

import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;

public class LtDevAty extends BaseAty {
    private boolean isCheck = false;
    private Spinner spProductId;
    private TextView tvResult;
    private EditText edDevNum;
    private List<MqttClient> mqttClients;


    private String[] getPermissionList() {
        return new String[]{
                Manifest.permission.READ_PHONE_STATE
        };
    }

    @Override
    protected int getLayout() {
        return R.layout.aty_lt_dev;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (PermissionTool.checkPermission(this, getPermissionList())) {
            createFile();
        }
        mqttClients = new ArrayList<>();
        spProductId = findViewById(R.id.spProductId);
        edDevNum = findViewById(R.id.edDevNum);
        tvResult = findViewById(R.id.tvResult);

        Button btPlatformTest = findViewById(R.id.bt_platform_test);
        String token = getToken();
        btPlatformTest.setOnClickListener(view -> {
            String devInfo = spProductId.getSelectedItem().toString();
            if (TextUtils.isEmpty(devInfo)) {
                Toast.makeText(LtDevAty.this, "请在平台添加产品和项目", Toast.LENGTH_LONG).show();
                return;
            }
            String num = edDevNum.getText().toString();
            if (TextUtils.isEmpty(num)) {
                Toast.makeText(LtDevAty.this, "请输入要创建的设备数量", Toast.LENGTH_LONG).show();
                return;
            }
            toCreateDev(devInfo, Integer.parseInt(num), token);
        });
        findViewById(R.id.btDel).setOnClickListener(view -> delDev());

        initData(token);
    }

    private void delDev() {
        showLoad("正在删除设备。。。");
        Observable.just(0)
                .map(r -> {
                    RoomDataUtils.getInstance().getDev().deleteAll();
                    return r;
                })
                .compose(ThreadTransformer())
                .subscribe(r -> hideLoad(), e -> {
                    hideLoad();
                    e.printStackTrace();
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isCheck) {
            if (PermissionTool.checkPermission(this, getPermissionList())) {
                createFile();
            }
            isCheck = false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionTool.requestCheckPermissionResult(LtDevAty.this, requestCode,
                permissions, grantResults, getPermissionList(), new PermissionTool.PermissionResult() {
                    @Override
                    public void isStartActivity(boolean start) {
                        isCheck = start;
                    }

                    @Override
                    public void getAllPermissionSuccess(boolean success) {
                        if (success) {
                            createFile();
                        }
                    }
                });
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void createFile() {
        LogUntil.e("权限授权");
    }


    private void initData(String token) {
        RetrofitManager.create()
                .needLife(false)
                .setUrl("http://openapi.heclouds.com/application")
                .addParam("action", "QueryProductList")
                .addParam("version", 1)
                .addParam("project_id", "yYKZLN")
                .addParam("offset", 0)
                .addParam("limit", 100)
                .addHeader("authorization", token)
                .queryGet(ProductList.class, new CallBack<ProductList>() {
                    @Override
                    public void onSuccess(ProductList productList) {
                        if (productList.getSuccess()) {
                            String[] strList = getStrList(productList);
                            if (strList == null) {
                                return;
                            }
                            ArrayAdapter<String> proAdapter = new ArrayAdapter<>(LtDevAty.this, R.layout.lay_spinner_w, strList);
                            proAdapter.setDropDownViewResource(R.layout.lay_spinner);
                            spProductId.setAdapter(proAdapter);
                        }

                    }

                    @Override
                    public void onFailure(HttpExceptionBean exceptionBean) {

                    }
                });
    }

    private String[] getStrList(ProductList productList) {
        List<ProductList.DataDTO.ListDTO> list = productList.getData().getList();
        if (list != null) {
            if (list.size() > 0) {
                List<String> stringList = new ArrayList<>();
                for (ProductList.DataDTO.ListDTO dto : list) {
                    stringList.add(dto.getName().replaceAll("-", "") + "-" + dto.getProduct_id());
                }
                String[] strList = new String[stringList.size()];
                return stringList.toArray(strList);
            }
        }
        return null;
    }


    private String getToken() {
        try {
            String version = "2020-05-29";
            String resourceName = "userid/303364";
            String expirationTime = System.currentTimeMillis() / 1000 + 100 * 24 * 60 * 60 + "";
            String signatureMethod = OneNetToken.SignatureMethod.MD5.name().toLowerCase();
            String accessKey = "32nhazggphDnr7CUfOTMWlEOTD8qjuxLnLaezywSCQEe/d8jyYI+CtiLk8zpDirkjkw4RwvgouQi9fdojZWwew==";
            String token = OneNetToken.assembleToken(version, resourceName, expirationTime, signatureMethod, accessKey);
            LogUntil.e("Authorization:" + token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void toCreateDev(String devInfo, int num, String token) {
        showLoad("正在创建设备。。。");
        Observable.just(devInfo)
                .map(r -> {
                    String[] strings = r.split("-");
                    String productId = strings[1];
                    List<SqlDevInfo> list = RoomDataUtils.getInstance().getDev().getDevByProductId(productId);
                    int devNum = list.size();
                    if (devNum >= num) {
                        return list;
                    } else {
                        int count = num - devNum;
                        List<SqlDevInfo> createList = new ArrayList<>();
                        for (int i = 0; i < count; i++) {
                            try {
                                Thread.sleep(1200);
                                String devName = getDevName();
                                ResultBean<DevBackInfo> resultBean = RetrofitManager.create()
                                        .needLife(false)
                                        .setUrl("http://openapi.heclouds.com/common")
                                        .addParam("action", "CreateDevice")
                                        .addParam("version", 1)
                                        .addHeader("authorization", token)
                                        .executePostBody(new DevInfo(productId, devName), DevBackInfo.class);
                                if (resultBean.getCode() == 200) {
                                    DevBackInfo info = resultBean.getT();
                                    if (info.getSuccess()) {
                                        DevBackInfo.DataDTO dataDTO = info.getData();
                                        SqlDevInfo sqlDevInfo = new SqlDevInfo();
                                        sqlDevInfo.setProductId(productId);
                                        sqlDevInfo.setDevName(dataDTO.getName());
                                        sqlDevInfo.setDevKey(dataDTO.getSec_key());
                                        long id = RoomDataUtils.getInstance().getDev().insert(sqlDevInfo);
                                        LogUntil.e("添加设备：" + id);
                                        showMsgResult("添加设备：" + devName + " 成功");
                                        createList.add(sqlDevInfo);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return createList;
                    }

                })
                .compose(ThreadTransformer())
                .subscribe(r -> {
                    hideLoad();
                    if (r.size() > 0) {
                        toConnect(r);
                        return;
                    }
                    LogUntil.e("失败");
                }, e -> {
                    hideLoad();
                    e.printStackTrace();
                    LogUntil.e("失败");
                });

    }

    private String getDevName() {
        String devName = java.util.UUID.randomUUID().toString().replace("-", "");
        SqlDevInfo devInfo = RoomDataUtils.getInstance().getDev().findByDevName(devName);
        if (devInfo == null) {
            return devName;
        }
        return getDevName();
    }


    private void toConnect(List<SqlDevInfo> list) {
        showLoad("设备注册。。。");
        Observable.just(0)
                .delay(1200, TimeUnit.MILLISECONDS)
                .map(a -> {
                    for (SqlDevInfo r : list) {
                        try {
                            Thread.sleep(1200);
                            showMsgResult("延迟1200毫秒连接设备");
                            MqttClient mqttClient = MqttClientUtilsImpl.getImpl().connect(r.getDevName(), r.getProductId(), r.getDevKey(), new MqttClientCallback() {

                                @Override
                                public void onConnected(String serverUrl) {
                                    showMsgResult(r.toString() + " 连接成功");
                                }

                                @Override
                                public void onConnectFailed(String serverUrl, Throwable throwable) {
                                    if (throwable != null) {
                                        throwable.printStackTrace();
                                        if (throwable instanceof MqttSecurityException) {
                                            if (((MqttSecurityException) throwable).getReasonCode() == 4) {
                                                showMsgResult(r.toString() + " 连接失败 删除设备");
                                                delOneDev(r);
                                                return;
                                            }
                                        }
                                    }
                                    showMsgResult(r.toString() + " 连接失败");
                                }

                                @Override
                                public void onConnectionLost(Throwable throwable) {
                                    showMsgResult("连接断开:" + throwable.getMessage());
                                }

                                @Override
                                public void onReceiveMessage(final String topic, final byte[] payload) {
                                    // 平台下发到设备的原始数据，在 MqttClientHelper.onReceiveMessage() 方法中根据topic的不同进行分发
                                    // 因为回调都是异步执行在子线程中，所以需要回到主线程更新 UI
                                    LogUntil.e("onReceiveMessage");
                                    String msg = new String(payload);
                                    String backTopic = MqttClientUtilsImpl.getImpl().topics.propertyPostReply();
                                    String dataStr = topic + " " + msg + " " + backTopic;
                                    showMsgResult(dataStr);
                                }
                            });
//                            mqttClients.add(mqttClient);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return a;
                })
                .compose(ThreadTransformer())
                .subscribe(r -> {
                    hideLoad();

                }, e -> {
                    hideLoad();
                    e.printStackTrace();
                    LogUntil.e("失败");
                });

    }

    private void delOneDev(SqlDevInfo sqlDevInfo) {
        Observable.just(sqlDevInfo)
                .map(r -> {
                    RoomDataUtils.getInstance().getDev().deleteByDevName(r.getDevName());
                    return r;
                })
                .compose(ThreadTransformer())
                .subscribe(r -> hideLoad(), e -> {
                    hideLoad();
                    e.printStackTrace();
                });
    }

    private void showMsgResult(String msg) {
        runOnUiThread(() -> {
            LogUntil.e(msg);
            String str = tvResult.getText().toString();
            StringBuilder stringBuilder = new StringBuilder();
            if (TextUtils.isEmpty(str)) {
                stringBuilder.append(msg);
            } else {
                stringBuilder.append(msg).append("\n").append(str);
            }
            tvResult.setText(stringBuilder.toString());
        });

    }

}