package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/3.
 * Descriptions:
 **/
public class HouseOfficeDetailsJointWorkBean {


    /**
     * imgUrl : [{"imgUrl":"https://img.officego.com.cn/building/1590999464639.jpg","typeId":226,"remark":null,"id":512,"imgType":2},{"imgUrl":"https://img.officego.com.cn/building/1591263008301.jpg","typeId":226,"remark":null,"id":550,"imgType":2}]
     * vrUrl : []
     * videoUrl : [{"imgUrl":"https://img.officego.com.cn/building/1591239116858.mp4","typeId":226,"remark":null,"id":45,"imgType":2},{"imgUrl":"https://img.officego.com.cn/building/1591239116858.mp4","typeId":226,"remark":null,"id":55,"imgType":2}]
     * IsFavorite : false
     * house : {"area":22,"dayPrice":8,"btype":2,"simple":null,"seats":12,"userId":107,"buildingId":56,"tags":[{"dictValue":1,"dictImg":"","dictCname":"近地铁","dictImgBlack":null}],"buildingName":"TEST大厦2","basicInformation":{"unitPatternImg":"https://img.officego.com.cn/house/1590999457962.jpg","officePattern":"办公格局","minimumLease":"1","earliestDelivery":"1","unitPatternRemark":"户型","rentFreePeriod":"1个月","otherRemark":null,"floor":"1"},"businessDistrict":"徐汇区-漕河泾","monthPrice":222,"licenceId":82,"stationNames":["漕河泾开发区"],"branchesName":"TEST网点","mainPic":"https://img.officego.com.cn/building/1590999464639.jpg","stationline":["9"],"id":226,"nearbySubwayTime":["1"],"stationColours":["#7ACDF5"],"decoration":"带窗户","officeType":1}
     */

    @SerializedName("IsFavorite")
    private boolean IsFavorite;
    @SerializedName("house")
    private HouseBean house;
    @SerializedName("imgUrl")
    private List<ImgUrlBean> imgUrl;
    @SerializedName("vrUrl")
    private List<VrUrlBean> vrUrl;
    @SerializedName("videoUrl")
    private List<VideoUrlBean> videoUrl;

    public boolean isIsFavorite() {
        return IsFavorite;
    }

    public void setIsFavorite(boolean IsFavorite) {
        this.IsFavorite = IsFavorite;
    }

    public HouseBean getHouse() {
        return house;
    }

    public void setHouse(HouseBean house) {
        this.house = house;
    }

    public List<ImgUrlBean> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(List<ImgUrlBean> imgUrl) {
        this.imgUrl = imgUrl;
    }

    public List<VrUrlBean> getVrUrl() {
        return vrUrl;
    }

    public void setVrUrl(List<VrUrlBean> vrUrl) {
        this.vrUrl = vrUrl;
    }

    public List<VideoUrlBean> getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(List<VideoUrlBean> videoUrl) {
        this.videoUrl = videoUrl;
    }

    public static class HouseBean {
        /**
         * area : 22
         * dayPrice : 8
         * btype : 2
         * simple : null
         * seats : 12
         * userId : 107
         * buildingId : 56
         * tags : [{"dictValue":1,"dictImg":"","dictCname":"近地铁","dictImgBlack":null}]
         * buildingName : TEST大厦2
         * basicInformation : {"unitPatternImg":"https://img.officego.com.cn/house/1590999457962.jpg","officePattern":"办公格局","minimumLease":"1","earliestDelivery":"1","unitPatternRemark":"户型","rentFreePeriod":"1个月","otherRemark":null,"floor":"1"}
         * businessDistrict : 徐汇区-漕河泾
         * monthPrice : 222
         * licenceId : 82
         * stationNames : ["漕河泾开发区"]
         * branchesName : TEST网点
         * mainPic : https://img.officego.com.cn/building/1590999464639.jpg
         * stationline : ["9"]
         * id : 226
         * nearbySubwayTime : ["1"]
         * stationColours : ["#7ACDF5"]
         * decoration : 带窗户
         * officeType : 1
         */

        @SerializedName("area")
        private Object area;
        @SerializedName("address")
        private String address;
        @SerializedName("dayPrice")
        private Object dayPrice;
        @SerializedName("btype")
        private int btype;
        @SerializedName("simple")
        private Object simple;
        @SerializedName("seats")
        private int seats;
        @SerializedName("userId")
        private int userId;
        @SerializedName("buildingId")
        private int buildingId;
        @SerializedName("buildingName")
        private String buildingName;
        @SerializedName("basicInformation")
        private BasicInformationBean basicInformation;
        @SerializedName("businessDistrict")
        private String businessDistrict;
        @SerializedName("monthPrice")
        private Object monthPrice;
        @SerializedName("licenceId")
        private int licenceId;
        @SerializedName("branchesName")
        private String branchesName;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("id")
        private int id;
        @SerializedName("decoration")
        private String decoration;
        @SerializedName("officeType")
        private int officeType;
        @SerializedName("tags")
        private List<TagsBean> tags;
        @SerializedName("stationNames")
        private List<String> stationNames;
        @SerializedName("stationline")
        private List<String> stationline;
        @SerializedName("nearbySubwayTime")
        private List<String> nearbySubwayTime;
        @SerializedName("stationColours")
        private List<String> stationColours;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getArea() {
            return area;
        }

        public void setArea(Object area) {
            this.area = area;
        }

        public Object getDayPrice() {
            return dayPrice;
        }

        public void setDayPrice(Object dayPrice) {
            this.dayPrice = dayPrice;
        }

