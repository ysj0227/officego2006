package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/11.
 * Descriptions:
 **/
public class ChatHouseBean {

    /**
     * isBuildOrHouse : 2
     * chatted : {"accountStatus":true,"targetId":"1031","IsZD":false,"nickname":"田小瘦","company":"","typeId":0,"avatar":"https://img.officego.com/head.png","job":"技术"}
     * createTime : 1591242886
     * scheduleStatus : 3
     * IsDepartureStatus : false
     * createUser : 用户5528
     * user : {"userImg":"https://img.officego.com/user/1591828495417.png","phone":"15673185528","wxId":null,"userId":104}
     * house : {"houseId":224,"address":"杨浦区上海浦东国际机场","btype":1,"latitude":"31.140815","dictCname":"带家具","buildingId":54,"buildingName":"恒源大楼","minSinglePrice":8,"stationNames":[],"maxArea":200,"district":"杨浦区-五角场","stationline":[],"nearbySubwayTime":[],"stationColours":[],"officeType":1,"longitude":"121.807559"}
     * IsFavorite : true
     */

    @SerializedName("isBuildOrHouse")
    private int isBuildOrHouse;
    @SerializedName("chatted")
    private ChattedBean chatted;
    @SerializedName("createTime")
    private Object createTime;
    @SerializedName("scheduleStatus")
    private int scheduleStatus;
    @SerializedName("IsDepartureStatus")
    private boolean IsDepartureStatus;
    @SerializedName("createUser")
    private String createUser;
    @SerializedName("user")
    private UserBean user;
    @SerializedName("building")
    private BuildingBean building;
    @SerializedName("IsFavorite")
    private boolean IsFavorite;
    @SerializedName("isChat")
    private int isChat;
    @SerializedName("house")
    private HouseBean house;

    public HouseBean getHouse() {
        return house;
    }

    public void setHouse(HouseBean house) {
        this.house = house;
    }
    public int getIsBuildOrHouse() {
        return isBuildOrHouse;
    }

    public void setIsBuildOrHouse(int isBuildOrHouse) {
        this.isBuildOrHouse = isBuildOrHouse;
    }

    public ChattedBean getChatted() {
        return chatted;
    }

