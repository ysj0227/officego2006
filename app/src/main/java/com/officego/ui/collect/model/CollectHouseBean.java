package com.officego.ui.collect.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class CollectHouseBean {

    /**
     * total : 3
     * pageSize : 10
     * totalPage : 1
     * pageNo : 1
     * prePage : 1
     * nextPage : 1
     * list : [{"area":88,"dayPrice":8,"Isfailure":false,"btype":1,"simple":"8,17","seats":"","buildingId":55,"buildingName":"TEST大楼","businessDistrict":"静安区-静安市","monthPrice":222,"totalFloor":"22","mainPic":"https://img.officego.com.cn/building/1590993420990.jpg","id":223,"floor":"2","decoration":"标准","officeType":""},{"area":200,"dayPrice":8,"Isfailure":false,"btype":1,"simple":"20,40","seats":"","buildingId":54,"buildingName":"恒源大楼","businessDistrict":"杨浦区-五角场","monthPrice":2000,"totalFloor":"30","mainPic":"https://img.officego.com.cn/building/1590998773855.jpg","id":224,"floor":"101","decoration":"带家具","officeType":""},{"area":111,"dayPrice":8,"Isfailure":false,"btype":1,"simple":"11,22","seats":"","buildingId":55,"buildingName":"TEST大楼","businessDistrict":"静安区-静安市","monthPrice":1111,"totalFloor":"22","mainPic":"https://img.officego.com.cn/building/1590998644012.jpg","id":225,"floor":"1","decoration":"带家具","officeType":""}]
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

    public static class ListBean {
        /**
         * area : 88
         * dayPrice : 8
         * Isfailure : false
         * btype : 1
         * simple : 8,17
         * seats :
         * buildingId : 55
         * buildingName : TEST大楼
         * businessDistrict : 静安区-静安市
         * monthPrice : 222
         * totalFloor : 22
         * mainPic : https://img.officego.com.cn/building/1590993420990.jpg
         * id : 223
         * floor : 2
         * decoration : 标准
         * officeType :
         */

        @SerializedName("area")
        private Object area;
        @SerializedName("dayPrice")
        private Object dayPrice;
        @SerializedName("Isfailure")
        private int Isfailure;
        @SerializedName("btype")
        private int btype;
        @SerializedName("simple")
        private String simple;
        @SerializedName("seats")
        private String seats;
        @SerializedName("buildingId")
        private int buildingId;
        @SerializedName("buildingName")
        private String buildingName;
        @SerializedName("businessDistrict")
        private String businessDistrict;
        @SerializedName("monthPrice")
        private Object monthPrice;
        @SerializedName("totalFloor")
        private String totalFloor;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("id")
        private int id;
        @SerializedName("floor")
        private String floor;
        @SerializedName("decoration")
        private String decoration;
        @SerializedName("officeType")
        private String officeType;
        @SerializedName("vr")
        private String vr;

        public Object getArea() {
            return area;
        }

        public void setArea(Object area) {
            this.area = area;
        }

        public Object getDayPrice() {
            return dayPrice;
        }

        public void setDayPrice(int dayPrice) {
            this.dayPrice = dayPrice;
        }

        public int isIsfailure() {
            return Isfailure;
        }

        public void setIsfailure(int Isfailure) {
            this.Isfailure = Isfailure;
        }

        public int getBtype() {
            return btype;
        }

        public void setBtype(int btype) {
            this.btype = btype;
        }

        public String getSimple() {
            return simple;
        }

        public void setSimple(String simple) {
            this.simple = simple;
        }

        public String getSeats() {
            return seats;
        }

        public void setSeats(String seats) {
            this.seats = seats;
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

        public String getBusinessDistrict() {
            return businessDistrict;
        }

        public void setBusinessDistrict(String businessDistrict) {
            this.businessDistrict = businessDistrict;
        }

        public Object getMonthPrice() {
            return monthPrice;
        }

        public void setMonthPrice(Object monthPrice) {
            this.monthPrice = monthPrice;
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

        public String getOfficeType() {
            return officeType;
        }

        public void setOfficeType(String officeType) {
            this.officeType = officeType;
        }

        public String getVr() {
            return vr;
        }

        public void setVr(String vr) {
            this.vr = vr;
        }
    }
}
