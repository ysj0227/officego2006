package com.owner.schedule.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/9.
 * Descriptions:
 **/
public class ViewingDateBean {

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
         * week : 周四
         * scheduleList : [{"area":"22㎡","address":"上海市徐汇区漕河泾开发区(地铁站)","btype":2,"remark":"备注","simple":"12人间","buildingId":56,"buildingName":"TEST大厦2","businessDistrict":"徐汇区·漕河泾","phone":"18516765366","contact":"飞姐姐","branchesName":"TEST网点","mainPic":"https://img.officego.com.cn/building/1590999378530.jpg","auditStatus":1,"time":1591856280,"job":"测试2","scheduleId":184},{"area":"500㎡","address":"上海市松江区九亭金地广场","btype":2,"remark":"备注","simple":"29人间","buildingId":57,"buildingName":"茸城大厦","businessDistrict":"松江区·九亭","phone":"18237774543","contact":"李拓取","branchesName":"茸城","mainPic":"https://img.officego.com.cn/building/1591000392743.jpg","auditStatus":1,"time":1591870920,"job":"开发","scheduleId":190}]
         * day : 2020-06-11
         */

        @SerializedName("week")
        private String week;
        @SerializedName("day")
        private String day;
        @SerializedName("scheduleList")
        private List<ScheduleListBean> scheduleList;

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public List<ScheduleListBean> getScheduleList() {
            return scheduleList;
        }

        public void setScheduleList(List<ScheduleListBean> scheduleList) {
            this.scheduleList = scheduleList;
        }

        public static class ScheduleListBean {
            /**
             * area : 22㎡
             * address : 上海市徐汇区漕河泾开发区(地铁站)
             * btype : 2
             * remark : 备注
             * simple : 12人间
             * buildingId : 56
             * buildingName : TEST大厦2
             * businessDistrict : 徐汇区·漕河泾
             * phone : 18516765366
             * contact : 飞姐姐
             * branchesName : TEST网点
             * mainPic : https://img.officego.com.cn/building/1590999378530.jpg
             * auditStatus : 1
             * time : 1591856280
             * job : 测试2
             * scheduleId : 184
             */

            @SerializedName("area")
            private String area;
            @SerializedName("address")
            private String address;
            @SerializedName("btype")
            private int btype;
            @SerializedName("remark")
            private String remark;
            @SerializedName("simple")
            private String simple;
            @SerializedName("buildingId")
            private int buildingId;
            @SerializedName("buildingName")
            private String buildingName;
            @SerializedName("businessDistrict")
            private String businessDistrict;
            @SerializedName("phone")
            private String phone;
            @SerializedName("contact")
            private String contact;
            @SerializedName("branchesName")
            private String branchesName;
            @SerializedName("mainPic")
            private String mainPic;
            @SerializedName("auditStatus")
            private int auditStatus;
            @SerializedName("time")
            private int time;
            @SerializedName("job")
            private String job;
            @SerializedName("scheduleId")
            private int scheduleId;

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getBtype() {
                return btype;
            }

            public void setBtype(int btype) {
                this.btype = btype;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getSimple() {
                return simple;
            }

            public void setSimple(String simple) {
                this.simple = simple;
            }

            public int getBuildingId() {
                return buildingId;
            }

            public void setBuildingId(int buildingId) {
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

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getContact() {
                return contact;
            }

            public void setContact(String contact) {
                this.contact = contact;
            }

            public String getBranchesName() {
                return branchesName;
            }

            public void setBranchesName(String branchesName) {
                this.branchesName = branchesName;
            }

            public String getMainPic() {
                return mainPic;
            }

            public void setMainPic(String mainPic) {
                this.mainPic = mainPic;
            }

            public int getAuditStatus() {
                return auditStatus;
            }

            public void setAuditStatus(int auditStatus) {
                this.auditStatus = auditStatus;
            }

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }

            public String getJob() {
                return job;
            }

            public void setJob(String job) {
                this.job = job;
            }

            public int getScheduleId() {
                return scheduleId;
            }

            public void setScheduleId(int scheduleId) {
                this.scheduleId = scheduleId;
            }
        }
    }
}
