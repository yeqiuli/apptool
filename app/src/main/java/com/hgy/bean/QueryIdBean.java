package com.hgy.bean;

import com.google.gson.annotations.JsonAdapter;
import com.hgy.jsonAdapter.QueryIDAdapter;

import java.util.List;

public class QueryIdBean {

    private String code;
    private String msg;
    @JsonAdapter(QueryIDAdapter.class)
    private List<DataDTO> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public static class DataDTO {
        private String groupId;
        private String name;
        private String groupType;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGroupType() {
            return groupType;
        }

        public void setGroupType(String groupType) {
            this.groupType = groupType;
        }
    }
}
