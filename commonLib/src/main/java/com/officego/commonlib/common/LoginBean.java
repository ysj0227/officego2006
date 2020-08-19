package com.officego.commonlib.common;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YangShiJie
 * Data 2020/5/27.
 * Descriptions:
 **/
public class LoginBean {

    /**
     * rongyuntoken : 1Rdv88w2vkhWoGBApvh/8ADG03ybxIih9fMM5XnpfOM=@7mb1.cn.rongnav.com;7mb1.cn.rongcfg.com
     * rid : 0  //角色id，-1：未选择角色，0：租户，1：房东
     * token : NTlfc3Vud2VsbF8xNTkwNTY4ODYxXzA=
     */

    @SerializedName("rongyuntoken")
    private String rongyuntoken;
    @SerializedName("nickName")
    private String nickName;
     @SerializedName("avatar")
    private String avatar;
    @SerializedName("rid")
    private int rid;
    @SerializedName("token")
    private String token;
    @SerializedName("uid")
    private String uid;

    public String getRongyuntoken() {
        return rongyuntoken;
    }

    public void setRongyuntoken(String rongyuntoken) {
        this.rongyuntoken = rongyuntoken;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
