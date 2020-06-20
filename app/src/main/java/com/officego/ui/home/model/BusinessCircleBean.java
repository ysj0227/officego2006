package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/1.
 * Descriptions:
 **/
public class BusinessCircleBean {

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
         * districtID : 1
         * district : 浦东新区
         * list : [{"area":"八佰伴","district":"浦东新区","id":1,"districtNum":1},{"area":"张江高科","district":"浦东新区","id":2,"districtNum":1},{"area":"外高桥","district":"浦东新区","id":3,"districtNum":1},{"area":"世纪公园","district":"浦东新区","id":4,"districtNum":1},{"area":"上南地区","district":"浦东新区","id":5,"districtNum":1},{"area":"浦东外环","district":"浦东新区","id":6,"districtNum":1},{"area":"陆家嘴","district":"浦东新区","id":7,"districtNum":1},{"area":"金桥","district":"浦东新区","id":8,"districtNum":1},{"area":"川沙","district":"浦东新区","id":9,"districtNum":1},{"area":"源深","district":"浦东新区","id":10,"districtNum":1},{"area":"塘桥/北蔡","district":"浦东新区","id":11,"districtNum":1},{"area":"三林","district":"浦东新区","id":12,"districtNum":1},{"area":"竹园商贸区","district":"浦东新区","id":13,"districtNum":1},{"area":"花木","district":"浦东新区","id":14,"districtNum":1}]
         */

        @SerializedName("districtID")
        private int districtID;
        @SerializedName("district")
        private String district;
        @SerializedName("list")
        private List<ListBean> list;

        public int getDistrictID() {
            return districtID;
        }

        public void setDistrictID(int districtID) {
            this.districtID = districtID;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * area : 八佰伴
             * district : 浦东新区
             * id : 1
             * districtNum : 1
             */

            @SerializedName("area")
            private String area;
            @SerializedName("district")
            private String district;
            @SerializedName("id")
            private int id;
            @SerializedName("districtNum")
            private int districtNum;

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getDistrict() {
                return district;
            }

            public void setDistrict(String district) {
                this.district = district;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getDistrictNum() {
                return districtNum;
            }

            public void setDistrictNum(int districtNum) {
                this.districtNum = districtNum;
            }
        }
    }
}