    public void setChatted(ChattedBean chatted) {
        this.chatted = chatted;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public int getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(int scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public boolean isIsDepartureStatus() {
        return IsDepartureStatus;
    }

    public void setIsDepartureStatus(boolean IsDepartureStatus) {
        this.IsDepartureStatus = IsDepartureStatus;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public BuildingBean getBuilding() {
        return building;
    }

    public void setBuilding(BuildingBean building) {
        this.building = building;
    }

    public boolean isIsFavorite() {
        return IsFavorite;
    }

    public void setIsFavorite(boolean IsFavorite) {
        this.IsFavorite = IsFavorite;
    }

    public int getIsChat() {
        return isChat;
    }

    public void setIsChat(int isChat) {
        this.isChat = isChat;
    }

    public static class ChattedBean {
        /**
         * accountStatus : true
         * targetId : 1031
         * IsZD : false
         * nickname : 田小瘦
         * company :
         * typeId : 0
         * avatar : https://img.officego.com/head.png
         * job : 技术
         */

        @SerializedName("targetId")
        private String targetId;
        @SerializedName("IsZD")
        private boolean IsZD;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("company")
        private String company;
        @SerializedName("typeId")
        private int typeId;
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("job")
        private String job;

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }

        public boolean isIsZD() {
            return IsZD;
        }

        public void setIsZD(boolean IsZD) {
            this.IsZD = IsZD;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }
    }

    public static class UserBean {
        /**
         * userImg : https://img.officego.com/user/1591828495417.png
         * phone : 15673185528
         * wxId : null
         * userId : 104
         */

        @SerializedName("userImg")
        private String userImg;
        @SerializedName("phone")
        private String phone;
        @SerializedName("wxId")
        private Object wxId;
        @SerializedName("userId")
        private int userId;

        public String getUserImg() {
            return userImg;
        }

        public void setUserImg(String userImg) {
            this.userImg = userImg;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Object getWxId() {
            return wxId;
        }

        public void setWxId(Object wxId) {
            this.wxId = wxId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }

    public static class BuildingBean {
        /**
         * houseId : 224
         * address : 杨浦区上海浦东国际机场
         * btype : 1
         * latitude : 31.140815
         * dictCname : 带家具
         * buildingId : 54
         * buildingName : 恒源大楼
         * minSinglePrice : 8
         * stationNames : []
         * maxArea : 200
         * district : 杨浦区-五角场
         * stationline : []
         * nearbySubwayTime : []
         * stationColours : []
         * officeType : 1
         * longitude : 121.807559
         */

        @SerializedName("houseId")
        private Object houseId;
        @SerializedName("address")
        private String address;
        @SerializedName("btype")
        private int btype;
        @SerializedName("latitude")
        private String latitude;
        @SerializedName("buildingId")
        private int buildingId;
        @SerializedName("buildingName")
        private String buildingName;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("minSinglePrice")
        private Object minSinglePrice;
        @SerializedName("maxArea")
        private Object maxArea;
        @SerializedName("district")
        private String district;
        @SerializedName("longitude")
        private String longitude;
        @SerializedName("stationNames")
        private List<String> stationNames;
        @SerializedName("stationline")
        private List<String> stationline;
        @SerializedName("nearbySubwayTime")
        private List<String> nearbySubwayTime;
        @SerializedName("tags")
        private List<TagsBean> tags;

        public List<TagsBean> getTags() {
            return tags;
        }

        public void setTags(List<TagsBean> tags) {
            this.tags = tags;
        }

        public String getMainPic() {
            return mainPic;
        }

        public void setMainPic(String mainPic) {
            this.mainPic = mainPic;
        }

        public Object getHouseId() {
            return houseId;
        }

        public void setHouseId(Object houseId) {
            this.houseId = houseId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getBtype() {
            return btype;
        }

        public void setBtype(int btype) {
            this.btype = btype;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
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

        public Object getMinSinglePrice() {
            return minSinglePrice;
        }

        public void setMinSinglePrice(Object minSinglePrice) {
            this.minSinglePrice = minSinglePrice;
        }

        public Object getMaxArea() {
            return maxArea;
        }

        public void setMaxArea(Object maxArea) {
            this.maxArea = maxArea;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
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

        public static class TagsBean {
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

    public static class HouseBean {
        /**
         * houseId : 17189
         * address : 金科路
         * distance :
         * btype : 1
         * latitude : 31.204213
         * dictCname : 毛坯
         * tags : [{"dictValue":4,"dictImg":null,"dictCname":"金牌物业","dictImgBlack":null},{"dictValue":5,"dictImg":null,"dictCname":"区域地标","dictImgBlack":null}]
         * buildingId : 8649
         * buildingName : 王者峡谷1
         * houseName : 10.0m²•毛坯•王者峡谷1
         * minSinglePrice : 1200
         * stationNames : ["金科路"]
         * maxArea : 10
         * district : 浦东新区-其他
         * mainPic : https://img.officego.com/test/1597038923768.jpg?x-oss-process=style/small
         * stationline : ["2"]
         * nearbySubwayTime : ["1"]
         * officeType : 1
         * longitude : 121.601989
         */

        @SerializedName("houseId")
        private int houseId;
        @SerializedName("address")
        private String address;
        @SerializedName("distance")
        private String distance;
        @SerializedName("btype")
        private int btype;
        @SerializedName("latitude")
        private String latitude;
        @SerializedName("dictCname")
        private String dictCname;
        @SerializedName("buildingId")
        private int buildingId;
        @SerializedName("buildingName")
        private String buildingName;
        @SerializedName("houseName")
        private String houseName;
        @SerializedName("minSinglePrice")
        private Object minSinglePrice;
        @SerializedName("maxArea")
        private Object maxArea;
        @SerializedName("district")
        private String district;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("officeType")
        private int officeType;
        @SerializedName("longitude")
        private String longitude;
        @SerializedName("tags")
        private List<TagsBeanX> tags;
        @SerializedName("stationNames")
        private List<String> stationNames;
        @SerializedName("stationline")
        private List<String> stationline;
        @SerializedName("nearbySubwayTime")
        private List<String> nearbySubwayTime;

        public int getHouseId() {
            return houseId;
        }

        public void setHouseId(int houseId) {
            this.houseId = houseId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public int getBtype() {
            return btype;
        }

        public void setBtype(int btype) {
            this.btype = btype;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getDictCname() {
            return dictCname;
        }

        public void setDictCname(String dictCname) {
            this.dictCname = dictCname;
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

        public String getHouseName() {
            return houseName;
        }

        public void setHouseName(String houseName) {
            this.houseName = houseName;
        }

        public Object getMinSinglePrice() {
            return minSinglePrice;
        }

        public void setMinSinglePrice(Object minSinglePrice) {
            this.minSinglePrice = minSinglePrice;
        }

        public Object getMaxArea() {
            return maxArea;
        }

        public void setMaxArea(Object maxArea) {
            this.maxArea = maxArea;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
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

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public List<TagsBeanX> getTags() {
            return tags;
        }

        public void setTags(List<TagsBeanX> tags) {
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

        public static class TagsBeanX {
            /**
             * dictValue : 4
             * dictImg : null
             * dictCname : 金牌物业
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
