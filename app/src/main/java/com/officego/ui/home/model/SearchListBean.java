package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class SearchListBean {

    @SerializedName("data")
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bid : 3
         * buildingName : 新百安大厦2
         * address : 新百安大厦2
         * district : <strong style='color:red'>浦</strong><strong style='color:red'>东</strong>新区
         * business : 外高桥
         * dayPrice : 3
         * buildType : 1   1:楼盘,2:网点
         */

        @SerializedName("bid")
        private int bid;
        @SerializedName("buildingName")
        private String buildingName;
        @SerializedName("address")
        private String address;
        @SerializedName("district")
        private Object district;
        @SerializedName("business")
        private Object business;
        @SerializedName("dayPrice")
        private Object dayPrice;
        @SerializedName("buildType")
        private int buildType;

        public int getBid() {
            return bid;
        }

        public void setBid(int bid) {
            this.bid = bid;
        }

        public String getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getDistrict() {
            return district;
        }

        public void setDistrict(Object district) {
            this.district = district;
        }

        public Object getBusiness() {
            return business;
        }

        public void setBusiness(Object business) {
            this.business = business;
        }

        public Object getDayPrice() {
            return dayPrice;
        }

        public void setDayPrice(Object dayPrice) {
            this.dayPrice = dayPrice;
        }

        public int getBuildType() {
            return buildType;
        }

        public void setBuildType(int buildType) {
            this.buildType = buildType;
        }
    }
}
