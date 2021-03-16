package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2021/3/16
 **/
public class NearbyBuildingBean {

    /**
     * area : 大连路
     * buildingName : 创邑SPACE | 海上海
     * businessDistrict : 杨浦 · 大连路
     * address : 大连路990号
     * minDayPrice : 4.5
     * maxArea : 432.69
     * district : 杨浦
     * minSeats : null
     * mainPic : https://img.officego.com/building/1596098760079.jpg?x-oss-process=style/websmall
     * maxSeats : null
     * id : 7458
     * minArea : 432.69
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
        @SerializedName("area")
        private String area;
        @SerializedName("buildingName")
        private String buildingName;
        @SerializedName("businessDistrict")
        private String businessDistrict;
        @SerializedName("address")
        private String address;
        @SerializedName("minDayPrice")
        private double minDayPrice;
        @SerializedName("district")
        private String district;
        @SerializedName("minSeats")
        private Object minSeats;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("maxSeats")
        private Object maxSeats;
        @SerializedName("id")
        private int id;
        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
        }

        public String getBusinessDistrict() {
            return businessDistrict;
        }

        public void setBusinessDistrict(String businessDistrict) {
            this.businessDistrict = businessDistrict;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getMinDayPrice() {
            return minDayPrice;
        }

        public void setMinDayPrice(double minDayPrice) {
            this.minDayPrice = minDayPrice;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public Object getMinSeats() {
            return minSeats;
        }

        public void setMinSeats(Object minSeats) {
            this.minSeats = minSeats;
        }

        public String getMainPic() {
            return mainPic;
        }

        public void setMainPic(String mainPic) {
            this.mainPic = mainPic;
        }

        public Object getMaxSeats() {
            return maxSeats;
        }

        public void setMaxSeats(Object maxSeats) {
            this.maxSeats = maxSeats;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }
}
