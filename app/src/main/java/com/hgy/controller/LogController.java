package com.hgy.controller;


import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.tecsun.network.network.RetrofitManager;
import com.tecsun.sixse.devserver.bean.PwdCheck;
import com.tecsun.sixse.devserver.utils.MyFileUtil;
import com.tecsun.sixse.devserver.utils.MyTimeUtil;
import com.yanzhenjie.andserver.annotation.CrossOrigin;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.PostMapping;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.util.MediaType;

import java.io.File;
import java.util.List;

/**
 * 日志相关接口
 */
@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class LogController {
    /**
     * 1、日志查看
     *
     * @param name 日志名称（yyyy-MM-dd）
     * @return String
     */
    @GetMapping(path = "/log", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getLog(@RequestParam("name") String name) {
        if (TextUtils.isEmpty(name)) {
            return getAndBean(ErrCode.KEY_ERR, "请传入正确的日志名称", null, false);
        }
        if (!MyTimeUtil.isDate(name, MyTimeUtil.SHORT_TIME1)) {
            return getAndBean(ErrCode.KEY_ERR, "请传入正确的日志名称（yyyy-MM-dd）", null, false);
        }
        File ourRoot = new File(MyFileUtil.LOG_DIR + name + ".txt");
        if (!ourRoot.exists()) {
            return getAndBean(ErrCode.NO_LOG, "不存在日志", null, false);
        } else {
            return getAndBean(ErrCode.SUCCESS, "查询成功", MyFileUtil.readFile(ourRoot.getAbsolutePath()), true);
        }
    }

    /**
     * 2、删除日志
     *
     * @param name 日志名称 传入“-1”清空日志
     * @return JSON
     */
    @PostMapping(path = "/delLog", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String delLog(@RequestParam(name = "name") String name) {
        try {
            if (TextUtils.isEmpty(name)) {
                return getAndBean(ErrCode.KEY_ERR, "请传入正确的日志名称", null, false);
            }
            if (name.equals("-1")) {
                List<String> names = MyFileUtil.getLogName();
                MyFileUtil.deleteFiles((new File(MyFileUtil.LOG_DIR)));
                return getAndBean(ErrCode.SUCCESS, "清空日志成功", names, true);
            }
            if (!MyTimeUtil.isDate(name, MyTimeUtil.SHORT_TIME1)) {
                return getAndBean(ErrCode.KEY_ERR, "请传入正确的日志名称（yyyy-MM-dd）", null, false);
            }
            File ourRoot = new File(MyFileUtil.LOG_DIR + name + ".txt");
            if (!ourRoot.exists()) {
                return getAndBean(ErrCode.NO_LOG, "日志不存在，删除失败", null, false);
            } else {
                MyFileUtil.deleteFileForFile(ourRoot);
                return getAndBean(ErrCode.SUCCESS, "删除日志文件成功", ourRoot.getAbsolutePath(), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getAndBean(ErrCode.OTHER_ERR, e.getMessage(), null, false);
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
        andBean.setMsg(msg);
        andBean.setObj(data == null ? new JsonObject() : data);
        andBean.setSuccess(flag);
        return RetrofitManager.get().getGson().toJson(andBean);
    }

}
