package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/10
 **/
public class CouponWriteOffListBean {

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
         * offline : 1609407811
         * discountMax : 300
         * batchCode : 859F7588CB5D472B
         * online : 1606902208
         * updateTime : 2020-12-09 19:54:11
         * batchTitle : 减至券啊
         * shelfLife : 2020.12.31 - 2020.12.02
         * title : 梦想加会议室(建科网B)
         * status : 6   //0:未启用,1:待使用,2:废弃,3:暂停,4:过期,5:冻结,6:已核销
         */

        @SerializedName("discountMax")
        private String discountMax;
        @SerializedName("discount")
        private String discount;
        @SerializedName("batchCode")
        private String batchCode;
        @SerializedName("online")
        private int online;
        @SerializedName("updateTime")
        private String updateTime;
        @SerializedName("batchTitle")
        private String batchTitle;
        @SerializedName("shelfLife")
        private String shelfLife;
        @SerializedName("title")
        private String title;
        @SerializedName("status")
        private int status;
        @SerializedName("couponType")
        private int couponType;

        public String getDiscountMax() {
            return discountMax;
        }

        public void setDiscountMax(String discountMax) {
            this.discountMax = discountMax;
        }

        public String getBatchCode() {
            return batchCode;
        }

        public void setBatchCode(String batchCode) {
            this.batchCode = batchCode;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getBatchTitle() {
            return batchTitle;
        }

        public void setBatchTitle(String batchTitle) {
            this.batchTitle = batchTitle;
        }

        public String getShelfLife() {
            return shelfLife;
        }

        public void setShelfLife(String shelfLife) {
            this.shelfLife = shelfLife;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCouponType() {
            return couponType;
        }

        public void setCouponType(int couponType) {
            this.couponType = couponType;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }
    }
}
