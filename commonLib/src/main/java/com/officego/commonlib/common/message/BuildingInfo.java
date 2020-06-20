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
    private boolean isFavorite;
    private String minSinglePrice;//单价
    private String district;//商圈
    private String routeMap;//线路
    private String tags;//特色
    private String imgUrl;//大楼图片

    public static BuildingInfo setBuildingData(String buildingName,
                                               String distance,
                                               String createTime,
                                               boolean isFavorite,
                                               String minSinglePrice,
                                               String district,
                                               String routeMap,
                                               String tags,
                                               String imgUrl
    ) {
        BuildingInfo info = new BuildingInfo();
        info.buildingName = buildingName;
        info.distance = distance;
        info.createTime = createTime;
        info.isFavorite = isFavorite;
        info.minSinglePrice = minSinglePrice;
        info.district = district;
        info.routeMap = routeMap;
        info.tags = tags;
        info.imgUrl=imgUrl;
        return info;
    }

    @Override
    public byte[] encode() {
        JSONObject object = new JSONObject();
        object.put("buildingName", buildingName);
        object.put("distance", distance);
        object.put("createTime", createTime);
        object.put("isFavorite", isFavorite);
        object.put("minSinglePrice", minSinglePrice);
        object.put("district", district);
        object.put("routeMap", routeMap);
        object.put("tags", tags);
        object.put("imgUrl", imgUrl);
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
            setFavorite(object.getBoolean("isFavorite"));
            setCreateTime(object.getString("createTime"));
            setMinSinglePrice(object.getString("minSinglePrice"));
            setDistrict(object.getString("district"));
            setRouteMap(object.getString("routeMap"));
            setTags(object.getString("tags"));
            setImgUrl(object.getString("imgUrl"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, buildingName);
        ParcelUtils.writeToParcel(dest, distance);
        ParcelUtils.writeToParcel(dest, createTime);
        ParcelUtils.writeToParcel(dest, isFavorite ? 1 : 0);
        ParcelUtils.writeToParcel(dest, minSinglePrice);
        ParcelUtils.writeToParcel(dest, district);
        ParcelUtils.writeToParcel(dest, routeMap);
        ParcelUtils.writeToParcel(dest, tags);
        ParcelUtils.writeToParcel(dest, imgUrl);
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
        isFavorite = ParcelUtils.readIntFromParcel(parcel) == 1;
        minSinglePrice = ParcelUtils.readFromParcel(parcel);
        district = ParcelUtils.readFromParcel(parcel);
        routeMap = ParcelUtils.readFromParcel(parcel);
        tags = ParcelUtils.readFromParcel(parcel);
        imgUrl = ParcelUtils.readFromParcel(parcel);
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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
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
}
