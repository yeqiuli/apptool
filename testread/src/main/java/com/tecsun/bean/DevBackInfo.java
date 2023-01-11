package com.tecsun.bean;

public class DevBackInfo {

    private DataDTO data;
    private String requestId;
    private Boolean success;

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public static class DataDTO {
        private String ct;
        private String desc;
        private String name;
        private Integer node_type;
        private Integer pt;
        private String sec_key;

        public String getCt() {
            return ct;
        }

        public void setCt(String ct) {
            this.ct = ct;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getNode_type() {
            return node_type;
        }

        public void setNode_type(Integer node_type) {
            this.node_type = node_type;
        }

        public Integer getPt() {
            return pt;
        }

        public void setPt(Integer pt) {
            this.pt = pt;
        }

        public String getSec_key() {
            return sec_key;
        }

        public void setSec_key(String sec_key) {
            this.sec_key = sec_key;
        }

    }
}
