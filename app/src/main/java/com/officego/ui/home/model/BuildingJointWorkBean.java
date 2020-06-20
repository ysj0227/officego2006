package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/2.
 * Descriptions:
 **/
public class BuildingJointWorkBean implements Serializable {


    /**
     * imgUrl : [{"imgUrl":"https://img.officego.com.cn/building/1591000392743.jpg","typeId":57,"remark":null,"id":514,"imgType":1},{"imgUrl":"https://img.officego.com.cn/building/1591000407547.jpeg","typeId":57,"remark":null,"id":515,"imgType":1},{"imgUrl":"https://img.officego.com.cn/building/1591000399998.jpg","typeId":57,"remark":null,"id":516,"imgType":1}]
     * vrUrl : [{"imgUrl":"","typeId":57,"remark":null,"id":119,"imgType":1}]
     * videoUrl : [{"imgUrl":"https://img.officego.com.cn/building/1591239116858.mp4","typeId":57,"remark":null,"id":46,"imgType":1}]
     * IsFavorite : false
     * introduction : {"airConditioning":"中央空调","propertyCosts":"单独计费","ParkingSpaceRent":"1200","passengerLift":12,"parkingSpace":"22","completionTime":null,"promoteSlogan":null,"cargoLift":10,"totalFloor":null,"property":null,"storeyHeight":"60","constructionArea":null,"internet":"undefined电信,联通,移动"}
     * building : {"maxSeatsIndependentOffice":29,"address":"龙阳路2277号","minDayPriceOpenStation":3000,"minSeatsOpenStation":12,"maxAreaIndependentOffice":500,"maxDayPriceOpenStation":3000,"openStationFlag":true,"avgDayPriceIndependentOffice":300,"buildingId":57,"avgDayPriceOpenStation":3000,"businessDistrict":"浦东新区-八佰伴","minSeatsIndependentOffice":29,"corporateServices":[{"dictValue":2,"dictImg":"https://img.officego.com.cn/dictionary/gsnj.png","dictCname":"公司年检","dictImgBlack":"https://img.officego.com.cn/dictionary/1591169900199.png"},{"dictValue":6,"dictImg":"https://img.officego.com.cn/dictionary/rlzp.png","dictCname":"人力招聘","dictImgBlack":"https://img.officego.com.cn/dictionary/1591169908538.png"},{"dictValue":8,"dictImg":"https://img.officego.com.cn/dictionary/zsjz.png","dictCname":"知识讲座","dictImgBlack":"https://img.officego.com.cn/dictionary/1591169920296.png"}],"stationNames":["花木路"],"basicServices":[{"dictValue":1,"dictImg":"https://img.officego.com.cn/dictionary/bgjj.png","dictCname":"办公家具","dictImgBlack":"https://img.officego.com.cn/dictionary/1591169949924.png"},{"dictValue":2,"dictImg":"https://img.officego.com.cn/dictionary/kongt.png","dictCname":"中央空调","dictImgBlack":"https://img.officego.com.cn/dictionary/1591169962361.png"},{"dictValue":4,"dictImg":"https://img.officego.com.cn/dictionary/wifi.png","dictCname":"WIFI","dictImgBlack":"https://img.officego.com.cn/dictionary/1591169989118.png"},{"dictValue":6,"dictImg":"https://img.officego.com.cn/dictionary/weibolu.png","dictCname":"微波炉","dictImgBlack":"https://img.officego.com.cn/dictionary/1591170243883.png"}],"minAreaIndependentOffice":500,"name":"茸城","mainPic":"https://img.officego.com.cn/building/1591000392743.jpg","openStationMap":{"houseId":228,"minimumLease":"1","dayPrice":3000,"mainPic":"https://img.officego.com.cn/building/1591000546398.jpg","seats":12},"stationline":["7"],"nearbySubwayTime":["9"],"stationColours":["#ED6D00"],"maxSeatsOpenStation":12}
     * tags : [{"dictValue":1,"dictImg":null,"dictCname":"星巴克","dictImgBlack":null},{"dictValue":2,"dictImg":null,"dictCname":"地铁上盖","dictImgBlack":null},{"dictValue":3,"dictImg":null,"dictCname":"金牌物业","dictImgBlack":null},{"dictValue":4,"dictImg":null,"dictCname":"区域地标","dictImgBlack":null},{"dictValue":5,"dictImg":null,"dictCname":"城市地标","dictImgBlack":null},{"dictValue":6,"dictImg":null,"dictCname":"员工食堂","dictImgBlack":null},{"dictValue":7,"dictImg":null,"dictCname":"健身房","dictImgBlack":null},{"dictValue":8,"dictImg":null,"dictCname":"充电桩","dictImgBlack":null},{"dictValue":9,"dictImg":null,"dictCname":"洗车服务","dictImgBlack":null},{"dictValue":10,"dictImg":null,"dictCname":"虚拟注册","dictImgBlack":null},{"dictValue":11,"dictImg":null,"dictCname":"涉外网路","dictImgBlack":null},{"dictValue":12,"dictImg":null,"dictCname":"免费软饮","dictImgBlack":null},{"dictValue":13,"dictImg":null,"dictCname":"秘书服务","dictImgBlack":null}]
     * factorMap : {"jointworkItem6":0,"jointworkItem7":1,"jointworkItem0":1,"jointworkItem1":0,"jointworkItem2":0,"jointworkItem3":0,"jointworkItem4":0,"jointworkItem5":0}
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
         * airConditioning : 中央空调
         * propertyCosts : 单独计费
         * ParkingSpaceRent : 1200
         * passengerLift : 12
         * parkingSpace : 22
         * completionTime : null
         * promoteSlogan : null
         * cargoLift : 10
         * totalFloor : null
         * property : null
         * storeyHeight : 60
         * constructionArea : null
         * internet : undefined电信,联通,移动
         */

