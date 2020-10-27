package com.officego.commonlib.common.model.owner;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/26
 **/
public class HouseBean {

    @SerializedName("total")
    private int total;
    @SerializedName("pageSize")
    private int pageSize;
    @SerializedName("totalPage")
    private int totalPage;
    @SerializedName("pageNo")
    private int pageNo;
    @SerializedName("prePage")
    private int prePage;
    @SerializedName("nextPage")
    private int nextPage;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    @SerializedName("list")
    private List<ListBean> list;

    public List<ListBean> getData() {
        return list;
    }

    public void setData(List<ListBean> data) {
        this.list = data;
    }

    public static class ListBean {
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
        @SerializedName("title")
        private String title;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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
