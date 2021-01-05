package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/31
 **/
public class HomeHotBean {

    /**
     * status : 200
     * message : success
     * data : {"total":0,"pageSize":10,"totalPage":0,"pageNo":1,"prePage":1,"nextPage":0,"list":[{"distance":"","btype":1,"remark":"","buildingMap":{"stationNames":["浦东国际机场"],"stationline":["2"],"nearbySubwayTime":["12"]},"independenceOffice":1,"mainPic":"https://img.officego.com/building/1592216154620.jpg?x-oss-process=style/small","id":4182,"seatMonthPrice":0,"address":"浦东国际机场","userId":0,"tags":[{"dictValue":9,"dictCname":"充电桩"},{"dictValue":10,"dictCname":"洗车服务"}],"businessDistrict":"浦东-其他","infotext":"房东1分钟前在线","minDayPrice":2.5,"name":"辉腾大楼","maxDayPrice":2.5,"openStation":0,"bannerMap":{"positionShow":4,"img":"https://img.officego.com/test/1609235684391.jpg?x-oss-process=style/orig","dayPrice":100,"distance":"","salePrice":null,"btype":2,"bannerName":"测试列表广告位","remark":null,"cityId":1,"type":1,"pageId":8527,"content":"","wurl":"","businessDistrict":"浦东-源深体育中心","pageType":1,"labelId":[{"dictValue":1,"dictCname":"标签"}],"name":" ABC（浦东）","layoutType":"1","id":19},"officeType":"1,0"}]}
     */

    @SerializedName("data")
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * total : 0
         * pageSize : 10
         * totalPage : 0
         * pageNo : 1
         * prePage : 1
         * nextPage : 0
         * list : [{"distance":"","btype":1,"remark":"","buildingMap":{"stationNames":["浦东国际机场"],"stationline":["2"],"nearbySubwayTime":["12"]},"independenceOffice":1,"mainPic":"https://img.officego.com/building/1592216154620.jpg?x-oss-process=style/small","id":4182,"seatMonthPrice":0,"address":"浦东国际机场","userId":0,"tags":[{"dictValue":9,"dictCname":"充电桩"},{"dictValue":10,"dictCname":"洗车服务"}],"businessDistrict":"浦东-其他","infotext":"房东1分钟前在线","minDayPrice":2.5,"name":"辉腾大楼","maxDayPrice":2.5,"openStation":0,"bannerMap":{"positionShow":4,"img":"https://img.officego.com/test/1609235684391.jpg?x-oss-process=style/orig","dayPrice":100,"distance":"","salePrice":null,"btype":2,"bannerName":"测试列表广告位","remark":null,"cityId":1,"type":1,"pageId":8527,"content":"","wurl":"","businessDistrict":"浦东-源深体育中心","pageType":1,"labelId":[{"dictValue":1,"dictCname":"标签"}],"name":" ABC（浦东）","layoutType":"1","id":19},"officeType":"1,0"}]
         */

        @SerializedName("list")
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * distance :
             * btype : 1
             * remark :
             * buildingMap : {"stationNames":["浦东国际机场"],"stationline":["2"],"nearbySubwayTime":["12"]}
             * independenceOffice : 1
             * mainPic : https://img.officego.com/building/1592216154620.jpg?x-oss-process=style/small
             * id : 4182
             * seatMonthPrice : 0
             * address : 浦东国际机场
             * userId : 0
             * tags : [{"dictValue":9,"dictCname":"充电桩"},{"dictValue":10,"dictCname":"洗车服务"}]
             * businessDistrict : 浦东-其他
             * infotext : 房东1分钟前在线
             * minDayPrice : 2.5
             * name : 辉腾大楼
             * maxDayPrice : 2.5
             * openStation : 0
             * bannerMap : {"positionShow":4,"img":"https://img.officego.com/test/1609235684391.jpg?x-oss-process=style/orig","dayPrice":100,"distance":"","salePrice":null,"btype":2,"bannerName":"测试列表广告位","remark":null,"cityId":1,"type":1,"pageId":8527,"content":"","wurl":"","businessDistrict":"浦东-源深体育中心","pageType":1,"labelId":[{"dictValue":1,"dictCname":"标签"}],"name":" ABC（浦东）","layoutType":"1","id":19}
             * officeType : 1,0
             */

