package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/9/16
 **/
public class ChatListBean {

    /**
     * buildingName : 快易名商-虹桥万通中心
     * chattedId : 4641
     * Isblock : false
     * nickname : Leo Zhong
     * IsDepartureStatus : false
     * typeId : 1
     * sort : 0
     * avatar : https://img.officego.com/head.png?x-oss-process=style/small
     */

    @SerializedName("buildingName")
    private String buildingName;
    @SerializedName("chattedId")
    private String chattedId;
    @SerializedName("Isblock")
    private boolean Isblock;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("IsDepartureStatus")
    private boolean IsDepartureStatus;
    @SerializedName("typeId")
    private int typeId;
    @SerializedName("sort")
    private int sort;
    @SerializedName("avatar")
    private String avatar;

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getChattedId() {
        return chattedId;
    }

    public void setChattedId(String chattedId) {
        this.chattedId = chattedId;
    }

    public boolean isIsblock() {
        return Isblock;
    }

    public void setIsblock(boolean Isblock) {
        this.Isblock = Isblock;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public boolean isIsDepartureStatus() {
        return IsDepartureStatus;
    }

    public void setIsDepartureStatus(boolean IsDepartureStatus) {
        this.IsDepartureStatus = IsDepartureStatus;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
