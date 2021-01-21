package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shijie
 * Date 2021/1/20
 **/
public class RCloudPushBean {
    @SerializedName("rongyuntoken")
    private String rongyuntoken;
    @SerializedName("nickName")
    private String nickName;
    @SerializedName("avatar")
    private String avatar;

    public String getRongyuntoken() {
        return rongyuntoken;
    }

    public void setRongyuntoken(String rongyuntoken) {
        this.rongyuntoken = rongyuntoken;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
