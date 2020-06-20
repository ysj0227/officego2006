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
 * 电话同意或拒绝
 */
@MessageTag(value = "og:exmobile", flag = MessageTag.ISPERSISTED | MessageTag.ISCOUNTED)
public class EcPhoneStatusInfo extends MessageContent {


    private String content;
    private String sendNumber;//（发送者的手机号）
    private String receiveNumber;//（接受者的手机号）
    private String extraMessage;
    private boolean isAgree;


    public static EcPhoneStatusInfo setPhoneData(String content, String sendNumber, String receiveNumber,
                                                 String extraMessage, boolean isAgree) {
        EcPhoneStatusInfo info = new EcPhoneStatusInfo();
        info.content = content;
        info.sendNumber = sendNumber;
        info.receiveNumber = receiveNumber;
        info.extraMessage = extraMessage;
        info.isAgree = isAgree;
        return info;
    }

    @Override
    public byte[] encode() {
        JSONObject object = new JSONObject();
        object.put("content", content);
        object.put("sendNumber", sendNumber);
        object.put("receiveNumber", receiveNumber);
        object.put("extraMessage", extraMessage);
        object.put("isAgree", isAgree);
        return object.toString().getBytes(StandardCharsets.UTF_8);
    }

    public EcPhoneStatusInfo() {
    }

    public EcPhoneStatusInfo(byte[] data) {
        super(data);
        String jsonStr = null;
        try {
            jsonStr = new String(data, StandardCharsets.UTF_8);
            JSONObject object = JSON.parseObject(jsonStr);
            setContent(object.getString("content"));
            setExtraMessage(object.getString("extraMessage"));
            setAgree(object.getBoolean("isAgree"));
            setSendNumber(object.getString("sendNumber"));
            setReceiveNumber(object.getString("receiveNumber"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, content);
        ParcelUtils.writeToParcel(dest, sendNumber);
        ParcelUtils.writeToParcel(dest, receiveNumber);
        ParcelUtils.writeToParcel(dest, extraMessage);
        ParcelUtils.writeToParcel(dest, isAgree ? 1 : 0);
    }

    public static final Creator<EcPhoneStatusInfo> CREATOR = new Creator<EcPhoneStatusInfo>() {

        @Override
        public EcPhoneStatusInfo createFromParcel(Parcel source) {
            return new EcPhoneStatusInfo(source);
        }

        @Override
        public EcPhoneStatusInfo[] newArray(int size) {
            return new EcPhoneStatusInfo[size];
        }
    };


    public EcPhoneStatusInfo(Parcel parcel) {
        content = ParcelUtils.readFromParcel(parcel);
        sendNumber = ParcelUtils.readFromParcel(parcel);
        receiveNumber = ParcelUtils.readFromParcel(parcel);
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

    public String getSendNumber() {
        return sendNumber;
    }

    public void setSendNumber(String sendNumber) {
        this.sendNumber = sendNumber;
    }

    public String getReceiveNumber() {
        return receiveNumber;
    }

    public void setReceiveNumber(String receiveNumber) {
        this.receiveNumber = receiveNumber;
    }
}