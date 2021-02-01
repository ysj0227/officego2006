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
 * 弥补下拉刷新无法获取历史记录消息
 */
@MessageTag(value = "og:insertlocal", flag = MessageTag.ISPERSISTED | MessageTag.ISCOUNTED)
public class InsertLocalInfo extends MessageContent {
    private String content;

    public static InsertLocalInfo setLocalData(String name) {
        InsertLocalInfo info = new InsertLocalInfo();
        info.content = name;
        return info;
    }

    @Override
    public byte[] encode() {
        JSONObject object = new JSONObject();
        object.put("content", content);
        return object.toString().getBytes(StandardCharsets.UTF_8);
    }

    public InsertLocalInfo() {
    }

    public InsertLocalInfo(byte[] data) {
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

    public static final Creator<InsertLocalInfo> CREATOR = new Creator<InsertLocalInfo>() {

        @Override
        public InsertLocalInfo createFromParcel(Parcel source) {
            return new InsertLocalInfo(source);
        }

        @Override
        public InsertLocalInfo[] newArray(int size) {
            return new InsertLocalInfo[size];
        }
    };

    public InsertLocalInfo(Parcel parcel) {
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