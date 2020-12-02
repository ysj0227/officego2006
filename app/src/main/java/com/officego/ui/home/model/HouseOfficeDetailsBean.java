package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/3.
 * Descriptions:
 **/
public class HouseOfficeDetailsBean {


    /**
     * imgUrl : [{"imgUrl":"https://img.officego.com.cn/building/1590998773855.jpg","typeId":224,"remark":null,"id":510,"imgType":2}]
     * vrUrl : [{"imgUrl":"www.hahaha.com","typeId":224,"remark":"","id":133,"imgType":2}]
     * videoUrl : [{"imgUrl":"https://img.officego.com.cn/building/1591239116858.mp4","typeId":224,"remark":null,"id":53,"imgType":2}]
     * IsFavorite : false
     * house : {"area":200,"dayPrice":8,"btype":1,"simple":"20,40","seats":null,"userId":103,"buildingId":54,"tags":[{"dictValue":1,"dictImg":"","dictCname":"近地铁","dictImgBlack":null},{"dictValue":10,"dictImg":null,"dictCname":"房型方正","dictImgBlack":null}],"buildingName":"恒源大楼","basicInformation":{"unitPatternImg":"https://img.officego.com.cn/house/1590996185581.jpg","officePattern":"大","minimumLease":"2","earliestDelivery":"随时","unitPatternRemark":"户型说明","rentFreePeriod":"1个月","otherRemark":null,"floor":"101"},"businessDistrict":"杨浦区-五角场","monthPrice":2000,"licenceId":80,"stationNames":["世纪公园","花木路"],"branchesName":null,"mainPic":"https://img.officego.com.cn/building/1590998773855.jpg","stationline":["2","7"],"id":224,"nearbySubwayTime":["1","14"],"stationColours":["#8DC11F","#ED6D00"],"decoration":"带家具","officeType":1}
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
         * area : 200
         * dayPrice : 8
         * btype : 1
         * simple : 20,40
         * seats : null
         * userId : 103
         * buildingId : 54
         * tags : [{"dictValue":1,"dictImg":"","dictCname":"近地铁","dictImgBlack":null},{"dictValue":10,"dictImg":null,"dictCname":"房型方正","dictImgBlack":null}]
         * buildingName : 恒源大楼
         * basicInformation : {"unitPatternImg":"https://img.officego.com.cn/house/1590996185581.jpg","officePattern":"大","minimumLease":"2","earliestDelivery":"随时","unitPatternRemark":"户型说明","rentFreePeriod":"1个月","otherRemark":null,"floor":"101"}
         * businessDistrict : 杨浦区-五角场
         * monthPrice : 2000
         * licenceId : 80
         * stationNames : ["世纪公园","花木路"]
         * branchesName : null
         * mainPic : https://img.officego.com.cn/building/1590998773855.jpg
         * stationline : ["2","7"]
         * id : 224
         * nearbySubwayTime : ["1","14"]
         * stationColours : ["#8DC11F","#ED6D00"]
         * decoration : 带家具
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
        private String simple;
        @SerializedName("seats")
        private Object seats;
        @SerializedName("userId")
        private int userId;
        @SerializedName("buildingId")
        private int buildingId;
        @SerializedName("buildingName")
        private String buildingName;
        @SerializedName("title")
        private String title;
        @SerializedName("basicInformation")
        private BasicInformationBean basicInformation;
        @SerializedName("businessDistrict")
        private String businessDistrict;
        @SerializedName("monthPrice")
        private Object monthPrice;
        @SerializedName("licenceId")
        private int licenceId;
        @SerializedName("branchesName")
        private Object branchesName;
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
        @SerializedName("longitude")
        private double longitude;
        @SerializedName("latitude")
        private double latitude;

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

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

        public String getSimple() {
            return simple;
        }

        public void setSimple(String simple) {
            this.simple = simple;
        }

        public Object getSeats() {
            return seats;
        }

        public void setSeats(Object seats) {
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

        public Object getBranchesName() {
            return branchesName;
        }

        public void setBranchesName(Object branchesName) {
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
             * unitPatternImg : https://img.officego.com.cn/house/1590996185581.jpg
             * officePattern : 大
             * minimumLease : 2
             * earliestDelivery : 随时
             * unitPatternRemark : 户型说明
             * rentFreePeriod : 1个月
             * otherRemark : null
             * floor : 101
             */

            @SerializedName("unitPatternImg")
            private String unitPatternImg;
            @SerializedName("unitPatternRemark")
            private String unitPatternRemark;
            @SerializedName("houseMsg")
            private List<HouseMsgBean> houseMsg;

            public String getUnitPatternImg() {
                return unitPatternImg;
            }

            public void setUnitPatternImg(String unitPatternImg) {
                this.unitPatternImg = unitPatternImg;
            }

            public String getUnitPatternRemark() {
                return unitPatternRemark;
            }

            public void setUnitPatternRemark(String unitPatternRemark) {
                this.unitPatternRemark = unitPatternRemark;
            }

            public List<HouseMsgBean> getHouseMsg() {
                return houseMsg;
            }

            public void setHouseMsg(List<HouseMsgBean> houseMsg) {
                this.houseMsg = houseMsg;
            }

            public static class HouseMsgBean {
                @SerializedName("name")
                private String name;
                @SerializedName("value")
                private String value;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
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
         * imgUrl : https://img.officego.com.cn/building/1590998773855.jpg
         * typeId : 224
         * remark : null
         * id : 510
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
         * typeId : 224
         * remark : null
         * id : 53
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
