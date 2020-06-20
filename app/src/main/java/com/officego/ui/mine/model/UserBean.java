package com.officego.ui.mine.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public class UserBean implements Serializable {
    /**
     * userId : 105
     * nickname : 用户8964
     * realname : 用户8964
     * openId : null
     * unionId : null
     * avatar : https://img.officego.com.cn/head.png
     * job : null
     * sex : null
     * birthday : null
     * password : 88888888
     * phone : 15981968964
     * address : null
     * idCard : null
     * idFront : null
     * idBack : null
     * businessLicense : null
     * creditNo : null
     * businessCard : null
     * workCard : null
     * personalType : null
     * proprietorRealname : 用户8964
     * proprietorJob : null
     * proprietorCompany : null
     * company : null
     * personalVerify : null
     * companyVerify : null
     * watchSchedule : null
     * watchHistory : null
     * secret : null
     * isVip : null
     * roleType : 0
     * source : 2
     * channel : 2
     * imei : 867698034787526
     * status : 1
     * createUser : 15981968964
     * updateUser : 15981968964
     * createTime : 1590992920
     * updateTime : 1590992920
     * wxId : null
     */

    @SerializedName("userId")
    private int userId;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("realname")
    private String realname;
    @SerializedName("openId")
    private Object openId;
    @SerializedName("unionId")
    private Object unionId;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("job")
    private Object job;
    @SerializedName("sex")
    private Object sex;
    @SerializedName("birthday")
    private Object birthday;
    @SerializedName("password")
    private String password;
    @SerializedName("phone")
    private String phone;
    @SerializedName("address")
    private Object address;
    @SerializedName("idCard")
    private Object idCard;
    @SerializedName("idFront")
    private Object idFront;
    @SerializedName("idBack")
    private Object idBack;
    @SerializedName("businessLicense")
    private Object businessLicense;
    @SerializedName("creditNo")
    private Object creditNo;
    @SerializedName("businessCard")
    private Object businessCard;
    @SerializedName("workCard")
    private Object workCard;
    @SerializedName("personalType")
    private Object personalType;
    @SerializedName("proprietorRealname")
    private String proprietorRealname;
    @SerializedName("proprietorJob")
    private Object proprietorJob;
    @SerializedName("proprietorCompany")
    private Object proprietorCompany;
    @SerializedName("company")
    private Object company;
    @SerializedName("personalVerify")
    private Object personalVerify;
    @SerializedName("companyVerify")
    private Object companyVerify;
    @SerializedName("watchSchedule")
    private Object watchSchedule;
    @SerializedName("watchHistory")
    private Object watchHistory;
    @SerializedName("secret")
    private Object secret;
    @SerializedName("isVip")
    private Object isVip;
    @SerializedName("roleType")
    private int roleType;
    @SerializedName("source")
    private int source;
    @SerializedName("channel")
    private int channel;
    @SerializedName("imei")
    private String imei;
    @SerializedName("status")
    private int status;
    @SerializedName("createUser")
    private String createUser;
    @SerializedName("updateUser")
    private String updateUser;
    @SerializedName("createTime")
    private int createTime;
    @SerializedName("updateTime")
    private int updateTime;
    @SerializedName("wxId")
    private Object wxId;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public Object getOpenId() {
        return openId;
    }

    public void setOpenId(Object openId) {
        this.openId = openId;
    }

    public Object getUnionId() {
        return unionId;
    }

    public void setUnionId(Object unionId) {
        this.unionId = unionId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Object getJob() {
        return job;
    }

    public void setJob(Object job) {
        this.job = job;
    }

    public Object getSex() {
        return sex;
    }

    public void setSex(Object sex) {
        this.sex = sex;
    }

    public Object getBirthday() {
        return birthday;
    }

    public void setBirthday(Object birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getIdCard() {
        return idCard;
    }

    public void setIdCard(Object idCard) {
        this.idCard = idCard;
    }

    public Object getIdFront() {
        return idFront;
    }

    public void setIdFront(Object idFront) {
        this.idFront = idFront;
    }

    public Object getIdBack() {
        return idBack;
    }

    public void setIdBack(Object idBack) {
        this.idBack = idBack;
    }

    public Object getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(Object businessLicense) {
        this.businessLicense = businessLicense;
    }

    public Object getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(Object creditNo) {
        this.creditNo = creditNo;
    }

    public Object getBusinessCard() {
        return businessCard;
    }

    public void setBusinessCard(Object businessCard) {
        this.businessCard = businessCard;
    }

    public Object getWorkCard() {
        return workCard;
    }

    public void setWorkCard(Object workCard) {
        this.workCard = workCard;
    }

    public Object getPersonalType() {
        return personalType;
    }

    public void setPersonalType(Object personalType) {
        this.personalType = personalType;
    }

    public String getProprietorRealname() {
        return proprietorRealname;
    }

    public void setProprietorRealname(String proprietorRealname) {
        this.proprietorRealname = proprietorRealname;
    }

    public Object getProprietorJob() {
        return proprietorJob;
    }

    public void setProprietorJob(Object proprietorJob) {
        this.proprietorJob = proprietorJob;
    }

    public Object getProprietorCompany() {
        return proprietorCompany;
    }

    public void setProprietorCompany(Object proprietorCompany) {
        this.proprietorCompany = proprietorCompany;
    }

    public Object getCompany() {
        return company;
    }

    public void setCompany(Object company) {
        this.company = company;
    }

    public Object getPersonalVerify() {
        return personalVerify;
    }

    public void setPersonalVerify(Object personalVerify) {
        this.personalVerify = personalVerify;
    }

    public Object getCompanyVerify() {
        return companyVerify;
    }

    public void setCompanyVerify(Object companyVerify) {
        this.companyVerify = companyVerify;
    }

    public Object getWatchSchedule() {
        return watchSchedule;
    }

    public void setWatchSchedule(Object watchSchedule) {
        this.watchSchedule = watchSchedule;
    }

    public Object getWatchHistory() {
        return watchHistory;
    }

    public void setWatchHistory(Object watchHistory) {
        this.watchHistory = watchHistory;
    }

    public Object getSecret() {
        return secret;
    }

    public void setSecret(Object secret) {
        this.secret = secret;
    }

    public Object getIsVip() {
        return isVip;
    }

    public void setIsVip(Object isVip) {
        this.isVip = isVip;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public Object getWxId() {
        return wxId;
    }

    public void setWxId(Object wxId) {
        this.wxId = wxId;
    }
}
