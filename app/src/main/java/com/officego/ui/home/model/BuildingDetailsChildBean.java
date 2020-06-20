package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/3.
 * Descriptions:
 **/
public class BuildingDetailsChildBean implements Serializable {


    /**
     * total : 1
     * pageSize : 10
     * totalPage : 1
     * pageNo : 1
     * prePage : 1
     * nextPage : 1
     * list : [{"area":200,"monthPrice":2000,"dayPrice":200,"totalFloor":"30","mainPic":"https://img.officego.com.cn/building/1590998773855.jpg","simple":"20,40","id":224,"floor":"101","decoration":"带家具","seats":0,"officeType":1}]
     * listids : []
     */

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
    @SerializedName("list")
    private List<ListBean> list;
    @SerializedName("listids")
    private List<?> listids;

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<?> getListids() {
        return listids;
    }

    public void setListids(List<?> listids) {
        this.listids = listids;
    }

    public static class ListBean implements Serializable {
        /**
         * area : 200
         * monthPrice : 2000
         * dayPrice : 200
         * totalFloor : 30
         * mainPic : https://img.officego.com.cn/building/1590998773855.jpg
         * simple : 20,40
         * id : 224
         * floor : 101
         * decoration : 带家具
         * seats : 0
         * officeType : 1
         */

        @SerializedName("area")
        private Object area;
        @SerializedName("monthPrice")
        private Object monthPrice;
        @SerializedName("dayPrice")
        private Object dayPrice;
        @SerializedName("totalFloor")
        private String totalFloor;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("simple")
        private String simple;
        @SerializedName("id")
        private int id;
        @SerializedName("floor")
        private String floor;
        @SerializedName("decoration")
        private String decoration;
        @SerializedName("seats")
        private int seats;
        @SerializedName("officeType")
        private int officeType;

        public Object getArea() {
            return area;
        }

        public void setArea(Object area) {
            this.area = area;
        }

        public Object getMonthPrice() {
            return monthPrice;
        }

        public void setMonthPrice(Object monthPrice) {
            this.monthPrice = monthPrice;
        }

        public Object getDayPrice() {
            return dayPrice;
        }

        public void setDayPrice(Object dayPrice) {
            this.dayPrice = dayPrice;
        }

        public String getTotalFloor() {
            return totalFloor;
        }

        public void setTotalFloor(String totalFloor) {
            this.totalFloor = totalFloor;
        }

        public String getMainPic() {
            return mainPic;
        }

        public void setMainPic(String mainPic) {
            this.mainPic = mainPic;
        }

        public String getSimple() {
            return simple;
        }

        public void setSimple(String simple) {
            this.simple = simple;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFloor() {
            return floor;
        }

        public void setFloor(String floor) {
            this.floor = floor;
        }

        public String getDecoration() {
            return decoration;
        }

        public void setDecoration(String decoration) {
            this.decoration = decoration;
        }

        public int getSeats() {
            return seats;
        }

        public void setSeats(int seats) {
            this.seats = seats;
        }

        public int getOfficeType() {
            return officeType;
        }

        public void setOfficeType(int officeType) {
            this.officeType = officeType;
        }
    }
}
