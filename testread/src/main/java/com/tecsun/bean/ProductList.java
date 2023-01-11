package com.tecsun.bean;

import java.util.List;

public class ProductList {

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
        private List<ListDTO> list;
        private MetaDTO meta;

        public List<ListDTO> getList() {
            return list;
        }

        public void setList(List<ListDTO> list) {
            this.list = list;
        }

        public MetaDTO getMeta() {
            return meta;
        }

        public void setMeta(MetaDTO meta) {
            this.meta = meta;
        }

        public static class MetaDTO {
            private Integer limit;
            private Integer offset;
            private Integer total;

            public Integer getLimit() {
                return limit;
            }

            public void setLimit(Integer limit) {
                this.limit = limit;
            }

            public Integer getOffset() {
                return offset;
            }

            public void setOffset(Integer offset) {
                this.offset = offset;
            }

            public Integer getTotal() {
                return total;
            }

            public void setTotal(Integer total) {
                this.total = total;
            }
        }

        public static class ListDTO {
            private Integer data_type;
            private String desc;
            private Integer device_number;
            private String name;
            private String network;
            private Integer node_type;
            private String product_id;
            private Integer protocol;
            private Integer type;

            public Integer getData_type() {
                return data_type;
            }

            public void setData_type(Integer data_type) {
                this.data_type = data_type;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public Integer getDevice_number() {
                return device_number;
            }

            public void setDevice_number(Integer device_number) {
                this.device_number = device_number;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNetwork() {
                return network;
            }

            public void setNetwork(String network) {
                this.network = network;
            }

            public Integer getNode_type() {
                return node_type;
            }

            public void setNode_type(Integer node_type) {
                this.node_type = node_type;
            }

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public Integer getProtocol() {
                return protocol;
            }

            public void setProtocol(Integer protocol) {
                this.protocol = protocol;
            }

            public Integer getType() {
                return type;
            }

            public void setType(Integer type) {
                this.type = type;
            }
        }
    }
}
