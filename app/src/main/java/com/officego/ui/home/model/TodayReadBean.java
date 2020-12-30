package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/30
 **/
public class TodayReadBean {


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
         * img : https://img.officego.com/test/1609308851180.jpg?x-oss-process=style/small
         * positionDuan : 0
         * ordernum : 1
         * type : 3
         * pageId : null
         * wurl : https://www.baidu.com
         * subTitleName : 老铁们！别慌小场面！
         * titleName : #午间小憩#
         * pageType : null
         * id : 1
         */

        @SerializedName("img")
        private String img;
        @SerializedName("positionDuan")
        private String positionDuan;
        @SerializedName("ordernum")
        private Integer ordernum;
        @SerializedName("type")
        private Integer type;
        @SerializedName("pageId")
        private Object pageId;
        @SerializedName("wurl")
        private String wurl;
        @SerializedName("subTitleName")
        private String subTitleName;
        @SerializedName("titleName")
        private String titleName;
        @SerializedName("pageType")
        private Object pageType;
        @SerializedName("id")
        private Integer id;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getPositionDuan() {
            return positionDuan;
        }

        public void setPositionDuan(String positionDuan) {
            this.positionDuan = positionDuan;
        }

        public Integer getOrdernum() {
            return ordernum;
        }

        public void setOrdernum(Integer ordernum) {
            this.ordernum = ordernum;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Object getPageId() {
            return pageId;
        }

        public void setPageId(Object pageId) {
            this.pageId = pageId;
        }

        public String getWurl() {
            return wurl;
        }

        public void setWurl(String wurl) {
            this.wurl = wurl;
        }

        public String getSubTitleName() {
            return subTitleName;
        }

        public void setSubTitleName(String subTitleName) {
            this.subTitleName = subTitleName;
        }

        public String getTitleName() {
            return titleName;
        }

        public void setTitleName(String titleName) {
            this.titleName = titleName;
        }

        public Object getPageType() {
            return pageType;
        }

        public void setPageType(Object pageType) {
            this.pageType = pageType;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }
}
