package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YangShiJie
 * Data 2020/6/11.
 * Descriptions:
 **/
public class FirstChatBean {


    /**
     * isChat : 0
     */

    @SerializedName("isChat")
    private int isChat;

    public int getIsChat() {
        return isChat;
    }

    public void setIsChat(int isChat) {
        this.isChat = isChat;
    }
}
