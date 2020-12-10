package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/10
 **/
public class CouponDetailsBean {

    /**
     * offline : 1609407811
     * discountMax : 300
     * phone : 15673185528
     * couponType : 3
     * batchCode : 859F7588CB5D472B
     * online : 1606902208
     * id : 19
     * batchTitle : 减至券啊
     * batchId : 20201202174338
     * shelfLife : 2020.12.31 - 2020.12.02
     * buildingMeetingroomList : [{"title":"梦想加会议室(建科网B)","roomId":6,"buildingId":7719}]
     */

    @SerializedName("offline")
    private int offline;
    @SerializedName("discountMax")
    private String discountMax;
    @SerializedName("phone")
    private String phone;
    @SerializedName("couponType")
    private int couponType;
    @SerializedName("batchCode")
    private String batchCode;
    @SerializedName("online")
    private int online;
    @SerializedName("id")
    private int id;
    @SerializedName("batchTitle")
    private String batchTitle;
    @SerializedName("batchId")
    private String batchId;
    @SerializedName("shelfLife")
    private String shelfLife;
    @SerializedName("buildingMeetingroomList")
    private List<BuildingMeetingroomListBean> buildingMeetingroomList;

    public int getOffline() {
        return offline;
    }

    public void setOffline(int offline) {
        this.offline = offline;
    }

    public String getDiscountMax() {
        return discountMax;
    }

    public void setDiscountMax(String discountMax) {
        this.discountMax = discountMax;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCouponType() {
        return couponType;
    }

    public void setCouponType(int couponType) {
        this.couponType = couponType;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBatchTitle() {
        return batchTitle;
    }

    public void setBatchTitle(String batchTitle) {
        this.batchTitle = batchTitle;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        this.shelfLife = shelfLife;
    }

    public List<BuildingMeetingroomListBean> getBuildingMeetingroomList() {
        return buildingMeetingroomList;
    }

    public void setBuildingMeetingroomList(List<BuildingMeetingroomListBean> buildingMeetingroomList) {
        this.buildingMeetingroomList = buildingMeetingroomList;
    }

    public static class BuildingMeetingroomListBean {
        /**
         * title : 梦想加会议室(建科网B)
         * roomId : 6
         * buildingId : 7719
         */

        @SerializedName("title")
        private String title;
        @SerializedName("roomId")
        private int roomId;
        @SerializedName("buildingId")
        private int buildingId;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public int getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(int buildingId) {
            this.buildingId = buildingId;
        }
    }
}
