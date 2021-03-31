package com.officego.commonlib.common.model.owner;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2021/3/30
 **/
public class ChatBuildingBean {

    /**
     * houseId : 10948
     * btype : 1
     * maxSinglePrice : 22
     * buildingId : 7778
     * buildingName : 大西洋楼盘
     * minSinglePrice : 18
     * stationNames : ["上海火车站","汉中路","中兴路"]
     * district : 静安-静安寺
     * mainPic : https://img.officego.com/building/1597409807986.png?x-oss-process=style/small
     * stationline : ["1,3,4","12,13","8"]
     * nearbySubwayTime : ["4","7","14"]
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
        @SerializedName("btype")
        private int btype;
        @SerializedName("buildingId")
        private int buildingId;
        @SerializedName("buildingName")
        private String buildingName;
        @SerializedName("minSinglePrice")
        private Object minSinglePrice;
        @SerializedName("district")
        private String district;
        @SerializedName("address")
        private String address;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("stationNames")
        private List<String> stationNames;
        @SerializedName("stationline")
        private List<String> stationline;
        @SerializedName("nearbySubwayTime")
        private List<String> nearbySubwayTime;

        public int getBtype() {
            return btype;
        }

        public void setBtype(int btype) {
            this.btype = btype;
        }

        public int getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(int buildingId) {
            this.buildingId = buildingId;
        }

        public String getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
        }

        public Object getMinSinglePrice() {
            return minSinglePrice;
        }

        public void setMinSinglePrice(Object minSinglePrice) {
            this.minSinglePrice = minSinglePrice;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getMainPic() {
            return mainPic;
        }

        public void setMainPic(String mainPic) {
            this.mainPic = mainPic;
        }

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}
