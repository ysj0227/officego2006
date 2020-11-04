package com.officego.commonlib.common.model.owner;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/11/3
 **/
public class HouseEditBean {


    /**
     * IsBasis : false
     * IsImg : true
     * btype : 1
     * banner : [{"id":19994,"imgType":null,"typeId":null,"imgUrl":"https://img.officego.com/test/1603245034680.jpg?x-oss-process=style/small","remark":null,"status":null,"createUser":null,"updateUser":null,"createTime":null,"updateTime":null}]
     * houseMsg : {"id":10646,"houseNumber":"F101170","houseStatus":1,"title":"","userId":546,"licenceId":468,"mainPic":"https://img.officego.com/test/1603245034680.jpg?x-oss-process=style/small","buildingId":7661,"area":3453,"monthPrice":3454353,"simple":"2,4","dayPrice":50,"floor":"150","decoration":1,"rentalMethod":"","minimumLease":"60","rentFreePeriod":"9个月","earliestDelivery":"","officePattern":"","otherRemark":"","unitPatternImg":"","unitPatternRemark":"","officeType":1,"seats":0,"tags":"","releaseTime":1603245044,"electricFee":0,"airConditioningFee":0,"propertyCosts":0,"storeyHeight":"","orientation":"","ishot":0,"oderNum":0,"clearHeight":"8","status":1,"createUser":"546","updateUser":"546","createTime":1603245044,"updateTime":1604373110,"propertyHouseCosts":"3453","conditioningType":"","conditioningTypeCost":""}
     * IsModel : false
     * video : [{"id":4,"imgUrl":"https://officego.com.cn/building/1589010988003.jpg"}]
     * vr : [{"id":4,"imgUrl":"https://officego.com.cn/building/1589010988003.jpg"}]
     * IsTags : false
     * IsOfficeType : 0
     */

    @SerializedName("IsBasis")
    private boolean IsBasis;
    @SerializedName("IsImg")
    private boolean IsImg;
    @SerializedName("btype")
    private int btype;
    @SerializedName("houseMsg")
    private HouseMsgBean houseMsg;
    @SerializedName("IsModel")
    private boolean IsModel;
    @SerializedName("IsTags")
    private boolean IsTags;
    @SerializedName("IsOfficeType")
    private int IsOfficeType;
    @SerializedName("banner")
    private List<BannerBean> banner;
    @SerializedName("video")
    private List<VideoBean> video;
    @SerializedName("vr")
    private List<VrBean> vr;

    public boolean isIsBasis() {
        return IsBasis;
    }

    public void setIsBasis(boolean IsBasis) {
        this.IsBasis = IsBasis;
    }

    public boolean isIsImg() {
        return IsImg;
    }

    public void setIsImg(boolean IsImg) {
        this.IsImg = IsImg;
    }

    public int getBtype() {
        return btype;
    }

    public void setBtype(int btype) {
        this.btype = btype;
    }

    public HouseMsgBean getHouseMsg() {
        return houseMsg;
    }

    public void setHouseMsg(HouseMsgBean houseMsg) {
        this.houseMsg = houseMsg;
    }

    public boolean isIsModel() {
        return IsModel;
    }

    public void setIsModel(boolean IsModel) {
        this.IsModel = IsModel;
    }

    public boolean isIsTags() {
        return IsTags;
    }

    public void setIsTags(boolean IsTags) {
        this.IsTags = IsTags;
    }

    public int getIsOfficeType() {
        return IsOfficeType;
    }

    public void setIsOfficeType(int IsOfficeType) {
        this.IsOfficeType = IsOfficeType;
    }

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public List<VideoBean> getVideo() {
        return video;
    }

    public void setVideo(List<VideoBean> video) {
        this.video = video;
    }

    public List<VrBean> getVr() {
        return vr;
    }

    public void setVr(List<VrBean> vr) {
        this.vr = vr;
    }

    public static class HouseMsgBean {
        /**
         * id : 10646
         * houseNumber : F101170
         * houseStatus : 1
         * title :
         * userId : 546
         * licenceId : 468
         * mainPic : https://img.officego.com/test/1603245034680.jpg?x-oss-process=style/small
         * buildingId : 7661
         * area : 3453
         * monthPrice : 3454353
         * simple : 2,4
         * dayPrice : 50
         * floor : 150
         * decoration : 1
         * rentalMethod :
         * minimumLease : 60
         * rentFreePeriod : 9个月
         * earliestDelivery :
         * officePattern :
         * otherRemark :
         * unitPatternImg :
         * unitPatternRemark :
         * officeType : 1
         * seats : 0
         * tags :
         * releaseTime : 1603245044
         * electricFee : 0
         * airConditioningFee : 0
         * propertyCosts : 0
         * storeyHeight :
         * orientation :
         * ishot : 0
         * oderNum : 0
         * clearHeight : 8
         * status : 1
         * createUser : 546
         * updateUser : 546
         * createTime : 1603245044
         * updateTime : 1604373110
         * propertyHouseCosts : 3453
         * conditioningType :
         * conditioningTypeCost :
         */

