package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/2.
 * Descriptions:
 **/
public class BuildingDetailsBean implements Serializable {


    /**
     * imgUrl : [{"imgUrl":"https://img.officego.com.cn/building/1590990890022.jpg","typeId":54,"remark":null,"id":506,"imgType":1}]
     * vrUrl : [{"imgUrl":"","typeId":54,"remark":null,"id":110,"imgType":1},{"imgUrl":"","typeId":54,"remark":null,"id":113,"imgType":1}]
     * videoUrl : [{"imgUrl":"https://img.officego.com.cn/building/1591239116858.mp4","typeId":54,"remark":null,"id":50,"imgType":1}]
     * IsFavorite : false
     * introduction : {"airConditioning":"中央空调(无需额外付费)","propertyCosts":"100","ParkingSpaceRent":"1000","passengerLift":20,"parkingSpace":"20","completionTime":915148800,"promoteSlogan":null,"cargoLift":10,"totalFloor":"30","property":"测试","storeyHeight":"10","constructionArea":"200","internet":"电信,联通"}
     * building : {"address":"龙阳路站","buildingId":54,"businessDistrict":"杨浦区-五角场","minDayPrice":200,"stationNames":["世纪公园","花木路"],"maxArea":200,"name":"恒源大楼","mainPic":"https://img.officego.com.cn/building/1590990890022.jpg","houseCount":1,"maxDayPrice":200,"stationline":["2","7"],"nearbySubwayTime":["1","14"],"stationColours":["#8DC11F","#ED6D00"],"minArea":200}
     * tags : [{"dictValue":1,"dictImg":"","dictCname":"免费停车","dictImgBlack":null}]
     * factorMap : {"buildingItem7":0,"buildingItem3":0,"buildingItem4":0,"buildingItem5":0,"buildingItem6":0,"buildingItem0":1,"buildingItem1":0,"buildingItem2":1}
     */

    @SerializedName("IsFavorite")
    private boolean IsFavorite;
    @SerializedName("introduction")
    private IntroductionBean introduction;
    @SerializedName("building")
    private BuildingBean building;
    @SerializedName("factorMap")
    private FactorMapBean factorMap;
    @SerializedName("imgUrl")
    private List<ImgUrlBean> imgUrl;
    @SerializedName("vrUrl")
    private List<VrUrlBean> vrUrl;
    @SerializedName("videoUrl")
    private List<VideoUrlBean> videoUrl;
    @SerializedName("tags")
    private List<TagsBean> tags;

    public boolean isIsFavorite() {
        return IsFavorite;
    }

    public void setIsFavorite(boolean IsFavorite) {
        this.IsFavorite = IsFavorite;
    }

    public IntroductionBean getIntroduction() {
        return introduction;
    }

    public void setIntroduction(IntroductionBean introduction) {
        this.introduction = introduction;
    }

    public BuildingBean getBuilding() {
        return building;
    }

    public void setBuilding(BuildingBean building) {
        this.building = building;
    }

    public FactorMapBean getFactorMap() {
        return factorMap;
    }

