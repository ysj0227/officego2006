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
@MessageTag(value = "og:fy", flag = MessageTag.ISPERSISTED | MessageTag.ISCOUNTED)
public class ViewingDateInfo extends MessageContent {

    private String time;
    private String content;
    private String extraMessage;
    private int fyId;//预约看房id
    private String buildingName;//大楼名称
    private String buildingAddress;//大楼地址

    public static ViewingDateInfo setViewingDateData(String time, String content,
                                                     String extraMessage, int id,
                                                     String buildingName, String buildingAddress) {
        ViewingDateInfo info = new ViewingDateInfo();
        info.time = time;
        info.content = content;
        info.extraMessage = extraMessage;
        info.fyId = id;
        info.buildingName = buildingName;
        info.buildingAddress = buildingAddress;
        return info;
    }

    @Override
    public byte[] encode() {
        JSONObject object = new JSONObject();
        object.put("time", time);
        object.put("content", content);
        object.put("extraMessage", extraMessage);
        object.put("fyId", fyId);
        object.put("buildingName", buildingName);
        object.put("buildingAddress", buildingAddress);
        return object.toString().getBytes(StandardCharsets.UTF_8);
    }

    public ViewingDateInfo() {
    }

    public ViewingDateInfo(byte[] data) {
        super(data);
        String jsonStr = null;
        try {
            jsonStr = new String(data, StandardCharsets.UTF_8);
            JSONObject object = JSON.parseObject(jsonStr);
            setTime(object.getString("time"));
            setContent(object.getString("content"));
            setExtraMessage(object.getString("extraMessage"));
            setId(object.getInteger("fyId"));
            setBuildingName(object.getString("buildingName"));
            setBuildingAddress(object.getString("buildingAddress"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, time);
        ParcelUtils.writeToParcel(dest, content);
        ParcelUtils.writeToParcel(dest, extraMessage);
        ParcelUtils.writeToParcel(dest, fyId);
        ParcelUtils.writeToParcel(dest, buildingName);
        ParcelUtils.writeToParcel(dest, buildingAddress);
    }

    public static final Creator<ViewingDateInfo> CREATOR = new Creator<ViewingDateInfo>() {

        @Override
        public ViewingDateInfo createFromParcel(Parcel source) {
            return new ViewingDateInfo(source);
        }

        @Override
        public ViewingDateInfo[] newArray(int size) {
            return new ViewingDateInfo[size];
        }
    };


    public ViewingDateInfo(Parcel parcel) {
        time = ParcelUtils.readFromParcel(parcel);
        content = ParcelUtils.readFromParcel(parcel);
        extraMessage = ParcelUtils.readFromParcel(parcel);
        fyId = ParcelUtils.readIntFromParcel(parcel);
        buildingName = ParcelUtils.readFromParcel(parcel);
        buildingAddress = ParcelUtils.readFromParcel(parcel);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
        return fyId;
    }

    public void setId(int id) {
        this.fyId = id;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }
}