        @SerializedName("id")
        private int id;
        @SerializedName("houseNumber")
        private String houseNumber;
        @SerializedName("houseStatus")
        private int houseStatus;
        @SerializedName("title")
        private String title;
        @SerializedName("userId")
        private int userId;
        @SerializedName("licenceId")
        private int licenceId;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("buildingId")
        private int buildingId;
        @SerializedName("area")
        private int area;
        @SerializedName("monthPrice")
        private Object monthPrice;
        @SerializedName("simple")
        private String simple;
        @SerializedName("dayPrice")
        private Object dayPrice;
        @SerializedName("floor")
        private String floor;
        @SerializedName("decoration")
        private int decoration;
        @SerializedName("rentalMethod")
        private String rentalMethod;
        @SerializedName("minimumLease")
        private String minimumLease;
        @SerializedName("rentFreePeriod")
        private String rentFreePeriod;
        @SerializedName("earliestDelivery")
        private String earliestDelivery;
        @SerializedName("officePattern")
        private String officePattern;
        @SerializedName("otherRemark")
        private String otherRemark;
        @SerializedName("unitPatternImg")
        private String unitPatternImg;
        @SerializedName("unitPatternRemark")
        private String unitPatternRemark;
        @SerializedName("officeType")
        private int officeType;
        @SerializedName("seats")
        private int seats;
        @SerializedName("tags")
        private String tags;
        @SerializedName("releaseTime")
        private int releaseTime;
        @SerializedName("electricFee")
        private int electricFee;
        @SerializedName("airConditioningFee")
        private int airConditioningFee;
        @SerializedName("propertyCosts")
        private int propertyCosts;
        @SerializedName("storeyHeight")
        private String storeyHeight;
        @SerializedName("orientation")
        private String orientation;
        @SerializedName("ishot")
        private int ishot;
        @SerializedName("oderNum")
        private int oderNum;
        @SerializedName("clearHeight")
        private String clearHeight;
        @SerializedName("status")
        private int status;
        @SerializedName("createUser")
        private String createUser;
        @SerializedName("updateUser")
        private String updateUser;
        @SerializedName("createTime")
        private int createTime;
        @SerializedName("updateTime")
        private int updateTime;
        @SerializedName("propertyHouseCosts")
        private String propertyHouseCosts;
        @SerializedName("conditioningType")
        private String conditioningType;
        @SerializedName("conditioningTypeCost")
        private String conditioningTypeCost;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getHouseNumber() {
            return houseNumber;
        }

        public void setHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
        }

        public int getHouseStatus() {
            return houseStatus;
        }