        @SerializedName("airConditioning")
        private String airConditioning;
        @SerializedName("propertyCosts")
        private String propertyCosts;
        @SerializedName("ParkingSpaceRent")
        private String ParkingSpaceRent;
        @SerializedName("passengerLift")
        private Object passengerLift;
        @SerializedName("parkingSpace")
        private String parkingSpace;
        @SerializedName("completionTime")
        private Object completionTime;
        @SerializedName("promoteSlogan")
        private Object promoteSlogan;
        @SerializedName("cargoLift")
        private Object cargoLift;
        @SerializedName("totalFloor")
        private Object totalFloor;
        @SerializedName("property")
        private Object property;
        @SerializedName("storeyHeight")
        private String storeyHeight;
        @SerializedName("constructionArea")
        private Object constructionArea;
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

        public Object getPassengerLift() {
            return passengerLift;
        }

        public void setPassengerLift(Object passengerLift) {
            this.passengerLift = passengerLift;
        }

        public String getParkingSpace() {
            return parkingSpace;
        }

        public void setParkingSpace(String parkingSpace) {
            this.parkingSpace = parkingSpace;
        }

        public Object getCompletionTime() {
            return completionTime;
        }

        public void setCompletionTime(Object completionTime) {
            this.completionTime = completionTime;
        }

        public Object getPromoteSlogan() {
            return promoteSlogan;
        }

        public void setPromoteSlogan(Object promoteSlogan) {
            this.promoteSlogan = promoteSlogan;
        }

        public Object getCargoLift() {
            return cargoLift;
        }

        public void setCargoLift(Object cargoLift) {
            this.cargoLift = cargoLift;
        }

        public Object getTotalFloor() {
            return totalFloor;
        }

        public void setTotalFloor(Object totalFloor) {
            this.totalFloor = totalFloor;
        }

        public Object getProperty() {
            return property;
        }

        public void setProperty(Object property) {
            this.property = property;
        }

        public String getStoreyHeight() {
            return storeyHeight;
        }

        public void setStoreyHeight(String storeyHeight) {
            this.storeyHeight = storeyHeight;
        }

        public Object getConstructionArea() {
            return constructionArea;
        }

        public void setConstructionArea(Object constructionArea) {
            this.constructionArea = constructionArea;
        }

        public String getInternet() {
            return internet;
        }

