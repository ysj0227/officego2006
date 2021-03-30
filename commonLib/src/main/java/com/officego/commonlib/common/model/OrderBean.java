package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2021/3/24
 **/
public class OrderBean {

    /**
     * amount : 6
     * creatAt : 1616579408
     * orderNo : 1000000792874156
     * endtime : 1619762227
     * id : 94
     * title : 森威大厦
     * type : 1个月（楼盘权益购买）
     * img : https://img.officego.com/test/1607932807584.jpg?x-oss-process=style/small
     * status : 2
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
        @SerializedName("amount")
        private Object amount;
        @SerializedName("creatAt")
        private int creatAt;
        @SerializedName("orderNo")
        private String orderNo;
        @SerializedName("endtime")
        private int endtime;
        @SerializedName("id")
        private int id;
        @SerializedName("title")
        private String title;
        @SerializedName("type")
        private String type;
        @SerializedName("img")
        private String img;
        @SerializedName("status")
        private int status;

        public Object getAmount() {
            return amount;
        }

        public void setAmount(Object amount) {
            this.amount = amount;
        }

        public int getCreatAt() {
            return creatAt;
        }

        public void setCreatAt(int creatAt) {
            this.creatAt = creatAt;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public int getEndtime() {
            return endtime;
        }

        public void setEndtime(int endtime) {
            this.endtime = endtime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