            @SerializedName("distance")
            private String distance;
            @SerializedName("btype")
            private Integer btype;
            @SerializedName("remark")
            private String remark;
            @SerializedName("buildingMap")
            private BuildingMapBean buildingMap;
            @SerializedName("independenceOffice")
            private Integer independenceOffice;
            @SerializedName("mainPic")
            private String mainPic;
            @SerializedName("id")
            private Integer id;
            @SerializedName("seatMonthPrice")
            private Integer seatMonthPrice;
            @SerializedName("address")
            private String address;
            @SerializedName("userId")
            private Integer userId;
            @SerializedName("businessDistrict")
            private String businessDistrict;
            @SerializedName("infotext")
            private String infotext;
            @SerializedName("minDayPrice")
            private Object minDayPrice;
            @SerializedName("name")
            private String name;
            @SerializedName("maxDayPrice")
            private Object maxDayPrice;
            @SerializedName("openStation")
            private Integer openStation;
            @SerializedName("houseCount")
            private Integer houseCount;
            @SerializedName("bannerMap")
            private BannerMapBean bannerMap;
            @SerializedName("officeType")
            private String officeType;
            @SerializedName("vr")
            private String vr;
            @SerializedName("tags")
            private List<TagsBean> tags;

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public Integer getBtype() {
                return btype;
            }

