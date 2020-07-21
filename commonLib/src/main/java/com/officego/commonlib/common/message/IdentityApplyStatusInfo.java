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
 * 自定义消息的实体类
 * 认证同意或拒绝
 */
@MessageTag(value = "og:identityapplystatus", flag = MessageTag.ISPERSISTED | MessageTag.ISCOUNTED)
public class IdentityApplyStatusInfo extends MessageContent {

    private int id;
    private String content;
    private String extraMessage;
    private boolean isAgree;


    public static IdentityApplyStatusInfo setPhoneData( int id,String content,
                                                       String extraMessage, boolean isAgree) {
        IdentityApplyStatusInfo info = new IdentityApplyStatusInfo();
        info.content = content;
        info.id = id;
        info.extraMessage = extraMessage;
        info.isAgree = isAgree;
        return info;
    }

    @Override
    public byte[] encode() {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("content", content);
        object.put("extraMessage", extraMessage);
        object.put("isAgree", isAgree);
        return object.toString().getBytes(StandardCharsets.UTF_8);
    }

    public IdentityApplyStatusInfo() {
    }

    public IdentityApplyStatusInfo(byte[] data) {
        super(data);
        String jsonStr = null;
        try {
            jsonStr = new String(data, StandardCharsets.UTF_8);
            JSONObject object = JSON.parseObject(jsonStr);
            setContent(object.getString("content"));
            setExtraMessage(object.getString("extraMessage"));
            setAgree(object.getBoolean("isAgree"));
            setId(object.getInteger("sendNumber"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, content);
        ParcelUtils.writeToParcel(dest, id);
        ParcelUtils.writeToParcel(dest, extraMessage);
        ParcelUtils.writeToParcel(dest, isAgree ? 1 : 0);
    }

    public static final Creator<IdentityApplyStatusInfo> CREATOR = new Creator<IdentityApplyStatusInfo>() {

        @Override
        public IdentityApplyStatusInfo createFromParcel(Parcel source) {
            return new IdentityApplyStatusInfo(source);
        }

        @Override
        public IdentityApplyStatusInfo[] newArray(int size) {
            return new IdentityApplyStatusInfo[size];
        }
    };


    public IdentityApplyStatusInfo(Parcel parcel) {
        content = ParcelUtils.readFromParcel(parcel);
        id = ParcelUtils.readIntFromParcel(parcel);
        extraMessage = ParcelUtils.readFromParcel(parcel);
        isAgree = ParcelUtils.readIntFromParcel(parcel) == 1;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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