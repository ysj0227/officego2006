package com.owner.mine.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by YangShiJie
 * Data 2020/6/19.
 * Descriptions:
 **/
public class UserOwnerBean implements Serializable {


    /**
     * address : TEST地址
     * sex : null
     * remark : 客户端申请认证
     * proprietorCompany : uuj
     * isPersonal : 0
     * avatar : https://img.officego.com/user/1592552695546.jpg
     * wxId : 哼哼唧唧
     * realname : 5366f33
     * accountStatus : 0
     * leaseType : 1
     * trip : 3
     * proprietorRealname : 5366f33
     * identityType :    "identityType": 0,//身份类型0个人1企业2联合
     * phone : 18516765366
     * proprietorJob : 在
     * authority : 1
     * auditStatus :  0待审核1审核通过2审核未通过
     * isEnterprise : 0
     * company :
     * job :
     * favorite : 13
     */

    @SerializedName("address")
    private String address;
    @SerializedName("sex")
    private Object sex;
    @SerializedName("remark")
    private String remark;
    @SerializedName("proprietorCompany")
    private String proprietorCompany;
    @SerializedName("isPersonal")
    private int isPersonal;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("wxId")
    private String wxId;
    @SerializedName("realname")
    private String realname;
    @SerializedName("accountStatus")
    private int accountStatus;
    @SerializedName("leaseType")
    private int leaseType;
    @SerializedName("trip")
    private int trip;
    @SerializedName("proprietorRealname")
    private String proprietorRealname;
    @SerializedName("identityType")
    private int identityType;
    @SerializedName("phone")
    private String phone;
    @SerializedName("proprietorJob")
    private String proprietorJob;
    @SerializedName("authority")
    private int authority;
    @SerializedName("auditStatus")
    private int auditStatus;
    @SerializedName("isEnterprise")
    private int isEnterprise;
    @SerializedName("company")
    private String company;
    @SerializedName("job")
    private String job;
    @SerializedName("favorite")
    private int favorite;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getSex() {
        return sex;
    }

    public void setSex(Object sex) {
        this.sex = sex;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProprietorCompany() {
        return proprietorCompany;
    }

    public void setProprietorCompany(String proprietorCompany) {
        this.proprietorCompany = proprietorCompany;
    }

    public int getIsPersonal() {
        return isPersonal;
    }

    public void setIsPersonal(int isPersonal) {
        this.isPersonal = isPersonal;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public int getLeaseType() {
        return leaseType;
    }

    public void setLeaseType(int leaseType) {
        this.leaseType = leaseType;
    }

    public int getTrip() {
        return trip;
    }

    public void setTrip(int trip) {
        this.trip = trip;
    }

    public String getProprietorRealname() {
        return proprietorRealname;
    }

    public void setProprietorRealname(String proprietorRealname) {
        this.proprietorRealname = proprietorRealname;
    }

    public int getIdentityType() {
        return identityType;
    }

    public void setIdentityType(int identityType) {
        this.identityType = identityType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProprietorJob() {
        return proprietorJob;
    }

    public void setProprietorJob(String proprietorJob) {
        this.proprietorJob = proprietorJob;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public int getIsEnterprise() {
        return isEnterprise;
    }

    public void setIsEnterprise(int isEnterprise) {
        this.isEnterprise = isEnterprise;
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

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}