            public void setBtype(Integer btype) {
                this.btype = btype;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public BuildingMapBean getBuildingMap() {
                return buildingMap;
            }

            public void setBuildingMap(BuildingMapBean buildingMap) {
                this.buildingMap = buildingMap;
            }

            public Integer getIndependenceOffice() {
                return independenceOffice;
            }

            public void setIndependenceOffice(Integer independenceOffice) {
                this.independenceOffice = independenceOffice;
            }

            public String getMainPic() {
                return mainPic;
            }

            public void setMainPic(String mainPic) {
                this.mainPic = mainPic;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Integer getSeatMonthPrice() {
                return seatMonthPrice;
            }

            public void setSeatMonthPrice(Integer seatMonthPrice) {
                this.seatMonthPrice = seatMonthPrice;
            }

            public String getVr() {
                return vr;
            }

            public void setVr(String vr) {
                this.vr = vr;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public Integer getUserId() {
                return userId;
            }

            public void setUserId(Integer userId) {
                this.userId = userId;
            }

            public String getBusinessDistrict() {
                return businessDistrict;
            }

            public void setBusinessDistrict(String businessDistrict) {
                this.businessDistrict = businessDistrict;
            }

            public String getInfotext() {
                return infotext;
            }

            public void setInfotext(String infotext) {
                this.infotext = infotext;
            }

            public Object getMinDayPrice() {
                return minDayPrice;
            }

            public void setMinDayPrice(Object minDayPrice) {
                this.minDayPrice = minDayPrice;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Object getMaxDayPrice() {
                return maxDayPrice;
            }

            public void setMaxDayPrice(Object maxDayPrice) {
                this.maxDayPrice = maxDayPrice;
            }

            public Integer getOpenStation() {
                return openStation;
            }

            public void setOpenStation(Integer openStation) {
                this.openStation = openStation;
            }

            public Integer getHouseCount() {
                return houseCount;
            }

            public void setHouseCount(Integer houseCount) {
                this.houseCount = houseCount;
            }

            public BannerMapBean getBannerMap() {
                return bannerMap;
            }

            public void setBannerMap(BannerMapBean bannerMap) {
                this.bannerMap = bannerMap;
            }

            public String getOfficeType() {
                return officeType;
            }

            public void setOfficeType(String officeType) {
                this.officeType = officeType;
            }

            public List<TagsBean> getTags() {
                return tags;
            }

            public void setTags(List<TagsBean> tags) {
                this.tags = tags;
            }

            public static class BuildingMapBean {
                @SerializedName("stationNames")
                private List<String> stationNames;
                @SerializedName("stationline")
                private List<String> stationline;
                @SerializedName("nearbySubwayTime")
                private List<String> nearbySubwayTime;

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
            }

            public static class BannerMapBean {
                /**
                 * positionShow : 4
                 * img : https://img.officego.com/test/1609235684391.jpg?x-oss-process=style/orig
                 * dayPrice : 100
                 * distance :
                 * salePrice : null
                 * btype : 2
                 * bannerName : 测试列表广告位
                 * remark : null
                 * cityId : 1
                 * type : 1
                 * pageId : 8527
                 * content :
                 * wurl :
                 * businessDistrict : 浦东-源深体育中心
                 * pageType : 1
                 * labelId : [{"dictValue":1,"dictCname":"标签"}]
                 * name :  ABC（浦东）
                 * layoutType : 1
                 * id : 19
                 */
                @SerializedName("img")
                private String img;
                @SerializedName("dayPrice")
                private Object dayPrice;
                @SerializedName("distance")
                private String distance;
                @SerializedName("salePrice")
                private Object salePrice;
                @SerializedName("btype")
                private Integer btype;
                @SerializedName("bannerName")
                private String bannerName;
                @SerializedName("remark")
                private Object remark;
                @SerializedName("cityId")
                private Integer cityId;
                @SerializedName("type")
                private Integer type;
                @SerializedName("pageId")
                private Integer pageId;
                @SerializedName("content")
                private String content;
                @SerializedName("wurl")
                private String wurl;
                @SerializedName("businessDistrict")
                private String businessDistrict;
                @SerializedName("pageType")
                private Integer pageType;
                @SerializedName("name")
                private String name;
                @SerializedName("layoutType")
                private String layoutType;
                @SerializedName("id")
                private Integer id;
                @SerializedName("labelId")
                private List<LabelIdBean> labelId;
                @SerializedName("imgList")
                private List<String> imgList;
                @SerializedName("subwayMap")
                private SubwayMapBean subwayMapBean;

                public SubwayMapBean getSubwayMapBean() {
                    return subwayMapBean;
                }

                public void setSubwayMapBean(SubwayMapBean subwayMapBean) {
                    this.subwayMapBean = subwayMapBean;
                }

                public List<String> getImgList() {
                    return imgList;
                }

                public void setImgList(List<String> imgList) {
                    this.imgList = imgList;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public Object getDayPrice() {
                    return dayPrice;
                }

                public void setDayPrice(Object dayPrice) {
                    this.dayPrice = dayPrice;
                }

                public String getDistance() {
                    return distance;
                }

                public void setDistance(String distance) {
                    this.distance = distance;
                }

                public Object getSalePrice() {
                    return salePrice;
                }

                public void setSalePrice(Object salePrice) {
                    this.salePrice = salePrice;
                }

                public Integer getBtype() {
                    return btype;
                }

                public void setBtype(Integer btype) {
                    this.btype = btype;
                }

                public String getBannerName() {
                    return bannerName;
                }

                public void setBannerName(String bannerName) {
                    this.bannerName = bannerName;
                }

                public Object getRemark() {
                    return remark;
                }

                public void setRemark(Object remark) {
                    this.remark = remark;
                }

                public Integer getCityId() {
                    return cityId;
                }

                public void setCityId(Integer cityId) {
                    this.cityId = cityId;
                }

                public Integer getType() {
                    return type;
                }

                public void setType(Integer type) {
                    this.type = type;
                }

                public Integer getPageId() {
                    return pageId;
                }

                public void setPageId(Integer pageId) {
                    this.pageId = pageId;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getWurl() {
                    return wurl;
                }

                public void setWurl(String wurl) {
                    this.wurl = wurl;
                }

                public String getBusinessDistrict() {
                    return businessDistrict;
                }

                public void setBusinessDistrict(String businessDistrict) {
                    this.businessDistrict = businessDistrict;
                }

                public Integer getPageType() {
                    return pageType;
                }

                public void setPageType(Integer pageType) {
                    this.pageType = pageType;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getLayoutType() {
                    return layoutType;
                }

                public void setLayoutType(String layoutType) {
                    this.layoutType = layoutType;
                }

                public Integer getId() {
                    return id;
                }

                public void setId(Integer id) {
                    this.id = id;
                }

                public List<LabelIdBean> getLabelId() {
                    return labelId;
                }

                public void setLabelId(List<LabelIdBean> labelId) {
                    this.labelId = labelId;
                }

                public static class SubwayMapBean {
                    @SerializedName("stationNames")
                    private List<String> stationNames;
                    @SerializedName("stationline")
                    private List<String> stationline;
                    @SerializedName("nearbySubwayTime")
                    private List<String> nearbySubwayTime;

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
                }

                public static class LabelIdBean {
                    /**
                     * dictValue : 1
                     * dictCname : 标签
                     */

                    @SerializedName("dictValue")
                    private Integer dictValue;
                    @SerializedName("dictCname")
                    private String dictCname;

                    public Integer getDictValue() {
                        return dictValue;
                    }

                    public void setDictValue(Integer dictValue) {
                        this.dictValue = dictValue;
                    }

                    public String getDictCname() {
                        return dictCname;
                    }

                    public void setDictCname(String dictCname) {
                        this.dictCname = dictCname;
                    }
                }
            }

            public static class TagsBean {
                /**
                 * dictValue : 9
                 * dictCname : 充电桩
                 */

                @SerializedName("dictValue")
                private Integer dictValue;
                @SerializedName("dictCname")
                private String dictCname;

                public Integer getDictValue() {
                    return dictValue;
                }

                public void setDictValue(Integer dictValue) {
                    this.dictValue = dictValue;
                }

                public String getDictCname() {
                    return dictCname;
                }

                public void setDictCname(String dictCname) {
                    this.dictCname = dictCname;
                }
            }
        }
    }
}
