package com.hgy.bean;

public class XmlHeadInfo {
    private String version;
    private String ref;
    private String sysCode;
    private String busCode;
    private String tradeSrc;
    private String sender;
    private String reciver;
    private String date;
    private String time;
    private String reSnd;
    private Rst rst;
    private String by;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getBusCode() {
        return busCode;
    }

    public void setBusCode(String busCode) {
        this.busCode = busCode;
    }

    public String getTradeSrc() {
        return tradeSrc;
    }

    public void setTradeSrc(String tradeSrc) {
        this.tradeSrc = tradeSrc;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReSnd() {
        return reSnd;
    }

    public void setReSnd(String reSnd) {
        this.reSnd = reSnd;
    }

    public Rst getRst() {
        return rst;
    }

    public void setRst(Rst rst) {
        this.rst = rst;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public static class Rst {
        private String tradeCode;
        private String busiCode;
        private String info;

        public String getTradeCode() {
            return tradeCode;
        }

        public void setTradeCode(String tradeCode) {
            this.tradeCode = tradeCode;
        }

        public String getBusiCode() {
            return busiCode;
        }

        public void setBusiCode(String busiCode) {
            this.busiCode = busiCode;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        @Override
        public String toString() {
            return "<tradeCode>" + tradeCode + "</tradeCode>" +
                    "<busiCode>" + "" + "</busiCode>" +
                    "<info>" + "" + "</info>";
        }
    }


    @Override
    public String toString() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<head>" +
                "<version>" + version + "</version>" +
                "<ref>" + ref + "</ref>" +
                "<sysCode>" + sysCode + "</sysCode>" +
                "<busCode>" + busCode + "</busCode>" +
                "<tradeSrc>" + tradeSrc + "</tradeSrc>" +
                "<sender>" + sender + "</sender>" +
                "<reciver>" + reciver + "</reciver>" +
                "<date>" + date + "</date>" +
                "<time>" + time + "</time>" +
                "<reSnd>" + reSnd + "</reSnd>" +
                "<rst>" + rst.toString() + "</rst>" +
                "<by>" + "" + "</by>" +
                "</head>";
    }
}
