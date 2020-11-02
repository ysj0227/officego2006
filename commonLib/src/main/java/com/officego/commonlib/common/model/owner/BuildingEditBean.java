package com.officego.commonlib.common.model.owner;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/11/2
 **/
public class BuildingEditBean {

    /**
     * address : 上海普陀长寿路
     * companyService : [{"dictValue":6,"dictImg":"https://img.officego.com/dictionary/rlzp.png","dictCname":"人力招聘","dictImgBlack":"https://img.officego.com/dictionary/1591169908538.png"}]
     * roomMatching : [{"dictValue":6,"dictImg":"https://img.officego.com/dictionary/rlzp.png","dictCname":"人力招聘","dictImgBlack":"https://img.officego.com/dictionary/1591169908538.png"}]
     * basicServices : [{"dictValue":6,"dictImg":"https://img.officego.com/dictionary/rlzp.png","dictCname":"人力招聘","dictImgBlack":"https://img.officego.com/dictionary/1591169908538.png"}]
     * banner : [{"id":19725,"imgType":null,"typeId":null,"imgUrl":"https://img.officego.com/test/1600225233490.jpg?x-oss-process=style/small","remark":null,"status":null,"createUser":null,"updateUser":null,"createTime":null,"updateTime":null}]
     * buildingMsg : {"id":7682,"buildingNumber":"","buildingType":1,"buildingName":"新百安大厦","branchesName":"","mainPic":"https://img.officego.com/building/1598841232107.jpg?x-oss-process=style/small","btype":1,"status":1,"address":"胶州路","longitude":"121.439420","latitude":"31.235292","buildingIntroduction":"","refurbishedTime":"2020","buildingOccupancyRate":"","supportFacilities":"","completionTime":"1998","constructionArea":"","houseCount":0,"totalFloor":"38","branchesTotalFloor":"","storeyHeight":"2.8","passengerLift":"6","cargoLift":"2","airConditioning":"中央空调","airConditioningFee":"","internet":"电信,联通,移动","property":"上海安国酒店有限公司","propertyCosts":"40","parkingSpace":"500","tags":"","promoteSlogan":"","basicServices":"","corporateServices":"","districtId":"5","businessDistrict":"34","nearbySubwayLine":"13,7","nearbySubway":"364,160","nearbySubwayTime":"10,8","nearbySubwayDistance":"855,629","releaseTime":0,"ishot":null,"settlementLicence":"蓉昕国际贸易有限公司，森炜实业发展（上海）有限公司，莫博实业（上海）有限公司，上海后弦文化传媒工作室","freeRoom":0,"conferenceNumber":0,"conferencePeopleNumber":0,"roomMatching":"","renovationStyle":"","transitName":"","transitLine":"","transitTime":"","transitDistance":"","buildingNum":"","clearHeight":"2.8","enterpriseActivities":"","floorType":"","stationType":"","remark":"","oderNum":880,"seatMonthPrice":0,"userId":0,"createUser":"752","updateUser":"752","createTime":1598605634,"updateTime":1602324556,"parkingSpaceRent":"2000"}
     * video : [{"id":46,"imgUrl":"https://img.officego.com/building/1591239116858.mp4"}]
     * vr : [{"id":188,"imgUrl":"https://img.officego.com/building/1594906208261.jpg"}]
     */

    @SerializedName("address")
    private String address;
    @SerializedName("buildingMsg")
    private BuildingMsgBean buildingMsg;
    @SerializedName("companyService")
    private List<CompanyServiceBean> companyService;
    @SerializedName("roomMatching")
    private List<RoomMatchingBean> roomMatching;
    @SerializedName("basicServices")
    private List<BasicServicesBean> basicServices;
    @SerializedName("banner")
    private List<BannerBean> banner;
    @SerializedName("video")
    private List<VideoBean> video;
    @SerializedName("vr")
    private List<VrBean> vr;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BuildingMsgBean getBuildingMsg() {
        return buildingMsg;
    }

    public void setBuildingMsg(BuildingMsgBean buildingMsg) {
        this.buildingMsg = buildingMsg;
    }

    public List<CompanyServiceBean> getCompanyService() {
        return companyService;
    }

    public void setCompanyService(List<CompanyServiceBean> companyService) {
        this.companyService = companyService;
    }

    public List<RoomMatchingBean> getRoomMatching() {
        return roomMatching;
    }

