package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2021/1/4
 **/
public class HomeMeetingBean {

    /**
     * data : {"list":[{"img":"https://img.officego.com/test/1606297863997.png?x-oss-process=style/small","title":"post会议室","buildingId":4197,"buildingName":"金葵科技","availableTime":"post会议室\npost会议室","price":200,"id":2}],"meetingRoomLocation":"5"}
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
         * list : [{"img":"https://img.officego.com/test/1606297863997.png?x-oss-process=style/small","title":"post会议室","buildingId":4197,"buildingName":"金葵科技","availableTime":"post会议室\npost会议室","price":200,"id":2}]
         * meetingRoomLocation : 5
         */

        @SerializedName("meetingRoomLocation")
        private String meetingRoomLocation;
         @SerializedName("meetingRoomTitle")
        private String meetingRoomTitle;
        @SerializedName("list")
        private List<ListBean> list;

        public String getMeetingRoomLocation() {
            return meetingRoomLocation;
        }

        public void setMeetingRoomLocation(String meetingRoomLocation) {
            this.meetingRoomLocation = meetingRoomLocation;
        }

        public String getMeetingRoomTitle() {
            return meetingRoomTitle;
        }

        public void setMeetingRoomTitle(String meetingRoomTitle) {
            this.meetingRoomTitle = meetingRoomTitle;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * img : https://img.officego.com/test/1606297863997.png?x-oss-process=style/small
             * title : post会议室
             * buildingId : 4197
             * buildingName : 金葵科技
             * availableTime : post会议室
             post会议室
             * price : 200
             * id : 2
             */

            @SerializedName("img")
            private String img;
            @SerializedName("title")
            private String title;
            @SerializedName("buildingId")
            private Integer buildingId;
            @SerializedName("buildingName")
            private String buildingName;
            @SerializedName("availableTime")
            private String availableTime;
            @SerializedName("price")
            private Object price;
            @SerializedName("id")
            private Integer id;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Integer getBuildingId() {
                return buildingId;
            }

            public void setBuildingId(Integer buildingId) {
                this.buildingId = buildingId;
            }

            public String getBuildingName() {
                return buildingName;
            }

            public void setBuildingName(String buildingName) {
                this.buildingName = buildingName;
            }

            public String getAvailableTime() {
                return availableTime;
            }

            public void setAvailableTime(String availableTime) {
                this.availableTime = availableTime;
            }

            public Object getPrice() {
                return price;
            }

            public void setPrice(Object price) {
                this.price = price;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }
        }
    }
}
