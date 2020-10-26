package com.officego.commonlib.common.model.owner;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/26
 **/
public class BuildingJointWorkBean {


    /**
     * auditStatus : 0
     * page : {"total":0,"pageSize":10,"totalPage":0,"pageNo":1,"prePage":1,"nextPage":0,"list":[{"IsBuildingManager":false,"dayPrice":"2.4","perfect":"94%","btype":1,"yCount":"0","updateUser":"沈燕","updateTime":1602324495,"remark":"","buildingId":7193,"buildingName":"星月·总部湾","businessDistrict":"浦东-康桥/周浦","IsEdit":0,"nCount":0,"mainPic":"https://img.officego.com/building/1600309598640.jpg?x-oss-process=style/large","payStatus":1,"status":1}],"listids":[]}
     * companyVerify : 0
     */

    @SerializedName("auditStatus")
    private int auditStatus;
    @SerializedName("page")
    private PageBean page;
    @SerializedName("companyVerify")
    private int companyVerify;

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public int getCompanyVerify() {
        return companyVerify;
    }

    public void setCompanyVerify(int companyVerify) {
        this.companyVerify = companyVerify;
    }

    public static class PageBean {
        /**
         * total : 0
         * pageSize : 10
         * totalPage : 0
         * pageNo : 1
         * prePage : 1
         * nextPage : 0
         * list : [{"IsBuildingManager":false,"dayPrice":"2.4","perfect":"94%","btype":1,"yCount":"0","updateUser":"沈燕","updateTime":1602324495,"remark":"","buildingId":7193,"buildingName":"星月·总部湾","businessDistrict":"浦东-康桥/周浦","IsEdit":0,"nCount":0,"mainPic":"https://img.officego.com/building/1600309598640.jpg?x-oss-process=style/large","payStatus":1,"status":1}]
         * listids : []
         */

        @SerializedName("total")
        private int total;
        @SerializedName("pageSize")
        private int pageSize;
        @SerializedName("totalPage")
        private int totalPage;
        @SerializedName("pageNo")
        private int pageNo;
        @SerializedName("prePage")
        private int prePage;
        @SerializedName("nextPage")
        private int nextPage;
        @SerializedName("list")
        private List<ListBean> list;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getPrePage() {
            return prePage;
        }

        public void setPrePage(int prePage) {
            this.prePage = prePage;
        }

        public int getNextPage() {
            return nextPage;
        }

        public void setNextPage(int nextPage) {
            this.nextPage = nextPage;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * IsBuildingManager : false
             * dayPrice : 2.4
             * perfect : 94%
             * btype : 1
             * yCount : 0
             * updateUser : 沈燕
             * updateTime : 1602324495
             * remark :
             * buildingId : 7193
             * buildingName : 星月·总部湾
             * businessDistrict : 浦东-康桥/周浦
             * IsEdit : 0
             * nCount : 0
             * mainPic : https://img.officego.com/building/1600309598640.jpg?x-oss-process=style/large
             * payStatus : 1
             * status : 1
             */

            @SerializedName("IsBuildingManager")
            private boolean IsBuildingManager;
            @SerializedName("dayPrice")
            private String dayPrice;
            @SerializedName("perfect")
            private String perfect;
            @SerializedName("btype")
            private int btype;
            @SerializedName("yCount")
            private String yCount;
            @SerializedName("updateUser")
            private String updateUser;
            @SerializedName("updateTime")
            private int updateTime;
            @SerializedName("remark")
            private String remark;
            @SerializedName("buildingId")
            private Object buildingId;
            @SerializedName("buildingName")
            private String buildingName;
            @SerializedName("businessDistrict")
            private String businessDistrict;
            @SerializedName("IsEdit")
            private int IsEdit;
            @SerializedName("nCount")
            private int nCount;
            @SerializedName("mainPic")
            private String mainPic;
            @SerializedName("payStatus")
            private int payStatus;
            @SerializedName("status")
            private int status;

            public boolean isIsBuildingManager() {
                return IsBuildingManager;
            }

            public void setIsBuildingManager(boolean IsBuildingManager) {
                this.IsBuildingManager = IsBuildingManager;
            }

            public String getDayPrice() {
                return dayPrice;
            }

            public void setDayPrice(String dayPrice) {
                this.dayPrice = dayPrice;
            }

            public String getPerfect() {
                return perfect;
            }

            public void setPerfect(String perfect) {
                this.perfect = perfect;
            }

            public int getBtype() {
                return btype;
            }

            public void setBtype(int btype) {
                this.btype = btype;
            }

            public String getYCount() {
                return yCount;
            }

            public void setYCount(String yCount) {
                this.yCount = yCount;
            }

            public String getUpdateUser() {
                return updateUser;
            }

            public void setUpdateUser(String updateUser) {
                this.updateUser = updateUser;
            }

            public int getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(int updateTime) {
                this.updateTime = updateTime;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public Object getBuildingId() {
                return buildingId;
            }

            public void setBuildingId(Object buildingId) {
                this.buildingId = buildingId;
            }

            public String getBuildingName() {
                return buildingName;
            }

            public void setBuildingName(String buildingName) {
                this.buildingName = buildingName;
            }

            public String getBusinessDistrict() {
                return businessDistrict;
            }

            public void setBusinessDistrict(String businessDistrict) {
                this.businessDistrict = businessDistrict;
            }

            public int getIsEdit() {
                return IsEdit;
            }

            public void setIsEdit(int IsEdit) {
                this.IsEdit = IsEdit;
            }

            public int getNCount() {
                return nCount;
            }

            public void setNCount(int nCount) {
                this.nCount = nCount;
            }

            public String getMainPic() {
                return mainPic;
            }

            public void setMainPic(String mainPic) {
                this.mainPic = mainPic;
            }

            public int getPayStatus() {
                return payStatus;
            }

            public void setPayStatus(int payStatus) {
                this.payStatus = payStatus;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
