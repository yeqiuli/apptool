package com.tecsun.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "devList")
public class SqlDevInfo {
    @PrimaryKey(autoGenerate = true)//主键是否自动增长，默认为false
    private long id;//ID
    private String productId;//产品ID
    private String devName;//设备名称
    private String devKey;//设备秘钥

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getDevKey() {
        return devKey;
    }

    public void setDevKey(String devKey) {
        this.devKey = devKey;
    }

    @Override
    public String toString() {
        return "设备名称：" + devName + " 设备秘钥：" + devKey;
    }
}
