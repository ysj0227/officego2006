package com.officego.commonlib.common.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/11/23
 **/
public class IdentityRejectBean {

    /**
     * businessLicense : ["https://img.officego.com/test/1606097099436.jpg?x-oss-process=style/small","https://img.officego.com/test/1606097097760.jpg?x-oss-process=style/small"]
     * provincetName :
     * address :
     * isHolder : 1
     * btype : 1
     * districtIdName :
     * buildId : 0
     * remark : [{"dictValue":1,"dictCname":"楼名名称不符合规范或不正确"}]
     * idFront : https://img.officego.com/test/1606097122484.jpg?x-oss-process=style/small
     * userId : 467
     * buildingId : 373
     * idBack : https://img.officego.com/test/1606097122484.jpg?x-oss-process=style/small
     * buildingName : 上海大众
     * businessDistrict :
     * districtId :
     * businessDistrictName :
     * materials : ["https://img.officego.com/test/1606097099436.jpg?x-oss-process=style/small","https://img.officego.com/test/1606097097760.jpg?x-oss-process=style/small"]
     * mainPic : https://img.officego.com/test/1599125902594.jpeg?x-oss-process=style/large
     * premisesPermit : ["https://img.officego.com/test/1606097099436.jpg?x-oss-process=style/small","https://img.officego.com/test/1606097097760.jpg?x-oss-process=style/small"]
     * status : 7
     */

    @SerializedName("provincetName")
    private String provincetName;
    @SerializedName("address")
    private String address;
    @SerializedName("isHolder")
    private String isHolder;
    @SerializedName("btype")
    private String btype;
    @SerializedName("districtIdName")
    private String districtIdName;
    @SerializedName("buildId")
    private String buildId;
    @SerializedName("idFront")
    private String idFront;
    @SerializedName("userId")
    private int userId;
    @SerializedName("buildingId")
    private int buildingId;
    @SerializedName("idBack")
    private String idBack;
    @SerializedName("buildingName")
    private String buildingName;
    @SerializedName("businessDistrict")
    private String businessDistrict;
    @SerializedName("districtId")
    private String districtId;
    @SerializedName("businessDistrictName")
    private String businessDistrictName;
    @SerializedName("mainPic")
    private String mainPic;
    @SerializedName("status")
    private int status;
    @SerializedName("businessLicense")
    private List<String> businessLicense;
    @SerializedName("remark")
    private List<RemarkBean> remark;
    @SerializedName("materials")
    private List<String> materials;
    @SerializedName("premisesPermit")
    private List<String> premisesPermit;

    public String getProvincetName() {
        return provincetName;
    }

    public void setProvincetName(String provincetName) {
        this.provincetName = provincetName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsHolder() {
        return isHolder;
    }

    public void setIsHolder(String isHolder) {
        this.isHolder = isHolder;
    }

    public String getBtype() {
        return btype;
    }

    public void setBtype(String btype) {
        this.btype = btype;
    }

    public String getDistrictIdName() {
        return districtIdName;
    }

    public void setDistrictIdName(String districtIdName) {
        this.districtIdName = districtIdName;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getIdFront() {
        return idFront;
    }

    public void setIdFront(String idFront) {
        this.idFront = idFront;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public String getIdBack() {
        return idBack;
    }

    public void setIdBack(String idBack) {
        this.idBack = idBack;
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

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getBusinessDistrictName() {
        return businessDistrictName;
    }

    public void setBusinessDistrictName(String businessDistrictName) {
        this.businessDistrictName = businessDistrictName;
    }

    public String getMainPic() {
        return mainPic;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(List<String> businessLicense) {
        this.businessLicense = businessLicense;
    }

    public List<RemarkBean> getRemark() {
        return remark;
    }

    public void setRemark(List<RemarkBean> remark) {
        this.remark = remark;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }

    public List<String> getPremisesPermit() {
        return premisesPermit;
    }

    public void setPremisesPermit(List<String> premisesPermit) {
        this.premisesPermit = premisesPermit;
    }

    public static class RemarkBean {
        /**
         * dictValue : 1
         * dictCname : 楼名名称不符合规范或不正确
         */

        @SerializedName("dictCode")
        private int dictCode;
        @SerializedName("dictCname")
        private String dictCname;

        public int getDictCode() {
            return dictCode;
        }

        public void setDictCode(int dictCode) {
            this.dictCode = dictCode;
        }

        public String getDictCname() {
            return dictCname;
        }

        public void setDictCname(String dictCname) {
            this.dictCname = dictCname;
        }
    }
}
