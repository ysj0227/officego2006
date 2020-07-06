package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YangShiJie
 * Data 2020/6/11.
 * Descriptions:
 **/
public class ChatsBean {
    @SerializedName("multiOwner")
    private int multiOwner;
    @SerializedName("targetId")
    private String targetId;

    public int getMultiOwner() {
        return multiOwner;
    }

    public void setMultiOwner(int multiOwner) {
        this.multiOwner = multiOwner;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

}