    public void setFactorMap(FactorMapBean factorMap) {
        this.factorMap = factorMap;
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

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public static class IntroductionBean implements Serializable{
        /**
         * airConditioning : 中央空调(无需额外付费)
         * propertyCosts : 100
         * ParkingSpaceRent : 1000
         * passengerLift : 20
         * parkingSpace : 20
         * completionTime : 915148800
         * promoteSlogan : null
         * cargoLift : 10
         * totalFloor : 30
         * property : 测试
         * storeyHeight : 10
         * constructionArea : 200
         * internet : 电信,联通
         */

        @SerializedName("airConditioning")
        private String airConditioning;
        @SerializedName("propertyCosts")
        private String propertyCosts;
        @SerializedName("ParkingSpaceRent")
        private String ParkingSpaceRent;
        @SerializedName("passengerLift")
        private String passengerLift;
        @SerializedName("parkingSpace")
        private String parkingSpace;
        @SerializedName("completionTime")
        private String completionTime;
        @SerializedName("promoteSlogan")
        private Object promoteSlogan;
        @SerializedName("cargoLift")
        private String cargoLift;
        @SerializedName("totalFloor")
        private String totalFloor;
        @SerializedName("property")
        private String property;
        @SerializedName("storeyHeight")
        private String storeyHeight;
        @SerializedName("constructionArea")
        private String constructionArea;
        @SerializedName("internet")
        private String internet;

        public String getAirConditioning() {
            return airConditioning;
        }

        public void setAirConditioning(String airConditioning) {
            this.airConditioning = airConditioning;
        }

        public String getPropertyCosts() {
            return propertyCosts;
        }

        public void setPropertyCosts(String propertyCosts) {
            this.propertyCosts = propertyCosts;
        }

        public String getParkingSpaceRent() {
            return ParkingSpaceRent;
        }

        public void setParkingSpaceRent(String ParkingSpaceRent) {
            this.ParkingSpaceRent = ParkingSpaceRent;
        }

        public String getPassengerLift() {
            return passengerLift;
        }

        public void setPassengerLift(String passengerLift) {
            this.passengerLift = passengerLift;
        }

        public String getParkingSpace() {
            return parkingSpace;
        }

        public void setParkingSpace(String parkingSpace) {
            this.parkingSpace = parkingSpace;
        }

        public String getCompletionTime() {
            return completionTime;
        }

        public void setCompletionTime(String completionTime) {
            this.completionTime = completionTime;
        }

        public Object getPromoteSlogan() {
            return promoteSlogan;
        }

        public void setPromoteSlogan(Object promoteSlogan) {
            this.promoteSlogan = promoteSlogan;
        }

        public String getCargoLift() {
            return cargoLift;
        }

        public void setCargoLift(String cargoLift) {
            this.cargoLift = cargoLift;
        }

        public String getTotalFloor() {
            return totalFloor;
        }

        public void setTotalFloor(String totalFloor) {
            this.totalFloor = totalFloor;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getStoreyHeight() {
            return storeyHeight;
        }

        public void setStoreyHeight(String storeyHeight) {
            this.storeyHeight = storeyHeight;
        }

        public String getConstructionArea() {
            return constructionArea;
        }

        public void setConstructionArea(String constructionArea) {
            this.constructionArea = constructionArea;
        }

        public String getInternet() {
            return internet;
        }

        public void setInternet(String internet) {
            this.internet = internet;
        }
    }

    public static class BuildingBean implements Serializable {
        /**
         * address : 龙阳路站
         * buildingId : 54
         * businessDistrict : 杨浦区-五角场
         * minDayPrice : 200
         * stationNames : ["世纪公园","花木路"]
         * maxArea : 200
         * name : 恒源大楼
         * mainPic : https://img.officego.com.cn/building/1590990890022.jpg
         * houseCount : 1
         * maxDayPrice : 200
         * stationline : ["2","7"]
         * nearbySubwayTime : ["1","14"]
         * stationColours : ["#8DC11F","#ED6D00"]
         * minArea : 200
         */

        @SerializedName("address")
        private String address;
        @SerializedName("buildingId")
        private int buildingId;
        @SerializedName("businessDistrict")
        private String businessDistrict;
        @SerializedName("minDayPrice")
        private Object minDayPrice;
        @SerializedName("maxArea")
        private Object maxArea;
        @SerializedName("maxDayPrice")
        private Object maxDayPrice;
        @SerializedName("minArea")
        private Object minArea;
        @SerializedName("name")
        private String name;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("houseCount")
        private int houseCount;
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

        public int getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(int buildingId) {
            this.buildingId = buildingId;
        }

        public String getBusinessDistrict() {
            return businessDistrict;
        }

        public void setBusinessDistrict(String businessDistrict) {
            this.businessDistrict = businessDistrict;
        }

        public Object getMinDayPrice() {
            return minDayPrice;
        }

        public void setMinDayPrice(Object minDayPrice) {
            this.minDayPrice = minDayPrice;
        }

        public Object getMaxArea() {
            return maxArea;
        }

        public void setMaxArea(Object maxArea) {
            this.maxArea = maxArea;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMainPic() {
            return mainPic;
        }

        public void setMainPic(String mainPic) {
            this.mainPic = mainPic;
        }

        public int getHouseCount() {
            return houseCount;
        }

        public void setHouseCount(int houseCount) {
            this.houseCount = houseCount;
        }

        public Object getMaxDayPrice() {
            return maxDayPrice;
        }

        public void setMaxDayPrice(Object maxDayPrice) {
            this.maxDayPrice = maxDayPrice;
        }

        public Object getMinArea() {
            return minArea;
        }

        public void setMinArea(Object minArea) {
            this.minArea = minArea;
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
    }

    public static class FactorMapBean implements Serializable{
        /**
         * buildingItem7 : 0
         * buildingItem3 : 0
         * buildingItem4 : 0
         * buildingItem5 : 0
         * buildingItem6 : 0
         * buildingItem0 : 1
         * buildingItem1 : 0
         * buildingItem2 : 1
         */

        @SerializedName("buildingItem7")
        private int buildingItem7;
        @SerializedName("buildingItem3")
        private int buildingItem3;
        @SerializedName("buildingItem4")
        private int buildingItem4;
        @SerializedName("buildingItem5")
        private int buildingItem5;
        @SerializedName("buildingItem6")
        private int buildingItem6;
        @SerializedName("buildingItem0")
        private int buildingItem0;
        @SerializedName("buildingItem1")
        private int buildingItem1;
        @SerializedName("buildingItem2")
        private int buildingItem2;

        public int getBuildingItem7() {
            return buildingItem7;
        }

        public void setBuildingItem7(int buildingItem7) {
            this.buildingItem7 = buildingItem7;
        }

        public int getBuildingItem3() {
            return buildingItem3;
        }

        public void setBuildingItem3(int buildingItem3) {
            this.buildingItem3 = buildingItem3;
        }

        public int getBuildingItem4() {
            return buildingItem4;
        }

        public void setBuildingItem4(int buildingItem4) {
            this.buildingItem4 = buildingItem4;
        }

        public int getBuildingItem5() {
            return buildingItem5;
        }

        public void setBuildingItem5(int buildingItem5) {
            this.buildingItem5 = buildingItem5;
        }

        public int getBuildingItem6() {
            return buildingItem6;
        }

        public void setBuildingItem6(int buildingItem6) {
            this.buildingItem6 = buildingItem6;
        }

        public int getBuildingItem0() {
            return buildingItem0;
        }

        public void setBuildingItem0(int buildingItem0) {
            this.buildingItem0 = buildingItem0;
        }

        public int getBuildingItem1() {
            return buildingItem1;
        }

        public void setBuildingItem1(int buildingItem1) {
            this.buildingItem1 = buildingItem1;
        }

        public int getBuildingItem2() {
            return buildingItem2;
        }

        public void setBuildingItem2(int buildingItem2) {
            this.buildingItem2 = buildingItem2;
        }
    }

    public static class ImgUrlBean implements Serializable{
        /**
         * imgUrl : https://img.officego.com.cn/building/1590990890022.jpg
         * typeId : 54
         * remark : null
         * id : 506
         * imgType : 1
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

    public static class VrUrlBean implements Serializable{
        /**
         * imgUrl :
         * typeId : 54
         * remark : null
         * id : 110
         * imgType : 1
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

    public static class VideoUrlBean implements Serializable{
        /**
         * imgUrl : https://img.officego.com.cn/building/1591239116858.mp4
         * typeId : 54
         * remark : null
         * id : 50
         * imgType : 1
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

    public static class TagsBean implements Serializable{
        /**
         * dictValue : 1
         * dictImg :
         * dictCname : 免费停车
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