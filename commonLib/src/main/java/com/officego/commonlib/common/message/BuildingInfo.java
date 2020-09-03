package com.officego.commonlib.common.message;

import android.os.Parcel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.nio.charset.StandardCharsets;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;

/**
 * Created by YangShiJie
 * Data 2020/5/26.
 * Descriptions:
 **/
@MessageTag(value = "og:buildinginfo", flag = MessageTag.ISPERSISTED | MessageTag.ISCOUNTED)
public class BuildingInfo extends MessageContent {

    private String buildingName;
    private String distance;
    private String createTime;
    private String minSinglePrice;//单价
    private String district;//商圈
    private String routeMap;//线路
    private String tags;//特色
    private String imgUrl;//大楼图片
    private int isBuildOrHouse;//1 楼盘网点 2房源
    private int btype;
    private int buildingId;
    private int houseId;

    public static BuildingInfo setBuildingData(String buildingName,
                                               String distance,
                                               String createTime,
                                               String minSinglePrice,
                                               String district,
                                               String routeMap,
                                               String tags,
                                               String imgUrl,
                                               int isBuildOrHouse,
                                               int btype,
                                               int buildingId,
                                               int houseId
    ) {
        BuildingInfo info = new BuildingInfo();
        info.buildingName = buildingName;
        info.distance = distance;
        info.createTime = createTime;
        info.minSinglePrice = minSinglePrice;
        info.district = district;
        info.routeMap = routeMap;
        info.tags = tags;
        info.imgUrl = imgUrl;
        info.isBuildOrHouse = isBuildOrHouse;
        info.btype = btype;
        info.buildingId = buildingId;
        info.houseId = houseId;
        return info;
    }

    @Override
    public byte[] encode() {
        JSONObject object = new JSONObject();
        object.put("buildingName", buildingName);
        object.put("distance", distance);
        object.put("createTime", createTime);
        object.put("minSinglePrice", minSinglePrice);
        object.put("district", district);
        object.put("routeMap", routeMap);
        object.put("tags", tags);
        object.put("imgUrl", imgUrl);
        object.put("isBuildOrHouse", isBuildOrHouse);
        object.put("btype", btype);
        object.put("buildingId", buildingId);
        object.put("houseId", houseId);
        return object.toString().getBytes(StandardCharsets.UTF_8);
    }

    public BuildingInfo() {
    }

    public BuildingInfo(byte[] data) {
        super(data);
        String jsonStr = null;
        try {
            jsonStr = new String(data, StandardCharsets.UTF_8);
            JSONObject object = JSON.parseObject(jsonStr);
            setbuildingName(object.getString("buildingName"));
            setdistance(object.getString("distance"));
            setCreateTime(object.getString("createTime"));
            setMinSinglePrice(object.getString("minSinglePrice"));
            setDistrict(object.getString("district"));
            setRouteMap(object.getString("routeMap"));
            setTags(object.getString("tags"));
            setImgUrl(object.getString("imgUrl"));
            setIsBuildOrHouse(object.getInteger("isBuildOrHouse"));
            setBtype(object.getInteger("btype"));
            setBuildingId(object.getInteger("buildingId"));
            setHouseId(object.getInteger("houseId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, buildingName);
        ParcelUtils.writeToParcel(dest, distance);
        ParcelUtils.writeToParcel(dest, createTime);
        ParcelUtils.writeToParcel(dest, minSinglePrice);
        ParcelUtils.writeToParcel(dest, district);
        ParcelUtils.writeToParcel(dest, routeMap);
        ParcelUtils.writeToParcel(dest, tags);
        ParcelUtils.writeToParcel(dest, imgUrl);
        ParcelUtils.writeToParcel(dest, isBuildOrHouse);
        ParcelUtils.writeToParcel(dest, btype);
        ParcelUtils.writeToParcel(dest, buildingId);
        ParcelUtils.writeToParcel(dest, houseId);
    }

    public static final Creator<BuildingInfo> CREATOR = new Creator<BuildingInfo>() {

        @Override
        public BuildingInfo createFromParcel(Parcel source) {
            return new BuildingInfo(source);
        }

        @Override
        public BuildingInfo[] newArray(int size) {
            return new BuildingInfo[size];
        }
    };


    public BuildingInfo(Parcel parcel) {
        buildingName = ParcelUtils.readFromParcel(parcel);
        distance = ParcelUtils.readFromParcel(parcel);
        createTime = ParcelUtils.readFromParcel(parcel);
        minSinglePrice = ParcelUtils.readFromParcel(parcel);
        district = ParcelUtils.readFromParcel(parcel);
        routeMap = ParcelUtils.readFromParcel(parcel);
        tags = ParcelUtils.readFromParcel(parcel);
        imgUrl = ParcelUtils.readFromParcel(parcel);
        isBuildOrHouse = ParcelUtils.readIntFromParcel(parcel);
        btype = ParcelUtils.readIntFromParcel(parcel);
        buildingId = ParcelUtils.readIntFromParcel(parcel);
        houseId = ParcelUtils.readIntFromParcel(parcel);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getbuildingName() {
        return buildingName;
    }

    public void setbuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getdistance() {
        return distance;
    }

    public void setdistance(String distance) {
        this.distance = distance;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMinSinglePrice() {
        return minSinglePrice;
    }

    public void setMinSinglePrice(String minSinglePrice) {
        this.minSinglePrice = minSinglePrice;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRouteMap() {
        return routeMap;
    }

    public void setRouteMap(String routeMap) {
        this.routeMap = routeMap;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getIsBuildOrHouse() {
        return isBuildOrHouse;
    }

    public void setIsBuildOrHouse(int isBuildOrHouse) {
        this.isBuildOrHouse = isBuildOrHouse;
    }

    public int getBtype() {
        return btype;
    }

    public void setBtype(int btype) {
        this.btype = btype;
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }
}
