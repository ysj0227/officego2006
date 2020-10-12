package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/28.
 * Descriptions:
 **/
public class DirectoryBean {


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
         * dictValue : 1
         * dictImg : url
         * dictCname : 办公家具
         */

        @SerializedName("dictValue")
        private int dictValue;
        @SerializedName("dictImg")
        private String dictImg;
        @SerializedName("dictCname")
        private String dictCname;

        public int getDictValue() {
            return dictValue;
        }

        public void setDictValue(int dictValue) {
            this.dictValue = dictValue;
        }

        public String getDictImg() {
            return dictImg;
        }

        public void setDictImg(String dictImg) {
            this.dictImg = dictImg;
        }

        public String getDictCname() {
            return dictCname;
        }

        public void setDictCname(String dictCname) {
            this.dictCname = dictCname;
        }
    }
}