        public void setInternet(String internet) {
            this.internet = internet;
        }
    }

    public static class BuildingBean implements Serializable{
        /**
         * maxSeatsIndependentOffice : 29
         * address : 龙阳路2277号
         * minDayPriceOpenStation : 3000
         * minSeatsOpenStation : 12
         * maxAreaIndependentOffice : 500
         * maxDayPriceOpenStation : 3000
         * openStationFlag : true
         * avgDayPriceIndependentOffice : 300
         * buildingId : 57
         * avgDayPriceOpenStation : 3000
         * businessDistrict : 浦东新区-八佰伴
         * minSeatsIndependentOffice : 29
         * corporateServices : [{"dictValue":2,"dictImg":"https://img.officego.com.cn/dictionary/gsnj.png","dictCname":"公司年检","dictImgBlack":"https://img.officego.com.cn/dictionary/1591169900199.png"},{"dictValue":6,"dictImg":"https://img.officego.com.cn/dictionary/rlzp.png","dictCname":"人力招聘","dictImgBlack":"https://img.officego.com.cn/dictionary/1591169908538.png"},{"dictValue":8,"dictImg":"https://img.officego.com.cn/dictionary/zsjz.png","dictCname":"知识讲座","dictImgBlack":"https://img.officego.com.cn/dictionary/1591169920296.png"}]
         * stationNames : ["花木路"]
         * basicServices : [{"dictValue":1,"dictImg":"https://img.officego.com.cn/dictionary/bgjj.png","dictCname":"办公家具","dictImgBlack":"https://img.officego.com.cn/dictionary/1591169949924.png"},{"dictValue":2,"dictImg":"https://img.officego.com.cn/dictionary/kongt.png","dictCname":"中央空调","dictImgBlack":"https://img.officego.com.cn/dictionary/1591169962361.png"},{"dictValue":4,"dictImg":"https://img.officego.com.cn/dictionary/wifi.png","dictCname":"WIFI","dictImgBlack":"https://img.officego.com.cn/dictionary/1591169989118.png"},{"dictValue":6,"dictImg":"https://img.officego.com.cn/dictionary/weibolu.png","dictCname":"微波炉","dictImgBlack":"https://img.officego.com.cn/dictionary/1591170243883.png"}]
         * minAreaIndependentOffice : 500
         * name : 茸城
         * mainPic : https://img.officego.com.cn/building/1591000392743.jpg
         * openStationMap : {"houseId":228,"minimumLease":"1","dayPrice":3000,"mainPic":"https://img.officego.com.cn/building/1591000546398.jpg","seats":12}
         * stationline : ["7"]
         * nearbySubwayTime : ["9"]
         * stationColours : ["#ED6D00"]
         * maxSeatsOpenStation : 12
         */

        @SerializedName("maxSeatsIndependentOffice")
        private int maxSeatsIndependentOffice;
        @SerializedName("address")
        private String address;
        @SerializedName("minDayPriceOpenStation")
        private Object minDayPriceOpenStation;
        @SerializedName("minSeatsOpenStation")
        private int minSeatsOpenStation;
        @SerializedName("maxAreaIndependentOffice")
        private Object maxAreaIndependentOffice;
        @SerializedName("maxDayPriceOpenStation")
        private Object maxDayPriceOpenStation;
        @SerializedName("openStationFlag")
        private boolean openStationFlag;
        @SerializedName("avgDayPriceIndependentOffice")
        private Object avgDayPriceIndependentOffice;
        @SerializedName("buildingId")
        private int buildingId;
        @SerializedName("avgDayPriceOpenStation")
        private Object avgDayPriceOpenStation;
        @SerializedName("businessDistrict")
        private String businessDistrict;
        @SerializedName("minSeatsIndependentOffice")
        private int minSeatsIndependentOffice;
        @SerializedName("minAreaIndependentOffice")
        private Object minAreaIndependentOffice;
        @SerializedName("name")
        private String name;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("openStationMap")
        private OpenStationMapBean openStationMap;
        @SerializedName("maxSeatsOpenStation")
        private int maxSeatsOpenStation;
        @SerializedName("corporateServices")
        private List<CorporateServicesBean> corporateServices;
        @SerializedName("stationNames")
        private List<String> stationNames;
        @SerializedName("basicServices")
        private List<BasicServicesBean> basicServices;
        @SerializedName("stationline")
        private List<String> stationline;
        @SerializedName("nearbySubwayTime")
        private List<String> nearbySubwayTime;
        @SerializedName("stationColours")
        private List<String> stationColours;

        public int getMaxSeatsIndependentOffice() {
            return maxSeatsIndependentOffice;
        }

        public void setMaxSeatsIndependentOffice(int maxSeatsIndependentOffice) {
            this.maxSeatsIndependentOffice = maxSeatsIndependentOffice;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getMinDayPriceOpenStation() {
            return minDayPriceOpenStation;
        }

        public void setMinDayPriceOpenStation(Object minDayPriceOpenStation) {
            this.minDayPriceOpenStation = minDayPriceOpenStation;
        }

        public int getMinSeatsOpenStation() {
            return minSeatsOpenStation;
        }

        public void setMinSeatsOpenStation(int minSeatsOpenStation) {
            this.minSeatsOpenStation = minSeatsOpenStation;
        }

        public Object getMaxAreaIndependentOffice() {
            return maxAreaIndependentOffice;
        }

        public void setMaxAreaIndependentOffice(Object maxAreaIndependentOffice) {
            this.maxAreaIndependentOffice = maxAreaIndependentOffice;
        }

        public Object getMaxDayPriceOpenStation() {
            return maxDayPriceOpenStation;
        }

        public void setMaxDayPriceOpenStation(Object maxDayPriceOpenStation) {
            this.maxDayPriceOpenStation = maxDayPriceOpenStation;
        }

        public boolean isOpenStationFlag() {
            return openStationFlag;
        }

        public void setOpenStationFlag(boolean openStationFlag) {
            this.openStationFlag = openStationFlag;
        }

        public Object getAvgDayPriceIndependentOffice() {
            return avgDayPriceIndependentOffice;
        }

        public void setAvgDayPriceIndependentOffice(Object avgDayPriceIndependentOffice) {
            this.avgDayPriceIndependentOffice = avgDayPriceIndependentOffice;
        }

        public int getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(int buildingId) {
            this.buildingId = buildingId;
        }

        public Object getAvgDayPriceOpenStation() {
            return avgDayPriceOpenStation;
        }

        public void setAvgDayPriceOpenStation(Object avgDayPriceOpenStation) {
            this.avgDayPriceOpenStation = avgDayPriceOpenStation;
        }

        public String getBusinessDistrict() {
            return businessDistrict;
        }

        public void setBusinessDistrict(String businessDistrict) {
            this.businessDistrict = businessDistrict;
        }

        public int getMinSeatsIndependentOffice() {
            return minSeatsIndependentOffice;
        }

        public void setMinSeatsIndependentOffice(int minSeatsIndependentOffice) {
            this.minSeatsIndependentOffice = minSeatsIndependentOffice;
        }

        public Object getMinAreaIndependentOffice() {
            return minAreaIndependentOffice;
        }

        public void setMinAreaIndependentOffice(Object minAreaIndependentOffice) {
            this.minAreaIndependentOffice = minAreaIndependentOffice;
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

        public OpenStationMapBean getOpenStationMap() {
            return openStationMap;
        }

        public void setOpenStationMap(OpenStationMapBean openStationMap) {
            this.openStationMap = openStationMap;
        }

        public int getMaxSeatsOpenStation() {
            return maxSeatsOpenStation;
        }

        public void setMaxSeatsOpenStation(int maxSeatsOpenStation) {
            this.maxSeatsOpenStation = maxSeatsOpenStation;
        }

        public List<CorporateServicesBean> getCorporateServices() {
            return corporateServices;
        }

        public void setCorporateServices(List<CorporateServicesBean> corporateServices) {
            this.corporateServices = corporateServices;
        }

        public List<String> getStationNames() {
            return stationNames;
        }

        public void setStationNames(List<String> stationNames) {
            this.stationNames = stationNames;
        }

        public List<BasicServicesBean> getBasicServices() {
            return basicServices;
        }

        public void setBasicServices(List<BasicServicesBean> basicServices) {
            this.basicServices = basicServices;
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

        public static class OpenStationMapBean implements Serializable{
            /**
             * houseId : 228
             * minimumLease : 1
             * dayPrice : 3000
             * mainPic : https://img.officego.com.cn/building/1591000546398.jpg
             * seats : 12
             */

            @SerializedName("houseId")
            private int houseId;
            @SerializedName("minimumLease")
            private String minimumLease;
            @SerializedName("dayPrice")
            private Object dayPrice;
            @SerializedName("mainPic")
            private String mainPic;
            @SerializedName("seats")
            private int seats;

            public int getHouseId() {
                return houseId;
            }

            public void setHouseId(int houseId) {
                this.houseId = houseId;
            }

            public String getMinimumLease() {
                return minimumLease;
            }

            public void setMinimumLease(String minimumLease) {
                this.minimumLease = minimumLease;
            }

            public Object getDayPrice() {
                return dayPrice;
            }

            public void setDayPrice(Object dayPrice) {
                this.dayPrice = dayPrice;
            }

            public String getMainPic() {
                return mainPic;
            }

            public void setMainPic(String mainPic) {
                this.mainPic = mainPic;
            }

            public int getSeats() {
                return seats;
            }

            public void setSeats(int seats) {
                this.seats = seats;
            }
        }

        public static class CorporateServicesBean implements Serializable{
            /**
             * dictValue : 2
             * dictImg : https://img.officego.com.cn/dictionary/gsnj.png
             * dictCname : 公司年检
             * dictImgBlack : https://img.officego.com.cn/dictionary/1591169900199.png
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

        public static class BasicServicesBean implements Serializable{
            /**
             * dictValue : 1
             * dictImg : https://img.officego.com.cn/dictionary/bgjj.png
             * dictCname : 办公家具
             * dictImgBlack : https://img.officego.com.cn/dictionary/1591169949924.png
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
    }

    public static class FactorMapBean implements Serializable{
        /**
         * jointworkItem6 : 0
         * jointworkItem7 : 1
         * jointworkItem0 : 1
         * jointworkItem1 : 0
         * jointworkItem2 : 0
         * jointworkItem3 : 0
         * jointworkItem4 : 0
         * jointworkItem5 : 0
         */

        @SerializedName("jointworkItem6")
        private int jointworkItem6;
        @SerializedName("jointworkItem7")
        private int jointworkItem7;
        @SerializedName("jointworkItem0")
        private int jointworkItem0;
        @SerializedName("jointworkItem1")
        private int jointworkItem1;
        @SerializedName("jointworkItem2")
        private int jointworkItem2;
        @SerializedName("jointworkItem3")
        private int jointworkItem3;
        @SerializedName("jointworkItem4")
        private int jointworkItem4;
        @SerializedName("jointworkItem5")
        private int jointworkItem5;

        public int getJointworkItem6() {
            return jointworkItem6;
        }

        public void setJointworkItem6(int jointworkItem6) {
            this.jointworkItem6 = jointworkItem6;
        }

        public int getJointworkItem7() {
            return jointworkItem7;
        }

        public void setJointworkItem7(int jointworkItem7) {
            this.jointworkItem7 = jointworkItem7;
        }

        public int getJointworkItem0() {
            return jointworkItem0;
        }

        public void setJointworkItem0(int jointworkItem0) {
            this.jointworkItem0 = jointworkItem0;
        }

        public int getJointworkItem1() {
            return jointworkItem1;
        }

        public void setJointworkItem1(int jointworkItem1) {
            this.jointworkItem1 = jointworkItem1;
        }

        public int getJointworkItem2() {
            return jointworkItem2;
        }

        public void setJointworkItem2(int jointworkItem2) {
            this.jointworkItem2 = jointworkItem2;
        }

        public int getJointworkItem3() {
            return jointworkItem3;
        }

        public void setJointworkItem3(int jointworkItem3) {
            this.jointworkItem3 = jointworkItem3;
        }

        public int getJointworkItem4() {
            return jointworkItem4;
        }

        public void setJointworkItem4(int jointworkItem4) {
            this.jointworkItem4 = jointworkItem4;
        }

        public int getJointworkItem5() {
            return jointworkItem5;
        }

        public void setJointworkItem5(int jointworkItem5) {
            this.jointworkItem5 = jointworkItem5;
        }
    }

    public static class ImgUrlBean implements Serializable{
        /**
         * imgUrl : https://img.officego.com.cn/building/1591000392743.jpg
         * typeId : 57
         * remark : null
         * id : 514
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
         * typeId : 57
         * remark : null
         * id : 119
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
         * typeId : 57
         * remark : null
         * id : 46
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
