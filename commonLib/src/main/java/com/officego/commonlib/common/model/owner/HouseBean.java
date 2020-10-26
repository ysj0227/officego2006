package com.officego.commonlib.common.model.owner;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/26
 **/
public class HouseBean {

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
         * area : 0
         * houseId : 17073
         * dayPrice : 0
         * houseStatus : 3
         * perfect : 44%
         * btype : 1
         * Ispermissions : true
         * updateUser : 用户0478
         * updateTime : 1595475823
         * dictCname : 标准
         * seats :
         * monthPrice : 0
         * mainPic : https://img.officego.com/building/1595475829035.png?x-oss-process=style/large
         * officeType : 1
         */

        @SerializedName("area")
        private Object area;
        @SerializedName("houseId")
        private int houseId;
        @SerializedName("dayPrice")
        private Object dayPrice;
        @SerializedName("houseStatus")
        private int houseStatus;
        @SerializedName("perfect")
        private String perfect;
        @SerializedName("btype")
        private int btype;
        @SerializedName("Ispermissions")
        private boolean Ispermissions;
        @SerializedName("updateUser")
        private String updateUser;
        @SerializedName("updateTime")
        private int updateTime;
        @SerializedName("dictCname")
        private String dictCname;
        @SerializedName("seats")
        private String seats;
        @SerializedName("buildingName")
        private String buildingName;
        @SerializedName("monthPrice")
        private Object monthPrice;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("officeType")
        private int officeType;

        public Object getArea() {
            return area;
        }

        public void setArea(Object area) {
            this.area = area;
        }

        public int getHouseId() {
            return houseId;
        }

        public void setHouseId(int houseId) {
            this.houseId = houseId;
        }

        public Object getDayPrice() {
            return dayPrice;
        }

        public void setDayPrice(Object dayPrice) {
            this.dayPrice = dayPrice;
        }

        public int getHouseStatus() {
            return houseStatus;
        }

        public void setHouseStatus(int houseStatus) {
            this.houseStatus = houseStatus;
        }

        public String getPerfect() {
            return perfect;
        }

        public void setPerfect(String perfect) {
            this.perfect = perfect;
        }

        public int getBtype() {
            return btype;
        }

        public void setBtype(int btype) {
            this.btype = btype;
        }

        public boolean isIspermissions() {
            return Ispermissions;
        }

        public void setIspermissions(boolean Ispermissions) {
            this.Ispermissions = Ispermissions;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        public int getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(int updateTime) {
            this.updateTime = updateTime;
        }

        public String getDictCname() {
            return dictCname;
        }

        public void setDictCname(String dictCname) {
            this.dictCname = dictCname;
        }

        public String getSeats() {
            return seats;
        }

        public void setSeats(String seats) {
            this.seats = seats;
        }

        public String getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
        }

        public Object getMonthPrice() {
            return monthPrice;
        }

        public void setMonthPrice(Object monthPrice) {
            this.monthPrice = monthPrice;
        }

        public String getMainPic() {
            return mainPic;
        }

        public void setMainPic(String mainPic) {
            this.mainPic = mainPic;
        }

        public int getOfficeType() {
            return officeType;
        }

        public void setOfficeType(int officeType) {
            this.officeType = officeType;
        }
    }
}
