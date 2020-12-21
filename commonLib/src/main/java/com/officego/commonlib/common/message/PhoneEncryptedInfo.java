package com.officego.commonlib.common.message;

import android.os.Parcel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.nio.charset.StandardCharsets;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by shijie
 * Date 2020/12/21
 * 自定义消息的实体类 手机号加密
 */
@MessageTag(value = "og:phoneEncrypted", flag = MessageTag.ISPERSISTED | MessageTag.ISCOUNTED)
public class PhoneEncryptedInfo extends MessageContent {


    private String content;


    public static PhoneEncryptedInfo setData(String content) {
        PhoneEncryptedInfo info = new PhoneEncryptedInfo();
        info.content = content;
        return info;
    }

    @Override
    public byte[] encode() {
        JSONObject object = new JSONObject();
        object.put("content", content);
        return object.toString().getBytes(StandardCharsets.UTF_8);
    }

    public PhoneEncryptedInfo() {
    }

    public PhoneEncryptedInfo(byte[] data) {
        super(data);
        String jsonStr = null;
        try {
            jsonStr = new String(data, StandardCharsets.UTF_8);
            JSONObject object = JSON.parseObject(jsonStr);
            setContent(object.getString("content"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, content);
    }

    public static final Creator<PhoneEncryptedInfo> CREATOR = new Creator<PhoneEncryptedInfo>() {

        @Override
        public PhoneEncryptedInfo createFromParcel(Parcel source) {
            return new PhoneEncryptedInfo(source);
        }

        @Override
        public PhoneEncryptedInfo[] newArray(int size) {
            return new PhoneEncryptedInfo[size];
        }
    };


    public PhoneEncryptedInfo(Parcel parcel) {
        content = ParcelUtils.readFromParcel(parcel);
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

}