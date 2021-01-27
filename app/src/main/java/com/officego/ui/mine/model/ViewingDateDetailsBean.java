package com.officego.ui.mine.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/9.
 * Descriptions:
 **/
public class ViewingDateDetailsBean {

    /**
     * house : [{"area":50,"houseId":1,"monthPrice":0,"dayPrice":0,"mainPic":"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1810958178,2297186643&fm=26&gp=0.jpgs","simple":1,"floor":"1","decoration":"毛胚房","seats":0,"scheduleId":5,"officeType":0}]
     * building : {"area":"400.0-400.0","address":"上海市昌平路广田大厦","dayPrice":"5.01-5.01","btype":1,"latitude":"31.23488","remark":null,"buildingId":5,"buildingName":"新百安大厦4","businessDistrict":"浦东新区·上南地区","phone":"15673185528","stationNames":["莘庄","江湾镇","五洲大道"],"contact":"用户5528","wxId":"","avatar":"","company":"","chatUserId":107,"branchesName":"","mainPic":"https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1810958178,2297186643&fm=26&gp=0.jpgs","auditStatus":0,"houseCount":1,"stationline":["1","3","6"],"time":1590394140,"nearbySubwayTime":["18","18","18"],"job":"test","stationColours":["#E70012","#FED700","#D34B9A"],"scheduleId":125,"longitude":"121.439629"}
     */

    @SerializedName("building")
    private BuildingBean building;
    @SerializedName("house")
    private List<HouseBean> house;

    public BuildingBean getBuilding() {
        return building;
    }

    public void setBuilding(BuildingBean building) {
        this.building = building;
    }

    public List<HouseBean> getHouse() {
        return house;
    }

    public void setHouse(List<HouseBean> house) {
        this.house = house;
    }

    public static class BuildingBean {
        /**
         * area : 400.0-400.0
         * address : 上海市昌平路广田大厦
         * dayPrice : 5.01-5.01
         * btype : 1
         * latitude : 31.23488
         * remark : null
         * buildingId : 5
         * buildingName : 新百安大厦4
         * businessDistrict : 浦东新区·上南地区
         * phone : 15673185528
         * stationNames : ["莘庄","江湾镇","五洲大道"]
         * contact : 用户5528
         * wxId :
         * avatar :
         * company :
         * chatUserId : 107
         * branchesName :
         * mainPic : https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1810958178,2297186643&fm=26&gp=0.jpgs
         * auditStatus : 0
         * houseCount : 1
         * stationline : ["1","3","6"]
         * time : 1590394140
         * nearbySubwayTime : ["18","18","18"]
         * job : test
         * stationColours : ["#E70012","#FED700","#D34B9A"]
         * scheduleId : 125
         * longitude : 121.439629
         */

        @SerializedName("area")
        private String area;
        @SerializedName("address")
        private String address;
        @SerializedName("dayPrice")
        private String dayPrice;
        @SerializedName("btype")
        private int btype;
        @SerializedName("latitude")
        private String latitude;
        @SerializedName("remark")
        private Object remark;
        @SerializedName("buildingId")
        private int buildingId;
        @SerializedName("buildingName")
        private String buildingName;
        @SerializedName("businessDistrict")
        private String businessDistrict;
        @SerializedName("phone")
        private String phone;
        @SerializedName("contact")
        private String contact;
        @SerializedName("wxId")
        private String wxId;
        @SerializedName("avatar")
        private String avatar;
        @SerializedName("company")
        private String company;
        @SerializedName("chatUserId")
        private int chatUserId;
        @SerializedName("branchesName")
        private String branchesName;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("auditStatus")
        private int auditStatus;
        @SerializedName("houseCount")
        private int houseCount;
        @SerializedName("time")
        private int time;
        @SerializedName("job")
        private String job;
        @SerializedName("scheduleId")
        private int scheduleId;
        @SerializedName("longitude")
        private String longitude;
        @SerializedName("stationNames")
        private List<String> stationNames;
        @SerializedName("stationline")
        private List<String> stationline;
        @SerializedName("nearbySubwayTime")
        private List<String> nearbySubwayTime;
        @SerializedName("stationColours")
        private List<String> stationColours;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDayPrice() {
            return dayPrice;
        }

        public void setDayPrice(String dayPrice) {
            this.dayPrice = dayPrice;
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

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getWxId() {
            return wxId;
        }

        public void setWxId(String wxId) {
            this.wxId = wxId;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public int getChatUserId() {
            return chatUserId;
        }

        public void setChatUserId(int chatUserId) {
            this.chatUserId = chatUserId;
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

        public int getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(int auditStatus) {
            this.auditStatus = auditStatus;
        }

        public int getHouseCount() {
            return houseCount;
        }

        public void setHouseCount(int houseCount) {
            this.houseCount = houseCount;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public int getScheduleId() {
            return scheduleId;
        }

        public void setScheduleId(int scheduleId) {
            this.scheduleId = scheduleId;
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

        public List<String> getStationColours() {
            return stationColours;
        }

        public void setStationColours(List<String> stationColours) {
            this.stationColours = stationColours;
        }
    }

    public static class HouseBean {
        /**
         * area : 50
         * houseId : 1
         * monthPrice : 0
         * dayPrice : 0
         * mainPic : https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1810958178,2297186643&fm=26&gp=0.jpgs
         * simple : 1
         * floor : 1
         * decoration : 毛胚房
         * seats : 0
         * scheduleId : 5
         * officeType : 0
         */

        @SerializedName("area")
        private Object area;
        @SerializedName("houseId")
        private int houseId;
        @SerializedName("dayPrice")
        private Object dayPrice;
        @SerializedName("mainPic")
        private String mainPic;
        @SerializedName("decoration")
        private String decoration;
        @SerializedName("seats")
        private int seats;

        public Object getArea() {
            return area;
        }

        public void setArea(Object area) {
            this.area = area;
        }

        public int getHouseId() {
            return houseId;
        }

        public void setHouseId(int houseId) {
            this.houseId = houseId;
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
    }
}