    public void setRoomMatching(List<RoomMatchingBean> roomMatching) {
        this.roomMatching = roomMatching;
    }

    public List<BasicServicesBean> getBasicServices() {
        return basicServices;
    }

    public void setBasicServices(List<BasicServicesBean> basicServices) {
        this.basicServices = basicServices;
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

    public static class BuildingMsgBean {
        /**
         * id : 7682
         * buildingNumber :
         * buildingType : 1
         * buildingName : 新百安大厦
         * branchesName :
         * mainPic : https://img.officego.com/building/1598841232107.jpg?x-oss-process=style/small
         * btype : 1
         * status : 1
         * address : 胶州路
         * longitude : 121.439420
         * latitude : 31.235292
         * buildingIntroduction :
         * refurbishedTime : 2020
         * buildingOccupancyRate :
         * supportFacilities :
         * completionTime : 1998
         * constructionArea :
         * houseCount : 0
         * totalFloor : 38
         * branchesTotalFloor :
         * storeyHeight : 2.8
         * passengerLift : 6
         * cargoLift : 2
         * airConditioning : 中央空调
         * airConditioningFee :
         * internet : 电信,联通,移动
         * property : 上海安国酒店有限公司
         * propertyCosts : 40
         * parkingSpace : 500
         * tags :
         * promoteSlogan :
         * basicServices :
         * corporateServices :
         * districtId : 5
         * businessDistrict : 34
         * nearbySubwayLine : 13,7
         * nearbySubway : 364,160
         * nearbySubwayTime : 10,8
         * nearbySubwayDistance : 855,629
         * releaseTime : 0
         * ishot : null
         * settlementLicence : 蓉昕国际贸易有限公司，森炜实业发展（上海）有限公司，莫博实业（上海）有限公司，上海后弦文化传媒工作室
         * freeRoom : 0
         * conferenceNumber : 0
         * conferencePeopleNumber : 0
         * roomMatching :
         * renovationStyle :
         * transitName :
         * transitLine :
         * transitTime :
         * transitDistance :
         * buildingNum :
         * clearHeight : 2.8
         * enterpriseActivities :
         * floorType :
         * stationType :
         * remark :
         * oderNum : 880
         * seatMonthPrice : 0
         * userId : 0
         * createUser : 752
         * updateUser : 752
         * createTime : 1598605634
         * updateTime : 1602324556
         * parkingSpaceRent : 2000
         */

        @SerializedName("id")
        private int id;
        @SerializedName("buildingNumber")
        private String buildingNumber;
        @SerializedName("buildingType")
        private int buildingType;
        @SerializedName("buildingName")
        private String buildingName;
        @SerializedName("branchesName")
        private String branchesName;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("btype")
        private int btype;
        @SerializedName("status")
        private int status;
        @SerializedName("address")
        private String address;
        @SerializedName("longitude")
        private String longitude;
        @SerializedName("latitude")
        private String latitude;
        @SerializedName("buildingIntroduction")
        private String buildingIntroduction;
        @SerializedName("refurbishedTime")
        private String refurbishedTime;
        @SerializedName("buildingOccupancyRate")
        private String buildingOccupancyRate;
        @SerializedName("supportFacilities")
        private String supportFacilities;
        @SerializedName("completionTime")
        private String completionTime;
        @SerializedName("constructionArea")
        private String constructionArea;
        @SerializedName("houseCount")
        private int houseCount;
        @SerializedName("totalFloor")
        private String totalFloor;
        @SerializedName("branchesTotalFloor")
        private String branchesTotalFloor;
        @SerializedName("storeyHeight")
        private String storeyHeight;
        @SerializedName("passengerLift")
        private String passengerLift;
        @SerializedName("cargoLift")
        private String cargoLift;
        @SerializedName("airConditioning")
        private String airConditioning;
        @SerializedName("airConditioningFee")
        private String airConditioningFee;
        @SerializedName("internet")
        private String internet;
        @SerializedName("property")
        private String property;
        @SerializedName("propertyCosts")
        private String propertyCosts;
        @SerializedName("parkingSpace")
        private String parkingSpace;
        @SerializedName("tags")
        private String tags;
        @SerializedName("promoteSlogan")
        private String promoteSlogan;
        @SerializedName("basicServices")
        private String basicServices;
        @SerializedName("corporateServices")
        private String corporateServices;
        @SerializedName("districtId")
        private String districtId;
        @SerializedName("businessDistrict")
        private String businessDistrict;
        @SerializedName("nearbySubwayLine")
        private String nearbySubwayLine;
        @SerializedName("nearbySubway")
        private String nearbySubway;
        @SerializedName("nearbySubwayTime")
        private String nearbySubwayTime;
        @SerializedName("nearbySubwayDistance")
        private String nearbySubwayDistance;
        @SerializedName("releaseTime")
        private int releaseTime;
        @SerializedName("ishot")
        private Object ishot;
        @SerializedName("settlementLicence")
        private String settlementLicence;
        @SerializedName("freeRoom")
        private int freeRoom;
        @SerializedName("conferenceNumber")
        private int conferenceNumber;
        @SerializedName("conferencePeopleNumber")
        private int conferencePeopleNumber;
        @SerializedName("roomMatching")
        private String roomMatching;
        @SerializedName("renovationStyle")
        private String renovationStyle;
        @SerializedName("transitName")
        private String transitName;
        @SerializedName("transitLine")
        private String transitLine;
        @SerializedName("transitTime")
        private String transitTime;
        @SerializedName("transitDistance")
        private String transitDistance;
        @SerializedName("buildingNum")
        private String buildingNum;
        @SerializedName("clearHeight")
        private String clearHeight;
        @SerializedName("enterpriseActivities")
        private String enterpriseActivities;
        @SerializedName("floorType")
        private String floorType;
        @SerializedName("stationType")
        private String stationType;
        @SerializedName("remark")
        private String remark;
        @SerializedName("oderNum")
        private int oderNum;
        @SerializedName("seatMonthPrice")
        private int seatMonthPrice;
        @SerializedName("userId")
        private int userId;
        @SerializedName("createUser")
        private String createUser;
        @SerializedName("updateUser")
        private String updateUser;
        @SerializedName("createTime")
        private int createTime;
        @SerializedName("updateTime")
        private int updateTime;
        @SerializedName("parkingSpaceRent")
        private String parkingSpaceRent;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBuildingNumber() {
            return buildingNumber;
        }

        public void setBuildingNumber(String buildingNumber) {
            this.buildingNumber = buildingNumber;
        }

        public int getBuildingType() {
            return buildingType;
        }

        public void setBuildingType(int buildingType) {
            this.buildingType = buildingType;
        }

        public String getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
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

        public int getBtype() {
            return btype;
        }

        public void setBtype(int btype) {
            this.btype = btype;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getBuildingIntroduction() {
            return buildingIntroduction;
        }

        public void setBuildingIntroduction(String buildingIntroduction) {
            this.buildingIntroduction = buildingIntroduction;
        }

        public String getRefurbishedTime() {
            return refurbishedTime;
        }

        public void setRefurbishedTime(String refurbishedTime) {
            this.refurbishedTime = refurbishedTime;
        }

        public String getBuildingOccupancyRate() {
            return buildingOccupancyRate;
        }

        public void setBuildingOccupancyRate(String buildingOccupancyRate) {
            this.buildingOccupancyRate = buildingOccupancyRate;
        }

        public String getSupportFacilities() {
            return supportFacilities;
        }

        public void setSupportFacilities(String supportFacilities) {
            this.supportFacilities = supportFacilities;
        }

        public String getCompletionTime() {
            return completionTime;
        }

        public void setCompletionTime(String completionTime) {
            this.completionTime = completionTime;
        }

        public String getConstructionArea() {
            return constructionArea;
        }

        public void setConstructionArea(String constructionArea) {
            this.constructionArea = constructionArea;
        }

        public int getHouseCount() {
            return houseCount;
        }

        public void setHouseCount(int houseCount) {
            this.houseCount = houseCount;
        }

        public String getTotalFloor() {
            return totalFloor;
        }

        public void setTotalFloor(String totalFloor) {
            this.totalFloor = totalFloor;
        }

        public String getBranchesTotalFloor() {
            return branchesTotalFloor;
        }

        public void setBranchesTotalFloor(String branchesTotalFloor) {
            this.branchesTotalFloor = branchesTotalFloor;
        }

        public String getStoreyHeight() {
            return storeyHeight;
        }

        public void setStoreyHeight(String storeyHeight) {
            this.storeyHeight = storeyHeight;
        }

        public String getPassengerLift() {
            return passengerLift;
        }

        public void setPassengerLift(String passengerLift) {
            this.passengerLift = passengerLift;
        }

        public String getCargoLift() {
            return cargoLift;
        }

        public void setCargoLift(String cargoLift) {
            this.cargoLift = cargoLift;
        }

        public String getAirConditioning() {
            return airConditioning;
        }

        public void setAirConditioning(String airConditioning) {
            this.airConditioning = airConditioning;
        }

        public String getAirConditioningFee() {
            return airConditioningFee;
        }

        public void setAirConditioningFee(String airConditioningFee) {
            this.airConditioningFee = airConditioningFee;
        }

        public String getInternet() {
            return internet;
        }

        public void setInternet(String internet) {
            this.internet = internet;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getPropertyCosts() {
            return propertyCosts;
        }

        public void setPropertyCosts(String propertyCosts) {
            this.propertyCosts = propertyCosts;
        }

        public String getParkingSpace() {
            return parkingSpace;
        }

        public void setParkingSpace(String parkingSpace) {
            this.parkingSpace = parkingSpace;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getPromoteSlogan() {
            return promoteSlogan;
        }

        public void setPromoteSlogan(String promoteSlogan) {
            this.promoteSlogan = promoteSlogan;
        }

        public String getBasicServices() {
            return basicServices;
        }

        public void setBasicServices(String basicServices) {
            this.basicServices = basicServices;
        }

        public String getCorporateServices() {
            return corporateServices;
        }

        public void setCorporateServices(String corporateServices) {
            this.corporateServices = corporateServices;
        }

        public String getDistrictId() {
            return districtId;
        }

        public void setDistrictId(String districtId) {
            this.districtId = districtId;
        }

        public String getBusinessDistrict() {
            return businessDistrict;
        }

        public void setBusinessDistrict(String businessDistrict) {
            this.businessDistrict = businessDistrict;
        }

        public String getNearbySubwayLine() {
            return nearbySubwayLine;
        }

        public void setNearbySubwayLine(String nearbySubwayLine) {
            this.nearbySubwayLine = nearbySubwayLine;
        }

        public String getNearbySubway() {
            return nearbySubway;
        }

        public void setNearbySubway(String nearbySubway) {
            this.nearbySubway = nearbySubway;
        }

        public String getNearbySubwayTime() {
            return nearbySubwayTime;
        }

        public void setNearbySubwayTime(String nearbySubwayTime) {
            this.nearbySubwayTime = nearbySubwayTime;
        }

        public String getNearbySubwayDistance() {
            return nearbySubwayDistance;
        }

        public void setNearbySubwayDistance(String nearbySubwayDistance) {
            this.nearbySubwayDistance = nearbySubwayDistance;
        }

        public int getReleaseTime() {
            return releaseTime;
        }

        public void setReleaseTime(int releaseTime) {
            this.releaseTime = releaseTime;
        }

        public Object getIshot() {
            return ishot;
        }

        public void setIshot(Object ishot) {
            this.ishot = ishot;
        }

        public String getSettlementLicence() {
            return settlementLicence;
        }

        public void setSettlementLicence(String settlementLicence) {
            this.settlementLicence = settlementLicence;
        }

        public int getFreeRoom() {
            return freeRoom;
        }

        public void setFreeRoom(int freeRoom) {
            this.freeRoom = freeRoom;
        }

        public int getConferenceNumber() {
            return conferenceNumber;
        }

        public void setConferenceNumber(int conferenceNumber) {
            this.conferenceNumber = conferenceNumber;
        }

        public int getConferencePeopleNumber() {
            return conferencePeopleNumber;
        }

        public void setConferencePeopleNumber(int conferencePeopleNumber) {
            this.conferencePeopleNumber = conferencePeopleNumber;
        }

        public String getRoomMatching() {
            return roomMatching;
        }

        public void setRoomMatching(String roomMatching) {
            this.roomMatching = roomMatching;
        }

        public String getRenovationStyle() {
            return renovationStyle;
        }

        public void setRenovationStyle(String renovationStyle) {
            this.renovationStyle = renovationStyle;
        }

        public String getTransitName() {
            return transitName;
        }

        public void setTransitName(String transitName) {
            this.transitName = transitName;
        }

        public String getTransitLine() {
            return transitLine;
        }

        public void setTransitLine(String transitLine) {
            this.transitLine = transitLine;
        }

        public String getTransitTime() {
            return transitTime;
        }

        public void setTransitTime(String transitTime) {
            this.transitTime = transitTime;
        }

        public String getTransitDistance() {
            return transitDistance;
        }

        public void setTransitDistance(String transitDistance) {
            this.transitDistance = transitDistance;
        }

        public String getBuildingNum() {
            return buildingNum;
        }

        public void setBuildingNum(String buildingNum) {
            this.buildingNum = buildingNum;
        }

        public String getClearHeight() {
            return clearHeight;
        }

        public void setClearHeight(String clearHeight) {
            this.clearHeight = clearHeight;
        }

        public String getEnterpriseActivities() {
            return enterpriseActivities;
        }

        public void setEnterpriseActivities(String enterpriseActivities) {
            this.enterpriseActivities = enterpriseActivities;
        }

        public String getFloorType() {
            return floorType;
        }

        public void setFloorType(String floorType) {
            this.floorType = floorType;
        }

        public String getStationType() {
            return stationType;
        }

        public void setStationType(String stationType) {
            this.stationType = stationType;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getOderNum() {
            return oderNum;
        }

        public void setOderNum(int oderNum) {
            this.oderNum = oderNum;
        }

        public int getSeatMonthPrice() {
            return seatMonthPrice;
        }

        public void setSeatMonthPrice(int seatMonthPrice) {
            this.seatMonthPrice = seatMonthPrice;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
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

        public String getParkingSpaceRent() {
            return parkingSpaceRent;
        }

        public void setParkingSpaceRent(String parkingSpaceRent) {
            this.parkingSpaceRent = parkingSpaceRent;
        }
    }

    public static class CompanyServiceBean {
        /**
         * dictValue : 6
         * dictImg : https://img.officego.com/dictionary/rlzp.png
         * dictCname : 人力招聘
         * dictImgBlack : https://img.officego.com/dictionary/1591169908538.png
         */

        @SerializedName("dictValue")
        private int dictValue;
        @SerializedName("dictImg")
        private String dictImg;
        @SerializedName("dictCname")
        private String dictCname;
        @SerializedName("dictImgBlack")
        private String dictImgBlack;

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

        public String getDictImgBlack() {
            return dictImgBlack;
        }

        public void setDictImgBlack(String dictImgBlack) {
            this.dictImgBlack = dictImgBlack;
        }
    }

    public static class RoomMatchingBean {
        /**
         * dictValue : 6
         * dictImg : https://img.officego.com/dictionary/rlzp.png
         * dictCname : 人力招聘
         * dictImgBlack : https://img.officego.com/dictionary/1591169908538.png
         */

        @SerializedName("dictValue")
        private int dictValue;
        @SerializedName("dictImg")
        private String dictImg;
        @SerializedName("dictCname")
        private String dictCname;
        @SerializedName("dictImgBlack")
        private String dictImgBlack;

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

        public String getDictImgBlack() {
            return dictImgBlack;
        }

        public void setDictImgBlack(String dictImgBlack) {
            this.dictImgBlack = dictImgBlack;
        }
    }

    public static class BasicServicesBean {
        /**
         * dictValue : 6
         * dictImg : https://img.officego.com/dictionary/rlzp.png
         * dictCname : 人力招聘
         * dictImgBlack : https://img.officego.com/dictionary/1591169908538.png
         */

        @SerializedName("dictValue")
        private int dictValue;
        @SerializedName("dictImg")
        private String dictImg;
        @SerializedName("dictCname")
        private String dictCname;
        @SerializedName("dictImgBlack")
        private String dictImgBlack;

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

        public String getDictImgBlack() {
            return dictImgBlack;
        }

        public void setDictImgBlack(String dictImgBlack) {
            this.dictImgBlack = dictImgBlack;
        }
    }

    public static class BannerBean {
        /**
         * id : 19725
         * imgType : null
         * typeId : null
         * imgUrl : https://img.officego.com/test/1600225233490.jpg?x-oss-process=style/small
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
         * id : 46
         * imgUrl : https://img.officego.com/building/1591239116858.mp4
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
         * id : 188
         * imgUrl : https://img.officego.com/building/1594906208261.jpg
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
