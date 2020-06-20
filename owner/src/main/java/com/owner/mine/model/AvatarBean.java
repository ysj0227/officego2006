package com.owner.mine.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YangShiJie
 * Data 2020/6/9.
 * Descriptions:
 **/
public class AvatarBean {

    /**
     * avatar : https://img.officego.com.cn/user/1591672848646.png
     */

    @SerializedName("avatar")
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
