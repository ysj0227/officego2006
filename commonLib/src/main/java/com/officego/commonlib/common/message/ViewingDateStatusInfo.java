package com.officego.commonlib.common.message;

/**
 * Created by YangShiJie
 * Data 2020/5/26.
 * Descriptions:
 **/

import android.os.Parcel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.nio.charset.StandardCharsets;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * 自定义消息的实体类 预约看房
 */
@MessageTag(value = "og:exfy", flag = MessageTag.ISPERSISTED | MessageTag.ISCOUNTED)
public class ViewingDateStatusInfo extends MessageContent {

    private String content;
    private String extraMessage;
    private boolean isAgree;

    public static ViewingDateStatusInfo setViewingDateStatusData(String id, String name, String num, boolean isAgree) {
        ViewingDateStatusInfo info = new ViewingDateStatusInfo();
        info.content = name;
        info.extraMessage = num;
        info.isAgree = isAgree;
        return info;
    }

    @Override
    public byte[] encode() {
        JSONObject object = new JSONObject();
        object.put("content", content);
        object.put("extraMessage", extraMessage);
        object.put("isAgree", isAgree);
        return object.toString().getBytes(StandardCharsets.UTF_8);
    }

    public ViewingDateStatusInfo() {
    }

    public ViewingDateStatusInfo(byte[] data) {
        super(data);
        String jsonStr = null;
        try {
            jsonStr = new String(data, StandardCharsets.UTF_8);
            JSONObject object = JSON.parseObject(jsonStr);
            setContent(object.getString("content"));
            setExtraMessage(object.getString("extraMessage"));
            setAgree(object.getBoolean("isAgree"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, content);
        ParcelUtils.writeToParcel(dest, extraMessage);
        ParcelUtils.writeToParcel(dest, isAgree ? 1 : 0);
    }

    public static final Creator<ViewingDateStatusInfo> CREATOR = new Creator<ViewingDateStatusInfo>() {

        @Override
        public ViewingDateStatusInfo createFromParcel(Parcel source) {
            return new ViewingDateStatusInfo(source);
        }

        @Override
        public ViewingDateStatusInfo[] newArray(int size) {
            return new ViewingDateStatusInfo[size];
        }
    };


    public ViewingDateStatusInfo(Parcel parcel) {
        content = ParcelUtils.readFromParcel(parcel);
        extraMessage = ParcelUtils.readFromParcel(parcel);
        isAgree = ParcelUtils.readIntFromParcel(parcel) == 1;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtraMessage() {
        return extraMessage;
    }

    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }

    public boolean isAgree() {
        return isAgree;
    }

    public void setAgree(boolean agree) {
        isAgree = agree;
    }
}