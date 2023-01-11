package com.hgy.bean;

public class JsDataInfo {
    private String charset;
    private Object json;
    private String sign_type;
    private String sign;

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Object getJson() {
        return json;
    }

    public void setJson(Object json) {
        this.json = json;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String toString1() {
        return "charset=" + charset + "&" +
                "json=" + json + "&" +
                "sign_type=" + sign_type;
    }

    public String toString2() {
        return "charset=" + charset + "&" +
                "json=" + json + "&" +
                "sign_type=" + sign_type + "&" +
                "sign=" + sign;
    }
}
