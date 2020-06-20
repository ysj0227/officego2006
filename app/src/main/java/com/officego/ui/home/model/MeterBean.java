package com.officego.ui.home.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/12.
 * Descriptions:
 **/
public class MeterBean {

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
         * line : 1号线
         * lid : 1
         * list : [{"stationName":"莘庄","id":1,"stationColour":"#E70012"},{"stationName":"外环路","id":2,"stationColour":"#E70012"}]
         */

        @SerializedName("line")
        private String line;
        @SerializedName("lid")
        private String lid;
        @SerializedName("list")
        private List<ListBean> list;

        public String getLine() {
            return line;
        }

        public void setLine(String line) {
            this.line = line;
        }

        public String getLid() {
            return lid;
        }

        public void setLid(String lid) {
            this.lid = lid;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * stationName : 莘庄
             * id : 1
             * stationColour : #E70012
             */

            @SerializedName("stationName")
            private String stationName;
            @SerializedName("id")
            private int id;
            @SerializedName("stationColour")
            private String stationColour;

            public String getStationName() {
                return stationName;
            }

            public void setStationName(String stationName) {
                this.stationName = stationName;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getStationColour() {
                return stationColour;
            }

            public void setStationColour(String stationColour) {
                this.stationColour = stationColour;
            }
        }
    }
}
