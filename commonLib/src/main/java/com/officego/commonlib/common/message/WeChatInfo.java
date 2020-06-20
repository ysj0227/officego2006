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
 */
@MessageTag(value = "og:wx", flag = MessageTag.ISPERSISTED | MessageTag.ISCOUNTED)
public class WeChatInfo extends MessageContent {


    private String content;
    private String number;
    private String extraMessage;


    public static WeChatInfo setPhoneData(String name, String number, String extraMessage) {
        WeChatInfo info = new WeChatInfo();
        info.content = name;
        info.number = number;
        info.extraMessage = extraMessage;
        return info;
    }

    @Override
    public byte[] encode() {
        JSONObject object = new JSONObject();
        object.put("content", content);
        object.put("number", number);
        object.put("extraMessage", extraMessage);
        return object.toString().getBytes(StandardCharsets.UTF_8);
    }

    public WeChatInfo() {
    }

    public WeChatInfo(byte[] data) {
        super(data);
        String jsonStr = null;
        try {
            jsonStr = new String(data, StandardCharsets.UTF_8);
            JSONObject object = JSON.parseObject(jsonStr);
            setContent(object.getString("content"));
            setNumber(object.getString("number"));
            setExtraMessage(object.getString("extraMessage"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, content);
        ParcelUtils.writeToParcel(dest, extraMessage);
        ParcelUtils.writeToParcel(dest, number);
    }

    public static final Creator<WeChatInfo> CREATOR = new Creator<WeChatInfo>() {

        @Override
        public WeChatInfo createFromParcel(Parcel source) {
            return new WeChatInfo(source);
        }

        @Override
        public WeChatInfo[] newArray(int size) {
            return new WeChatInfo[size];
        }
    };


    public WeChatInfo(Parcel parcel) {
        content = ParcelUtils.readFromParcel(parcel);
        extraMessage = ParcelUtils.readFromParcel(parcel);
        number = ParcelUtils.readFromParcel(parcel);
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}