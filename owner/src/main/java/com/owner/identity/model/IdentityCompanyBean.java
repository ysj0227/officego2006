package com.owner.identity.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/7/14.
 * Descriptions:
 **/
public class IdentityCompanyBean {


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
         * buildingName : 恒源大楼
         * address : 上海市静安区胶州路699号
         * branchesName : null
         * company : 上海娃哈哈
         * bid : 80
         */

        @SerializedName("buildingName")
        private String buildingName;
        @SerializedName("address")
        private String address;
        @SerializedName("branchesName")
        private Object branchesName;
        @SerializedName("company")
        private String company;
        @SerializedName("bid")
        private int bid;

        public String getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getBranchesName() {
            return branchesName;
        }

        public void setBranchesName(Object branchesName) {
            this.branchesName = branchesName;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public int getBid() {
            return bid;
        }

        public void setBid(int bid) {
            this.bid = bid;
        }
    }
}
