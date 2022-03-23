package com.hgy.bean;

import com.google.gson.JsonArray;

public class DeptBean {

    private JsonArray children;
    private String name;
    private String rid;

    public JsonArray getChildren() {
        return children;
    }

    public void setChildren(JsonArray children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }
}
