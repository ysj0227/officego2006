package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YangShiJie
 * Data 2020/6/11.
 * Descriptions:
 **/
public class ChatsBean {
    @SerializedName("targetId")
    private int targetId;
    @SerializedName("multiOwner")
    private int multiOwner;

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getMultiOwner() {
        return multiOwner;
    }

    public void setMultiOwner(int multiOwner) {
        this.multiOwner = multiOwner;
    }
}
