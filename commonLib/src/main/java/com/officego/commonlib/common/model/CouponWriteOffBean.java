package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shijie
 * Date 2020/12/10
 **/
public class CouponWriteOffBean {

    /**
     * updateTime : 2020.12.10 10:12:45
     */

    @SerializedName("updateTime")
    private String updateTime;

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
