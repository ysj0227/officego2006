package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/12/10
 **/
public class CouponListBean {

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
         * couponType : 3
         * amountRange : 1000,1500
         * useRange : 1
         * batchCode : 859F7588CB5D472B
         * discount :
         * online : 1606902208
         * batchTitle : 减至券啊
         * batchId : 20201202174338
         * shelfLife : 2020.12.31 - 2020.12.02
         * status : 1
         */

        @SerializedName("discountMax")
        private String discountMax;
        @SerializedName("couponType")
        private int couponType;
        @SerializedName("amountRange")
        private String amountRange;
        @SerializedName("amountRangeText")
        private String amountRangeText;
        @SerializedName("useRange")
        private int useRange;
        @SerializedName("batchCode")
        private String batchCode;
        @SerializedName("discount")
        private String discount;
        @SerializedName("online")
        private int online;
        @SerializedName("batchTitle")
        private String batchTitle;
        @SerializedName("batchId")
        private String batchId;
        @SerializedName("shelfLife")
        private String shelfLife;
        @SerializedName("status")
        private int status;

        public String getDiscountMax() {
            return discountMax;
        }

        public void setDiscountMax(String discountMax) {
            this.discountMax = discountMax;
        }

        public int getCouponType() {
            return couponType;
        }

        public void setCouponType(int couponType) {
            this.couponType = couponType;
        }

        public String getAmountRange() {
            return amountRange;
        }

        public void setAmountRange(String amountRange) {
            this.amountRange = amountRange;
        }

        public int getUseRange() {
            return useRange;
        }

        public void setUseRange(int useRange) {
            this.useRange = useRange;
        }

        public String getBatchCode() {
            return batchCode;
        }

        public void setBatchCode(String batchCode) {
            this.batchCode = batchCode;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public String getBatchTitle() {
            return batchTitle;
        }

        public void setBatchTitle(String batchTitle) {
            this.batchTitle = batchTitle;
        }

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public String getShelfLife() {
            return shelfLife;
        }

        public void setShelfLife(String shelfLife) {
            this.shelfLife = shelfLife;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getAmountRangeText() {
            return amountRangeText;
        }

        public void setAmountRangeText(String amountRangeText) {
            this.amountRangeText = amountRangeText;
        }
    }
}
