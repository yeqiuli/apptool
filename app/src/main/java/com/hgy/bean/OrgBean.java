package com.hgy.bean;

import com.google.gson.JsonObject;

public class OrgBean {
    /**
     * {
     * "success": true,
     * "traceId": "7726910362189824",
     * "errorCode": "0",
     * "errorMessage": "ok",
     * "data": {
     * "children": [],
     * "name": "测试团队002",
     * "rid": "7634433249198080"
     * }
     * }
     */

    private Boolean success;
    private String traceId;
    private String errorCode;
    private String errorMessage;
    private JsonObject data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

}
