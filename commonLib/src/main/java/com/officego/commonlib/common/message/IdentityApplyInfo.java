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
@MessageTag(value = "og:identityapply", flag = MessageTag.ISPERSISTED | MessageTag.ISCOUNTED)
public class IdentityApplyInfo extends MessageContent {
    private String content;
    private String extraMessage;
    private int id;

    public static IdentityApplyInfo setIdentityApplyInfo(int id, String content,
                                                       String extraMessage) {
        IdentityApplyInfo info = new IdentityApplyInfo();
        info.id = id;
        info.content = content;
        info.extraMessage = extraMessage;
        return info;
    }

    @Override
    public byte[] encode() {
        JSONObject object = new JSONObject();
        object.put("content", content);
        object.put("extraMessage", extraMessage);
        object.put("id", id);
        return object.toString().getBytes(StandardCharsets.UTF_8);
    }

    public IdentityApplyInfo() {
    }

    public IdentityApplyInfo(byte[] data) {
        super(data);
        String jsonStr = null;
        try {
            jsonStr = new String(data, StandardCharsets.UTF_8);
            JSONObject object = JSON.parseObject(jsonStr);
            setContent(object.getString("content"));
            setExtraMessage(object.getString("extraMessage"));
            setId(object.getInteger("id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, content);
        ParcelUtils.writeToParcel(dest, extraMessage);
        ParcelUtils.writeToParcel(dest, id);
    }

    public static final Creator<IdentityApplyInfo> CREATOR = new Creator<IdentityApplyInfo>() {

        @Override
        public IdentityApplyInfo createFromParcel(Parcel source) {
            return new IdentityApplyInfo(source);
        }

        @Override
        public IdentityApplyInfo[] newArray(int size) {
            return new IdentityApplyInfo[size];
        }
    };


    public IdentityApplyInfo(Parcel parcel) {
        content = ParcelUtils.readFromParcel(parcel);
        extraMessage = ParcelUtils.readFromParcel(parcel);
        id = ParcelUtils.readIntFromParcel(parcel);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}