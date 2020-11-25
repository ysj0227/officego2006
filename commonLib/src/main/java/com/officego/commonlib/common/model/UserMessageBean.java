package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by YangShiJie
 * Data 2020/6/19.
 * Descriptions:
 **/
public class UserMessageBean implements Serializable {

    /**
     * avatar : https://img.officego.com.cn/user/1589287552580.png
     * auditStatus : 0   // -1从来没有认证过0待审核1审核通过2审核未通过
     * isUserInfo : true
     * nickname :
     * sex :
     * company :
     * job :
     * wxId :
     * roleType :
     */

    @SerializedName("avatar")
    private String avatar;
    @SerializedName("auditStatus")
    private int auditStatus;
    @SerializedName("isUserInfo")
    private boolean isUserInfo;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("sex")
    private String sex;
    @SerializedName("company")
    private String company;
    @SerializedName("job")
    private String job;
    @SerializedName("wxId")
    private String wxId;
    @SerializedName("roleType")
    private String roleType;
    @SerializedName("phone")
    private String phone;
    @SerializedName("isNickname")
    private boolean isNickname;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public boolean isIsUserInfo() {
        return isUserInfo;
    }

    public void setIsUserInfo(boolean isUserInfo) {
        this.isUserInfo = isUserInfo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isNickname() {
        return isNickname;
    }

    public void setNickname(boolean nickname) {
        isNickname = nickname;
    }
}
