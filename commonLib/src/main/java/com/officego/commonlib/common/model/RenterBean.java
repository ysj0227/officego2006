package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YangShiJie
 * Data 2020/6/12.
 * Descriptions:
 **/
public class RenterBean {

    /**
     * id : 152
     * renterId : 88
     * proprietorId : 84
     * buildingId : 45
     * time : 1593275280
     * auditStatus : 0
     * renterName : 用户3333
     * proprietorName : 田小瘦
     * renterRemark : 测试0529
     * proprietorRemark : null
     * renterType : 0
     * proprietorType : 1
     * status : 1
     * createUser : 用户3333
     * updateUser : 用户3333
     * createTime : 1590684193
     * updateTime : 1590684193
     */

    @SerializedName("id")
    private int id;
    @SerializedName("renterId")
    private int renterId;
    @SerializedName("proprietorId")
    private int proprietorId;
    @SerializedName("buildingId")
    private int buildingId;
    @SerializedName("time")
    private int time;
    @SerializedName("auditStatus")
    private int auditStatus;
    @SerializedName("renterName")
    private String renterName;
    @SerializedName("proprietorName")
    private String proprietorName;
    @SerializedName("renterRemark")
    private String renterRemark;
    @SerializedName("proprietorRemark")
    private Object proprietorRemark;
    @SerializedName("renterType")
    private int renterType;
    @SerializedName("proprietorType")
    private int proprietorType;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRenterId() {
        return renterId;
    }

    public void setRenterId(int renterId) {
        this.renterId = renterId;
    }

    public int getProprietorId() {
        return proprietorId;
    }

    public void setProprietorId(int proprietorId) {
        this.proprietorId = proprietorId;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getRenterName() {
        return renterName;
    }

    public void setRenterName(String renterName) {
        this.renterName = renterName;
    }

    public String getProprietorName() {
        return proprietorName;
    }

    public void setProprietorName(String proprietorName) {
        this.proprietorName = proprietorName;
    }

    public String getRenterRemark() {
        return renterRemark;
    }

    public void setRenterRemark(String renterRemark) {
        this.renterRemark = renterRemark;
    }

    public Object getProprietorRemark() {
        return proprietorRemark;
    }

    public void setProprietorRemark(Object proprietorRemark) {
        this.proprietorRemark = proprietorRemark;
    }

    public int getRenterType() {
        return renterType;
    }

    public void setRenterType(int renterType) {
        this.renterType = renterType;
    }

    public int getProprietorType() {
        return proprietorType;
    }

    public void setProprietorType(int proprietorType) {
        this.proprietorType = proprietorType;
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
}
