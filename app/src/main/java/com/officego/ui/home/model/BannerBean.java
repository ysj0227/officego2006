package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/29.
 * Descriptions:
 **/
public class BannerBean {
    @SerializedName("data")
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * bannerName : 测试2
         * img : http://image.officego.com.cn/banner/1589785860634.jpg?x-oss-process=style/middle
         * type : 1
         * pageType : 1
         * pageId : 1
         * wurl :
         * remark : null
         * content :
         * position : 1
         * positionDuan : 1
         * duration : 3
         * ordernum : 2
         * online : 1589785226
         * offline : 1590822329
         * status : 1
         * createUser : null
         * updateUser : null
         * createTime : 1589785860
         * updateTime : 1589785860
         */

        @SerializedName("id")
        private int id;
        @SerializedName("bannerName")
        private String bannerName;
        @SerializedName("img")
        private String img;
        @SerializedName("type")
        private int type;
        @SerializedName("pageType")
        private int pageType;
        @SerializedName("pageId")
        private int pageId;
        @SerializedName("wurl")
        private String wurl;
        @SerializedName("remark")
        private Object remark;
        @SerializedName("content")
        private String content;
        @SerializedName("position")
        private int position;
        @SerializedName("positionDuan")
        private String positionDuan;
        @SerializedName("duration")
        private int duration;
        @SerializedName("ordernum")
        private int ordernum;
        @SerializedName("online")
        private int online;
        @SerializedName("offline")
        private int offline;
        @SerializedName("status")
        private int status;
        @SerializedName("createUser")
        private Object createUser;
        @SerializedName("updateUser")
        private Object updateUser;
        @SerializedName("createTime")
        private int createTime;
        @SerializedName("updateTime")
        private int updateTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBannerName() {
            return bannerName;
        }

        public void setBannerName(String bannerName) {
            this.bannerName = bannerName;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getPageType() {
            return pageType;
        }

        public void setPageType(int pageType) {
            this.pageType = pageType;
        }

        public int getPageId() {
            return pageId;
        }

        public void setPageId(int pageId) {
            this.pageId = pageId;
        }

        public String getWurl() {
            return wurl;
        }

        public void setWurl(String wurl) {
            this.wurl = wurl;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getPositionDuan() {
            return positionDuan;
        }

        public void setPositionDuan(String positionDuan) {
            this.positionDuan = positionDuan;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getOrdernum() {
            return ordernum;
        }

        public void setOrdernum(int ordernum) {
            this.ordernum = ordernum;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public int getOffline() {
            return offline;
        }

        public void setOffline(int offline) {
            this.offline = offline;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
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
    }
}
