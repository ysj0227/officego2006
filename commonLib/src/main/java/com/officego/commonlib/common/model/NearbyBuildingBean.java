package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2021/3/16
 **/
public class NearbyBuildingBean {


    /**
     * releaseTime : 0
     * distance :
     * passengerLift : 4
     * btype : 1
     * remark :
     * buildingMap : {"stationNames":["鞍山新村","临平路","四平路"],"stationline":["8","4","10"],"nearbySubwayTime":["10","11","13"]}
     * independenceOffice : 0
     * maxArea : 432.69
     * minSeats :
     * mainPic : https://img.officego.com/building/1596098760079.jpg?x-oss-process=style/small
     * houseCount : 1
     * id : 7458
     * minArea : 432.69
     * seatMonthPrice : 0.0
     * address : 大连路990号
     * maxSeats :
     * updateTime : 1602324529
     * userId : 0
     * businessDistrict : 杨浦-大连路
     * infotext : 房东最近活跃过
     * createTime : 1596097966
     * minDayPrice : 4.5
     * name : 创邑SPACE | 海上海
     * totalFloor : 29
     * storeyHeight : 3.6
     * maxDayPrice : 4.5
     * openStation : 1
     * vr : 0
     * officeType : 0,2
     */

    @SerializedName("data")
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        @SerializedName("releaseTime")
        private String releaseTime;
        @SerializedName("distance")
        private String distance;
        @SerializedName("passengerLift")
        private String passengerLift;
        @SerializedName("btype")
        private int btype;
        @SerializedName("remark")
        private String remark;
        @SerializedName("buildingMap")
        private BuildingMapBean buildingMap;
        @SerializedName("independenceOffice")
        private int independenceOffice;
        @SerializedName("maxArea")
        private String maxArea;
        @SerializedName("minSeats")
        private String minSeats;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("houseCount")
        private int houseCount;
        @SerializedName("id")
        private int id;
        @SerializedName("minArea")
        private String minArea;
        @SerializedName("seatMonthPrice")
        private String seatMonthPrice;
        @SerializedName("address")
        private String address;
        @SerializedName("maxSeats")
        private String maxSeats;
        @SerializedName("updateTime")
        private String updateTime;
        @SerializedName("userId")
        private int userId;
        @SerializedName("businessDistrict")
        private String businessDistrict;
        @SerializedName("infotext")
        private String infotext;
        @SerializedName("createTime")
        private String createTime;
        @SerializedName("minDayPrice")
        private String minDayPrice;
        @SerializedName("name")
        private String name;
        @SerializedName("totalFloor")
        private String totalFloor;
        @SerializedName("storeyHeight")
        private String storeyHeight;
        @SerializedName("maxDayPrice")
        private String maxDayPrice;
        @SerializedName("openStation")
        private int openStation;
        @SerializedName("vr")
        private String vr;
        @SerializedName("officeType")
        private String officeType;

        public String getReleaseTime() {
            return releaseTime;
        }

        public void setReleaseTime(String releaseTime) {
            this.releaseTime = releaseTime;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getPassengerLift() {
            return passengerLift;
        }

        public void setPassengerLift(String passengerLift) {
            this.passengerLift = passengerLift;
        }

        public int getBtype() {
            return btype;
        }

        public void setBtype(int btype) {
            this.btype = btype;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public BuildingMapBean getBuildingMap() {
            return buildingMap;
        }

        public void setBuildingMap(BuildingMapBean buildingMap) {
            this.buildingMap = buildingMap;
        }

        public int getIndependenceOffice() {
            return independenceOffice;
        }

        public void setIndependenceOffice(int independenceOffice) {
            this.independenceOffice = independenceOffice;
        }

        public String getMaxArea() {
            return maxArea;
        }

        public void setMaxArea(String maxArea) {
            this.maxArea = maxArea;
        }

        public String getMinSeats() {
            return minSeats;
        }

        public void setMinSeats(String minSeats) {
            this.minSeats = minSeats;
        }

        public String getMainPic() {
            return mainPic;
        }

        public void setMainPic(String mainPic) {
            this.mainPic = mainPic;
        }

        public int getHouseCount() {
            return houseCount;
        }

        public void setHouseCount(int houseCount) {
            this.houseCount = houseCount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMinArea() {
            return minArea;
        }

        public void setMinArea(String minArea) {
            this.minArea = minArea;
        }

        public String getSeatMonthPrice() {
            return seatMonthPrice;
        }

        public void setSeatMonthPrice(String seatMonthPrice) {
            this.seatMonthPrice = seatMonthPrice;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMaxSeats() {
            return maxSeats;
        }

        public void setMaxSeats(String maxSeats) {
            this.maxSeats = maxSeats;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getBusinessDistrict() {
            return businessDistrict;
        }

        public void setBusinessDistrict(String businessDistrict) {
            this.businessDistrict = businessDistrict;
        }

        public String getInfotext() {
            return infotext;
        }

        public void setInfotext(String infotext) {
            this.infotext = infotext;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getMinDayPrice() {
            return minDayPrice;
        }

        public void setMinDayPrice(String minDayPrice) {
            this.minDayPrice = minDayPrice;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTotalFloor() {
            return totalFloor;
        }

        public void setTotalFloor(String totalFloor) {
            this.totalFloor = totalFloor;
        }

        public String getStoreyHeight() {
            return storeyHeight;
        }

        public void setStoreyHeight(String storeyHeight) {
            this.storeyHeight = storeyHeight;
        }

        public String getMaxDayPrice() {
            return maxDayPrice;
        }

        public void setMaxDayPrice(String maxDayPrice) {
            this.maxDayPrice = maxDayPrice;
        }

        public int getOpenStation() {
            return openStation;
        }

        public void setOpenStation(int openStation) {
            this.openStation = openStation;
        }

        public String getVr() {
            return vr;
        }

        public void setVr(String vr) {
            this.vr = vr;
        }

        public String getOfficeType() {
            return officeType;
        }

        public void setOfficeType(String officeType) {
            this.officeType = officeType;
        }

        public static class BuildingMapBean {
            @SerializedName("stationNames")
            private List<String> stationNames;
            @SerializedName("stationline")
            private List<String> stationline;
            @SerializedName("nearbySubwayTime")
            private List<String> nearbySubwayTime;

            public List<String> getStationNames() {
                return stationNames;
            }

            public void setStationNames(List<String> stationNames) {
                this.stationNames = stationNames;
            }

            public List<String> getStationline() {
                return stationline;
            }

            public void setStationline(List<String> stationline) {
                this.stationline = stationline;
            }

            public List<String> getNearbySubwayTime() {
                return nearbySubwayTime;
            }

            public void setNearbySubwayTime(List<String> nearbySubwayTime) {
                this.nearbySubwayTime = nearbySubwayTime;
            }
        }
    }
}
