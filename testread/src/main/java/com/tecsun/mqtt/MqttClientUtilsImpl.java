package com.tecsun.mqtt;


import android.text.TextUtils;

import com.cmiot.onenet.studio.mqtt.MqttClient;
import com.cmiot.onenet.studio.mqtt.MqttClientCallback;
import com.cmiot.onenet.studio.mqtt.Topics;


public class MqttClientUtilsImpl {

    public static volatile MqttClientUtilsImpl impl;
    public MqttClient client;
    public Topics topics;

    public static MqttClientUtilsImpl getImpl() {
        if (impl == null) {
            synchronized (MqttClientUtilsImpl.class) {
                if (impl == null) {
                    impl = new MqttClientUtilsImpl();
                }
            }
        }
        return impl;
    }

    public MqttClient connect(String devName, String productId, String accessKey, MqttClientCallback mCallback) {

        String mqttPath = "studio-mqtts.heclouds.com";
        int mqttPort = 1883;
        if (TextUtils.isEmpty(productId)) {
            mCallback.onConnectionLost(new Throwable("参数填写不完整"));
            return null;
        }
        topics = new Topics(productId, devName);
        client = createMqttClient(
                mqttPath,
                mqttPort,
                productId,
                accessKey,
                devName // 此处应替换为用户的设备名称
        );
        client.addCallback(mCallback);
        try {
            client.connect(); // 连接服务器
            return client;
        } catch (Exception e) {
            e.printStackTrace();
            return client;
        }
    }

    private MqttClient createMqttClient(String path, int port, String productId, String accessKey, String deviceName) {
        return new MqttClient.Builder()
                //MQTT 服务器地址
                .host(path)
                // MQTT 服务器端⼝
                .port(port)
                // 是否使用 ssl 加密通信，默认 true
                .ssl(false)
                //如果选择加密通信则要设置此项
//                .cert(FileUtils.getSSLFile())
                // 产品id（必填）
                .productId(productId)
                // 产品key或设备秘钥
                .accessKey(accessKey)
                // 设备名称（必填）
                .deviceName(deviceName)

                // 鉴权 token 超时时间，单位秒，默认 100 天
                .expireTime(100 * 24 * 60 * 60)
                // 是否自动重连
                .autoReconnect(true)
                // 连接超时时间，以秒为单位，默认15秒
                .connectionTimeout(10)
                // keep-alive 发送间隔，以秒为单位，默认120秒
                .keepAliveInterval(60)
                // 是否显示调试日志
                .showDebugLog(true)
                .build();
    }


    public void release() {
        if (client != null) {
            // 断开服务器连接
            client.disconnect();
            // 释放资源
            client.close();
        }
    }

}
