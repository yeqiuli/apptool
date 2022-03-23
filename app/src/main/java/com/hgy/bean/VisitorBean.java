package com.hgy.bean;

public class VisitorBean {

    private String name;//姓名
    private String gender;//性别 1男 2女 0未知
    private String certificateType;//证件类型 111身份证、112律师证、990其他证件
    private String certificateNumber;//证件号码
    private String facePhotoUrl;//人脸照片
    private String cardPhotoUrl;//证件照片
    private String role;//人员角色 lawyer律师 other其他
    private String groupId;//人脸分组ID
    private String phone;//电话
    private Integer nation;//民族
    private String address;//地址
    private Integer matchResult;//人证比对结果 0比对通过 1比对失败
    private String visitReason;//来访事由
    private String deviceIndexCode;//设备编号
    private String channelIndexCode;//通道编号
    private Integer personType;//人员类型 1立案、2信访、3开庭、4律师、5访客、6其他

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getFacePhotoUrl() {
        return facePhotoUrl;
    }

    public void setFacePhotoUrl(String facePhotoUrl) {
        this.facePhotoUrl = facePhotoUrl;
    }

    public String getCardPhotoUrl() {
        return cardPhotoUrl;
    }

    public void setCardPhotoUrl(String cardPhotoUrl) {
        this.cardPhotoUrl = cardPhotoUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getNation() {
        return nation;
    }

    public void setNation(Integer nation) {
        this.nation = nation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(Integer matchResult) {
        this.matchResult = matchResult;
    }

    public String getVisitReason() {
        return visitReason;
    }

    public void setVisitReason(String visitReason) {
        this.visitReason = visitReason;
    }

    public String getDeviceIndexCode() {
        return deviceIndexCode;
    }

    public void setDeviceIndexCode(String deviceIndexCode) {
        this.deviceIndexCode = deviceIndexCode;
    }

    public String getChannelIndexCode() {
        return channelIndexCode;
    }

    public void setChannelIndexCode(String channelIndexCode) {
        this.channelIndexCode = channelIndexCode;
    }

    public Integer getPersonType() {
        return personType;
    }

    public void setPersonType(Integer personType) {
        this.personType = personType;
    }
}
