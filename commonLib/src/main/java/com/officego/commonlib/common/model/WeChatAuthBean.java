package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shijie
 * Date 2021/1/26
 **/
public class WeChatAuthBean {


    /**
     * access_token : 41_0wirq7Yjcefr5WcqxGEgkiKtBET9lmf0W22IR0iZ3wk-WFMzc7Zm2612eYy3pVRQl5qW8hXS_BNJl80pvFy30SYn-zc
     * country : 中国
     * unionid : o0z3_vrJDSV37kWwonHe
     * province : 上海
     * city : 浦东新区
     * openid : ozLylwLEBv5YtU
     * sex : 1
     * nickname : 蓝天
     * headimgurl : https://thirdwx.qQrlt2CQNvKD2b1LxPvPf2ibiczWLpE4u6m1TyeaibjiaOnPQA/132
     * language : zh_CN
     */

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("unionid")
    private String unionid;
    @SerializedName("openid")
    private String openid;
    @SerializedName("sex")
    private Integer sex;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("headimgurl")
    private String headimgurl;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

}
