package com.officego.commonlib.common;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public class VersionBean {

    /**
     * ver : 1.0.2  //最新版本号
     * uploadUrl : https://
     * force : false
     * updateTime : 0
     * desc : 请更新到最新版本获取更好体验哟~
     */

    @SerializedName("ver")
    private String ver;
    @SerializedName("uploadUrl")
    private String uploadUrl;
    @SerializedName("force")
    private boolean force;
    @SerializedName("updateTime")
    private int updateTime;
    @SerializedName("desc")
    private String desc;

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public int getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(int updateTime) {
        this.updateTime = updateTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