        public void setHouseStatus(int houseStatus) {
            this.houseStatus = houseStatus;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getLicenceId() {
            return licenceId;
        }

        public void setLicenceId(int licenceId) {
            this.licenceId = licenceId;
        }

        public String getMainPic() {
            return mainPic;
        }

        public void setMainPic(String mainPic) {
            this.mainPic = mainPic;
        }

        public int getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(int buildingId) {
            this.buildingId = buildingId;
        }

        public int getArea() {
            return area;
        }

        public void setArea(int area) {
            this.area = area;
        }

        public Object getMonthPrice() {
            return monthPrice;
        }

        public void setMonthPrice(Object monthPrice) {
            this.monthPrice = monthPrice;
        }

        public String getSimple() {
            return simple;
        }

        public void setSimple(String simple) {
            this.simple = simple;
        }

        public Object getDayPrice() {
            return dayPrice;
        }

        public void setDayPrice(Object dayPrice) {
            this.dayPrice = dayPrice;
        }

        public String getFloor() {
            return floor;
        }

        public void setFloor(String floor) {
            this.floor = floor;
        }

        public int getDecoration() {
            return decoration;
        }

        public void setDecoration(int decoration) {
            this.decoration = decoration;
        }

        public String getRentalMethod() {
            return rentalMethod;
        }

        public void setRentalMethod(String rentalMethod) {
            this.rentalMethod = rentalMethod;
        }

        public String getMinimumLease() {
            return minimumLease;
        }

        public void setMinimumLease(String minimumLease) {
            this.minimumLease = minimumLease;
        }

        public String getRentFreePeriod() {
            return rentFreePeriod;
        }

        public void setRentFreePeriod(String rentFreePeriod) {
            this.rentFreePeriod = rentFreePeriod;
        }

        public String getEarliestDelivery() {
            return earliestDelivery;
        }

        public void setEarliestDelivery(String earliestDelivery) {
            this.earliestDelivery = earliestDelivery;
        }

        public String getOfficePattern() {
            return officePattern;
        }

        public void setOfficePattern(String officePattern) {
            this.officePattern = officePattern;
        }

        public String getOtherRemark() {
            return otherRemark;
        }

        public void setOtherRemark(String otherRemark) {
            this.otherRemark = otherRemark;
        }

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

        public int getOfficeType() {
            return officeType;
        }

        public void setOfficeType(int officeType) {
            this.officeType = officeType;
        }

        public int getSeats() {
            return seats;
        }

        public void setSeats(int seats) {
            this.seats = seats;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public int getReleaseTime() {
            return releaseTime;
        }

        public void setReleaseTime(int releaseTime) {
            this.releaseTime = releaseTime;
        }

        public int getElectricFee() {
            return electricFee;
        }

        public void setElectricFee(int electricFee) {
            this.electricFee = electricFee;
        }

        public int getAirConditioningFee() {
            return airConditioningFee;
        }

        public void setAirConditioningFee(int airConditioningFee) {
            this.airConditioningFee = airConditioningFee;
        }

        public int getPropertyCosts() {
            return propertyCosts;
        }

        public void setPropertyCosts(int propertyCosts) {
            this.propertyCosts = propertyCosts;
        }

        public String getStoreyHeight() {
            return storeyHeight;
        }

        public void setStoreyHeight(String storeyHeight) {
            this.storeyHeight = storeyHeight;
        }

        public String getOrientation() {
            return orientation;
        }

        public void setOrientation(String orientation) {
            this.orientation = orientation;
        }

        public int getIshot() {
            return ishot;
        }

        public void setIshot(int ishot) {
            this.ishot = ishot;
        }

        public int getOderNum() {
            return oderNum;
        }

        public void setOderNum(int oderNum) {
            this.oderNum = oderNum;
        }

        public String getClearHeight() {
            return clearHeight;
        }

        public void setClearHeight(String clearHeight) {
            this.clearHeight = clearHeight;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        public int getCreateTime() {
            return createTime;
        }

        public void setCreateTime(int createTime) {
            this.createTime = createTime;
        }

        public int getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(int updateTime) {
            this.updateTime = updateTime;
        }

        public String getPropertyHouseCosts() {
            return propertyHouseCosts;
        }

        public void setPropertyHouseCosts(String propertyHouseCosts) {
            this.propertyHouseCosts = propertyHouseCosts;
        }

        public String getConditioningType() {
            return conditioningType;
        }

        public void setConditioningType(String conditioningType) {
            this.conditioningType = conditioningType;
        }

        public String getConditioningTypeCost() {
            return conditioningTypeCost;
        }

        public void setConditioningTypeCost(String conditioningTypeCost) {
            this.conditioningTypeCost = conditioningTypeCost;
        }
    }

    public static class BannerBean {
        /**
         * id : 19994
         * imgType : null
         * typeId : null
         * imgUrl : https://img.officego.com/test/1603245034680.jpg?x-oss-process=style/small
         * remark : null
         * status : null
         * createUser : null
         * updateUser : null
         * createTime : null
         * updateTime : null
         */

        @SerializedName("id")
        private int id;
        @SerializedName("imgType")
        private Object imgType;
        @SerializedName("typeId")
        private Object typeId;
        @SerializedName("imgUrl")
        private String imgUrl;
        @SerializedName("remark")
        private Object remark;
        @SerializedName("status")
        private Object status;
        @SerializedName("createUser")
        private Object createUser;
        @SerializedName("updateUser")
        private Object updateUser;
        @SerializedName("createTime")
        private Object createTime;
        @SerializedName("updateTime")
        private Object updateTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getImgType() {
            return imgType;
        }

        public void setImgType(Object imgType) {
            this.imgType = imgType;
        }

        public Object getTypeId() {
            return typeId;
        }

        public void setTypeId(Object typeId) {
            this.typeId = typeId;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public Object getCreateUser() {
            return createUser;
        }

        public void setCreateUser(Object createUser) {
            this.createUser = createUser;
        }

        public Object getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(Object updateUser) {
            this.updateUser = updateUser;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }
    }

    public static class VideoBean {
        /**
         * id : 4
         * imgUrl : https://officego.com.cn/building/1589010988003.jpg
         */

        @SerializedName("id")
        private int id;
        @SerializedName("imgUrl")
        private String imgUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }

    public static class VrBean {
        /**
         * id : 4
         * imgUrl : https://officego.com.cn/building/1589010988003.jpg
         */

        @SerializedName("id")
        private int id;
        @SerializedName("imgUrl")
        private String imgUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
