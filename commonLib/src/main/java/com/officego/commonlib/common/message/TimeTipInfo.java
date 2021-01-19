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
 * 时间提示
 */
@MessageTag(value = "og:timetip", flag = MessageTag.ISPERSISTED | MessageTag.ISCOUNTED)
public class TimeTipInfo extends MessageContent {


    private String content;


    public static TimeTipInfo setData(String content) {
        TimeTipInfo info = new TimeTipInfo();
        info.content = content;
        return info;
    }

    @Override
    public byte[] encode() {
        JSONObject object = new JSONObject();
        object.put("content", content);
        return object.toString().getBytes(StandardCharsets.UTF_8);
    }

    public TimeTipInfo() {
    }

    public TimeTipInfo(byte[] data) {
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

    public static final Creator<TimeTipInfo> CREATOR = new Creator<TimeTipInfo>() {

        @Override
        public TimeTipInfo createFromParcel(Parcel source) {
            return new TimeTipInfo(source);
        }

        @Override
        public TimeTipInfo[] newArray(int size) {
            return new TimeTipInfo[size];
        }
    };


    public TimeTipInfo(Parcel parcel) {
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