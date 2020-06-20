package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/1.
 * Descriptions:
 **/
public class BuildingBean implements Serializable {

    /**
     * total : 412
     * pageSize : 10
     * totalPage : 42
     * pageNo : 1
     * prePage : 1
     * nextPage : 2
     * list : [{"releaseTime":null,"distance":"0km","passengerLift":4,"btype":2,"remark":null,"areaMap":[22,22],"buildingMap":{"stationNames":["漕宝路"],"stationline":["12"],"nearbySubwayTime":["6"],"stationColours":["#10614A"]},"independenceOffice":1,"maxArea":22,"minSeats":12,"mainPic":"https://img.officego.com/building/1590999378530.jpg?x-oss-process=style/large","id":56,"minArea":22,"seatMonthPrice":0,"address":"上海市徐汇区漕河泾开发区(地铁站)","maxSeats":22,"updateTime":1591686434,"userId":null,"tags":[{"dictValue":1,"dictImg":null,"dictCname":"星巴克","dictImgBlack":null},{"dictValue":2,"dictImg":null,"dictCname":"地铁上盖","dictImgBlack":null},{"dictValue":3,"dictImg":null,"dictCname":"金牌物业","dictImgBlack":null},{"dictValue":6,"dictImg":null,"dictCname":"员工食堂","dictImgBlack":null},{"dictValue":7,"dictImg":null,"dictCname":"健身房","dictImgBlack":null},{"dictValue":8,"dictImg":null,"dictCname":"充电桩","dictImgBlack":null},{"dictValue":11,"dictImg":null,"dictCname":"涉外网路","dictImgBlack":null},{"dictValue":12,"dictImg":null,"dictCname":"免费软饮","dictImgBlack":null}],"businessDistrict":"徐汇区-漕河泾","createTime":1590999259,"minDayPrice":8,"totalFloor":null,"name":"TEST网点","storeyHeight":"2","maxDayPrice":22,"openStation":1,"officeType":"1,2"}]
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
         * releaseTime : null
         * distance : 0km
         * passengerLift : 4
         * btype : 2
         * remark : null
         * areaMap : [22,22]
         * buildingMap : {"stationNames":["漕宝路"],"stationline":["12"],"nearbySubwayTime":["6"],"stationColours":["#10614A"]}
         * independenceOffice : 1
         * maxArea : 22
         * minSeats : 12
         * mainPic : https://img.officego.com/building/1590999378530.jpg?x-oss-process=style/large
         * id : 56
         * minArea : 22
         * seatMonthPrice : 0
         * address : 上海市徐汇区漕河泾开发区(地铁站)
         * maxSeats : 22
         * updateTime : 1591686434
         * userId : null
         * tags : [{"dictValue":1,"dictImg":null,"dictCname":"星巴克","dictImgBlack":null},{"dictValue":2,"dictImg":null,"dictCname":"地铁上盖","dictImgBlack":null},{"dictValue":3,"dictImg":null,"dictCname":"金牌物业","dictImgBlack":null},{"dictValue":6,"dictImg":null,"dictCname":"员工食堂","dictImgBlack":null},{"dictValue":7,"dictImg":null,"dictCname":"健身房","dictImgBlack":null},{"dictValue":8,"dictImg":null,"dictCname":"充电桩","dictImgBlack":null},{"dictValue":11,"dictImg":null,"dictCname":"涉外网路","dictImgBlack":null},{"dictValue":12,"dictImg":null,"dictCname":"免费软饮","dictImgBlack":null}]
         * businessDistrict : 徐汇区-漕河泾
         * createTime : 1590999259
         * minDayPrice : 8
         * totalFloor : null
         * name : TEST网点
         * storeyHeight : 2
         * maxDayPrice : 22
         * openStation : 1
         * officeType : 1,2
         */

        @SerializedName("releaseTime")
        private Object releaseTime;
        @SerializedName("distance")
        private String distance;
        @SerializedName("passengerLift")
        private Object passengerLift;
        @SerializedName("btype")
        private int btype;
        @SerializedName("remark")
        private Object remark;
        @SerializedName("buildingMap")
        private BuildingMapBean buildingMap;
        @SerializedName("independenceOffice")
        private int independenceOffice;
        @SerializedName("minSeats")
        private int minSeats;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("id")
        private int id;
        @SerializedName("maxArea")
        private Object maxArea;
        @SerializedName("minArea")
        private Object minArea;
        @SerializedName("seatMonthPrice")
        private Object seatMonthPrice;
        @SerializedName("minDayPrice")
        private Object minDayPrice;
        @SerializedName("maxDayPrice")
        private Object maxDayPrice;
        @SerializedName("address")
        private String address;
        @SerializedName("maxSeats")
        private int maxSeats;
        @SerializedName("updateTime")
        private int updateTime;
        @SerializedName("userId")
        private Object userId;
        @SerializedName("businessDistrict")
        private String businessDistrict;
        @SerializedName("createTime")
        private int createTime;
        @SerializedName("totalFloor")
        private Object totalFloor;
        @SerializedName("name")
        private String name;
        @SerializedName("storeyHeight")
        private String storeyHeight;
        @SerializedName("openStation")
        private int openStation;
        @SerializedName("officeType")
        private String officeType;
        @SerializedName("areaMap")
        private List<Object> areaMap;
        @SerializedName("tags")
        private List<TagsBean> tags;

