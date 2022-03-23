package com.hgy.controller;


import com.google.gson.JsonObject;
import com.hgy.bean.PwdCheck;
import com.tecsun.network.network.RetrofitManager;
import com.tecsun.network.utils.AppUtils;
import com.tecsun.network.utils.LogUntil;
import com.yanzhenjie.andserver.annotation.CrossOrigin;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.PostMapping;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.util.MediaType;

/**
 * 设备控制
 */
@CrossOrigin
@RestController
@RequestMapping("/device")
public class DevicesController {

    public static final String result = "{\"result\":1,\"success\":ture}";
    /**
     * 1、设备重启
     *
     * @return String
     */
    @PostMapping(path = "/reboot", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String rebootDev() {
        AppUtils.getInstance().reboot();
        return getAndBean(ErrCode.SUCCESS, "发送重启命令生效，将再1秒后重启", null, true);
    }


    @GetMapping(path = "/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getDevID() {
        return getAndBean(ErrCode.SUCCESS, "获取成功设备序列号成功", new JsonObject(), true);
    }




    /**
     * 1、 数据接收
     *
     * @param deviceKey        设备序列号
     * @param personId         人员ID
     * @param time             识别记录时间戳
     * @param type             数据类型
     * @param path             照片路径
     * @param imgBase64        照片
     * @param data             身份证数据
     * @param ip               设备IP
     * @param searchScore      识别比分
     * @param livenessScore    活体比分
     * @param temperature      人员温度测量值
     * @param standard         设置的体温异常值
     * @param temperatureState 体温状态:1正常2异常
     * @return result
     */
    @PostMapping(path = "/data/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String dataUp(@RequestParam(name = "deviceKey", required = false) String deviceKey,
                         @RequestParam(name = "personId", required = false) String personId,
                         @RequestParam(name = "time", required = false) String time,
                         @RequestParam(name = "type", required = false) String type,
                         @RequestParam(name = "path", required = false) String path,
                         @RequestParam(name = "imgBase64", required = false) String imgBase64,
                         @RequestParam(name = "data", required = false) String data,
                         @RequestParam(name = "ip", required = false) String ip,
                         @RequestParam(name = "searchScore", required = false) String searchScore,
                         @RequestParam(name = "livenessScore", required = false) String livenessScore,
                         @RequestParam(name = "temperature", required = false) String temperature,
                         @RequestParam(name = "standard", required = false) String standard,
                         @RequestParam(name = "temperatureState", required = false) String temperatureState) {
        try {
            LogUntil.e("deviceKey" + deviceKey);
            LogUntil.e("personId" + personId);
            LogUntil.e(data);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    /**
     * 返回对象构建
     *
     * @param code 结果码
     * @param msg  信息
     * @param data 数据
     * @return String
     */
    public String getAndBean(int code, String msg, Object data, boolean flag) {
        PwdCheck andBean = new PwdCheck();
        andBean.setCode(code);
        andBean.setSuccess(flag);
        andBean.setMsg(msg);
        andBean.setObj(data == null ? new JsonObject() : data);
        return RetrofitManager.get().getGson().toJson(andBean);
    }

}
