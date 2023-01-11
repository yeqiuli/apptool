package com.tecsun.bean;

public class DevInfo {

    public DevInfo(String product_id, String device_name) {
        this.product_id = product_id;
        this.device_name = device_name;
    }

    private String product_id;
    private String device_name;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }
}