        public Object getReleaseTime() {
            return releaseTime;
        }

        public void setReleaseTime(Object releaseTime) {
            this.releaseTime = releaseTime;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public Object getPassengerLift() {
            return passengerLift;
        }

        public void setPassengerLift(Object passengerLift) {
            this.passengerLift = passengerLift;
        }

        public int getBtype() {
            return btype;
        }

        public void setBtype(int btype) {
            this.btype = btype;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
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

        public Object getMaxArea() {
            return maxArea;
        }

        public void setMaxArea(Object maxArea) {
            this.maxArea = maxArea;
        }

        public int getMinSeats() {
            return minSeats;
        }

        public void setMinSeats(int minSeats) {
            this.minSeats = minSeats;
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

        public Object getMinArea() {
            return minArea;
        }

        public void setMinArea(Object minArea) {
            this.minArea = minArea;
        }

        public Object getSeatMonthPrice() {
            return seatMonthPrice;
        }

        public void setSeatMonthPrice(Object seatMonthPrice) {
            this.seatMonthPrice = seatMonthPrice;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getMaxSeats() {
            return maxSeats;
        }

        public void setMaxSeats(int maxSeats) {
            this.maxSeats = maxSeats;
        }

        public int getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(int updateTime) {
            this.updateTime = updateTime;
        }

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public String getBusinessDistrict() {
            return businessDistrict;
        }

        public void setBusinessDistrict(String businessDistrict) {
            this.businessDistrict = businessDistrict;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public Object getMinDayPrice() {
            return minDayPrice;
        }

        public void setMinDayPrice(Object minDayPrice) {
            this.minDayPrice = minDayPrice;
        }

        public Object getTotalFloor() {
            return totalFloor;
        }

        public void setTotalFloor(Object totalFloor) {
            this.totalFloor = totalFloor;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStoreyHeight() {
            return storeyHeight;
        }

        public void setStoreyHeight(String storeyHeight) {
            this.storeyHeight = storeyHeight;
        }

        public Object getMaxDayPrice() {
            return maxDayPrice;
        }

        public void setMaxDayPrice(Object maxDayPrice) {
            this.maxDayPrice = maxDayPrice;
        }

        public int getOpenStation() {
            return openStation;
        }

        public void setOpenStation(int openStation) {
            this.openStation = openStation;
        }

        public String getOfficeType() {
            return officeType;
        }

        public void setOfficeType(String officeType) {
            this.officeType = officeType;
        }

        public List<Object> getAreaMap() {
            return areaMap;
        }

        public void setAreaMap(List<Object> areaMap) {
            this.areaMap = areaMap;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public static class BuildingMapBean implements Serializable {
            @SerializedName("stationNames")
            private List<String> stationNames;
            @SerializedName("stationline")
            private List<String> stationline;
            @SerializedName("nearbySubwayTime")
            private List<String> nearbySubwayTime;
            @SerializedName("stationColours")
            private List<String> stationColours;

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

            public List<String> getStationColours() {
                return stationColours;
            }

            public void setStationColours(List<String> stationColours) {
                this.stationColours = stationColours;
            }
        }

        public static class TagsBean implements Serializable {
            /**
             * dictValue : 1
             * dictImg : null
             * dictCname : 星巴克
             * dictImgBlack : null
             */

            @SerializedName("dictValue")
            private int dictValue;
            @SerializedName("dictImg")
            private Object dictImg;
            @SerializedName("dictCname")
            private String dictCname;
            @SerializedName("dictImgBlack")
            private Object dictImgBlack;

            public int getDictValue() {
                return dictValue;
            }

            public void setDictValue(int dictValue) {
                this.dictValue = dictValue;
            }

            public Object getDictImg() {
                return dictImg;
            }

            public void setDictImg(Object dictImg) {
                this.dictImg = dictImg;
            }

            public String getDictCname() {
                return dictCname;
            }

            public void setDictCname(String dictCname) {
                this.dictCname = dictCname;
            }

            public Object getDictImgBlack() {
                return dictImgBlack;
            }

            public void setDictImgBlack(Object dictImgBlack) {
                this.dictImgBlack = dictImgBlack;
            }
        }
    }
}
