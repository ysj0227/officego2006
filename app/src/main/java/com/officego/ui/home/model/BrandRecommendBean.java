package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/30
 **/
public class BrandRecommendBean {

    /**
     * data : [{"offline":1612079784,"img":"https://img.officego.com/building/1599040274318.jpg?x-oss-process=style/small","titleName":"2222","formulation":"","online":1609222702,"ordernum":0,"remark":"","id":1}]
     * totalCount : null
     */
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
         * offline : 1612079784
         * img : https://img.officego.com/building/1599040274318.jpg?x-oss-process=style/small
         * titleName : 2222
         * formulation :
         * online : 1609222702
         * ordernum : 0
         * remark :
         * id : 1
         */

        @SerializedName("offline")
        private Integer offline;
        @SerializedName("img")
        private String img;
        @SerializedName("titleName")
        private String titleName;
        @SerializedName("formulation")
        private String formulation;
        @SerializedName("online")
        private Integer online;
        @SerializedName("ordernum")
        private Integer ordernum;
        @SerializedName("remark")
        private String remark;
        @SerializedName("id")
        private Integer id;

        public Integer getOffline() {
            return offline;
        }

        public void setOffline(Integer offline) {
            this.offline = offline;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTitleName() {
            return titleName;
        }

        public void setTitleName(String titleName) {
            this.titleName = titleName;
        }

        public String getFormulation() {
            return formulation;
        }

        public void setFormulation(String formulation) {
            this.formulation = formulation;
        }

        public Integer getOnline() {
            return online;
        }

        public void setOnline(Integer online) {
            this.online = online;
        }

        public Integer getOrdernum() {
            return ordernum;
        }

        public void setOrdernum(Integer ordernum) {
            this.ordernum = ordernum;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }
}
