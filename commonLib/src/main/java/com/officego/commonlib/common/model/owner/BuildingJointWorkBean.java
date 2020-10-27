package com.officego.commonlib.common.model.owner;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/10/26
 **/
public class BuildingJointWorkBean {


    /**
     * total : 0
     * pageSize : 10
     * totalPage : 0
     * pageNo : 1
     * prePage : 1
     * nextPage : 0
     * list : [{"buildingName":"星月·总部湾","IsBuildingManager":false,"isEdit":0,"btype":1,"isTemp":0,"buildingId":7193,"status":1}]
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
         * buildingName : 星月·总部湾
         * IsBuildingManager : false
         * isEdit : 0
         * btype : 1
         * isTemp : 0
         * buildingId : 7193
         * status : 1
         */

        @SerializedName("buildingName")
        private String buildingName;
        @SerializedName("IsBuildingManager")
        private boolean IsBuildingManager;
        @SerializedName("isEdit")
        private int isEdit;
        @SerializedName("btype")
        private int btype;
        @SerializedName("isTemp")
        private int isTemp;
        @SerializedName("buildingId")
        private int buildingId;
        @SerializedName("status")
        private int status;

        public String getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
        }

        public boolean isIsBuildingManager() {
            return IsBuildingManager;
        }

        public void setIsBuildingManager(boolean IsBuildingManager) {
            this.IsBuildingManager = IsBuildingManager;
        }

        public int getIsEdit() {
            return isEdit;
        }

        public void setIsEdit(int isEdit) {
            this.isEdit = isEdit;
        }

        public int getBtype() {
            return btype;
        }

        public void setBtype(int btype) {
            this.btype = btype;
        }

        public int getIsTemp() {
            return isTemp;
        }

        public void setIsTemp(int isTemp) {
            this.isTemp = isTemp;
        }

        public int getBuildingId() {
            return buildingId;
        }

        public void setBuildingId(int buildingId) {
            this.buildingId = buildingId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}