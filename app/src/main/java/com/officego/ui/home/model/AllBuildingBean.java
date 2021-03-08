package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2021/3/5
 **/
public class AllBuildingBean {

    /**
     * address : 闸北广中路696号
     * btype : 1
     * latitude : 31.278628
     * districts : 静安
     * areas : 其他
     * buildingName : 楼盘名字
     * minDayPrice : 1.0
     * branchesName : 网点名字
     * mainPic : https://img.officego.com/building/1594276006926.jpg?x-oss-process=style/small
     * houseCount : 9
     * maxDayPrice : 1.0
     * id : 54
     * longitude : 121.459955
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
        @SerializedName("address")
        private String address;
        @SerializedName("btype")
        private int btype;
        @SerializedName("latitude")
        private String latitude;
        @SerializedName("districts")
        private String districts;
        @SerializedName("areas")
        private String areas;
        @SerializedName("buildingName")
        private String buildingName;
        @SerializedName("minDayPrice")
        private String minDayPrice;
        @SerializedName("branchesName")
        private String branchesName;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("houseCount")
        private String houseCount;
        @SerializedName("maxDayPrice")
        private String maxDayPrice;
        @SerializedName("id")
        private int id;
        @SerializedName("longitude")
        private String longitude;
        @SerializedName("buildingMap")
        private BuildingMapBean buildingMap;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getBtype() {
            return btype;
        }

        public void setBtype(int btype) {
            this.btype = btype;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getDistricts() {
            return districts;
        }

        public void setDistricts(String districts) {
            this.districts = districts;
        }

        public String getAreas() {
            return areas;
        }

        public void setAreas(String areas) {
            this.areas = areas;
        }

        public String getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
        }

        public String getMinDayPrice() {
            return minDayPrice;
        }

        public void setMinDayPrice(String minDayPrice) {
            this.minDayPrice = minDayPrice;
        }

        public String getBranchesName() {
            return branchesName;
        }

        public void setBranchesName(String branchesName) {
            this.branchesName = branchesName;
        }

        public String getMainPic() {
            return mainPic;
        }

        public void setMainPic(String mainPic) {
            this.mainPic = mainPic;
        }

        public String getHouseCount() {
            return houseCount;
        }

        public void setHouseCount(String houseCount) {
            this.houseCount = houseCount;
        }

        public String getMaxDayPrice() {
            return maxDayPrice;
        }

        public void setMaxDayPrice(String maxDayPrice) {
            this.maxDayPrice = maxDayPrice;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public BuildingMapBean getBuildingMap() {
            return buildingMap;
        }

        public void setBuildingMap(BuildingMapBean buildingMap) {
            this.buildingMap = buildingMap;
        }

        public static class BuildingMapBean {
            @SerializedName("stationNames")
            private List<String> stationNames;

            public List<String> getStationNames() {
                return stationNames;
            }

            public void setStationNames(List<String> stationNames) {
                this.stationNames = stationNames;
            }
        }
    }
}
