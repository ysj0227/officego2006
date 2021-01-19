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
@MessageTag(value = "og:ecphonewarn", flag = MessageTag.ISPERSISTED | MessageTag.ISCOUNTED)
public class EcPhoneWarnInfo extends MessageContent {
    private String content;

    public static EcPhoneWarnInfo setPhoneData(String name) {
        EcPhoneWarnInfo info = new EcPhoneWarnInfo();
        info.content = name;
        return info;
    }

    @Override
    public byte[] encode() {
        JSONObject object = new JSONObject();
        object.put("content", content);
        return object.toString().getBytes(StandardCharsets.UTF_8);
    }

    public EcPhoneWarnInfo() {
    }

    public EcPhoneWarnInfo(byte[] data) {
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

    public static final Creator<EcPhoneWarnInfo> CREATOR = new Creator<EcPhoneWarnInfo>() {

        @Override
        public EcPhoneWarnInfo createFromParcel(Parcel source) {
            return new EcPhoneWarnInfo(source);
        }

        @Override
        public EcPhoneWarnInfo[] newArray(int size) {
            return new EcPhoneWarnInfo[size];
        }
    };

    public EcPhoneWarnInfo(Parcel parcel) {
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