        public int getBtype() {
            return btype;
        }

        public void setBtype(int btype) {
            this.btype = btype;
        }

        public Object getSimple() {
            return simple;
        }

        public void setSimple(Object simple) {
            this.simple = simple;
        }

        public int getSeats() {
            return seats;
        }

        public void setSeats(int seats) {
            this.seats = seats;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
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

        public BasicInformationBean getBasicInformation() {
            return basicInformation;
        }

        public void setBasicInformation(BasicInformationBean basicInformation) {
            this.basicInformation = basicInformation;
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

        public int getLicenceId() {
            return licenceId;
        }

        public void setLicenceId(int licenceId) {
            this.licenceId = licenceId;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDecoration() {
            return decoration;
        }

        public void setDecoration(String decoration) {
            this.decoration = decoration;
        }

        public int getOfficeType() {
            return officeType;
        }

        public void setOfficeType(int officeType) {
            this.officeType = officeType;
        }

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
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

        public List<String> getStationColours() {
            return stationColours;
        }

        public void setStationColours(List<String> stationColours) {
            this.stationColours = stationColours;
        }

        public static class BasicInformationBean {
            /**
             * unitPatternImg : https://img.officego.com.cn/house/1590999457962.jpg
             * officePattern : 办公格局
             * minimumLease : 1
             * earliestDelivery : 1
             * unitPatternRemark : 户型
             * rentFreePeriod : 1个月
             * otherRemark : null
             * floor : 1
             */

            @SerializedName("unitPatternImg")
            private String unitPatternImg;
            @SerializedName("officePattern")
            private String officePattern;
            @SerializedName("minimumLease")
            private String minimumLease;
            @SerializedName("earliestDelivery")
            private String earliestDelivery;
            @SerializedName("unitPatternRemark")
            private String unitPatternRemark;
            @SerializedName("rentFreePeriod")
            private String rentFreePeriod;
            @SerializedName("otherRemark")
            private Object otherRemark;
            @SerializedName("floor")
            private String floor;

            public String getUnitPatternImg() {
                return unitPatternImg;
            }

            public void setUnitPatternImg(String unitPatternImg) {
                this.unitPatternImg = unitPatternImg;
            }

            public String getOfficePattern() {
                return officePattern;
            }

            public void setOfficePattern(String officePattern) {
                this.officePattern = officePattern;
            }

            public String getMinimumLease() {
                return minimumLease;
            }

            public void setMinimumLease(String minimumLease) {
                this.minimumLease = minimumLease;
            }

            public String getEarliestDelivery() {
                return earliestDelivery;
            }

            public void setEarliestDelivery(String earliestDelivery) {
                this.earliestDelivery = earliestDelivery;
            }

            public String getUnitPatternRemark() {
                return unitPatternRemark;
            }

            public void setUnitPatternRemark(String unitPatternRemark) {
                this.unitPatternRemark = unitPatternRemark;
            }

            public String getRentFreePeriod() {
                return rentFreePeriod;
            }

            public void setRentFreePeriod(String rentFreePeriod) {
                this.rentFreePeriod = rentFreePeriod;
            }

            public Object getOtherRemark() {
                return otherRemark;
            }

            public void setOtherRemark(Object otherRemark) {
                this.otherRemark = otherRemark;
            }

            public String getFloor() {
                return floor;
            }

            public void setFloor(String floor) {
                this.floor = floor;
            }
        }

        public static class TagsBean {
            /**
             * dictValue : 1
             * dictImg :
             * dictCname : 近地铁
             * dictImgBlack : null
             */

            @SerializedName("dictValue")
            private int dictValue;
            @SerializedName("dictImg")
            private String dictImg;
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

            public String getDictImg() {
                return dictImg;
            }

            public void setDictImg(String dictImg) {
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

    public static class ImgUrlBean {
        /**
         * imgUrl : https://img.officego.com.cn/building/1590999464639.jpg
         * typeId : 226
         * remark : null
         * id : 512
         * imgType : 2
         */

        @SerializedName("imgUrl")
        private String imgUrl;
        @SerializedName("typeId")
        private int typeId;
        @SerializedName("remark")
        private Object remark;
        @SerializedName("id")
        private int id;
        @SerializedName("imgType")
        private int imgType;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getImgType() {
            return imgType;
        }

        public void setImgType(int imgType) {
            this.imgType = imgType;
        }
    }

    public static class VrUrlBean {
        /**
         * imgUrl : www.hahaha.com
         * typeId : 224
         * remark :
         * id : 133
         * imgType : 2
         */

        @SerializedName("imgUrl")
        private String imgUrl;
        @SerializedName("typeId")
        private int typeId;
        @SerializedName("remark")
        private String remark;
        @SerializedName("id")
        private int id;
        @SerializedName("imgType")
        private int imgType;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getImgType() {
            return imgType;
        }

        public void setImgType(int imgType) {
            this.imgType = imgType;
        }
    }

    public static class VideoUrlBean {
        /**
         * imgUrl : https://img.officego.com.cn/building/1591239116858.mp4
         * typeId : 226
         * remark : null
         * id : 45
         * imgType : 2
         */

        @SerializedName("imgUrl")
        private String imgUrl;
        @SerializedName("typeId")
        private int typeId;
        @SerializedName("remark")
        private Object remark;
        @SerializedName("id")
        private int id;
        @SerializedName("imgType")
        private int imgType;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getImgType() {
            return imgType;
        }

        public void setImgType(int imgType) {
            this.imgType = imgType;
        }
    }
}
