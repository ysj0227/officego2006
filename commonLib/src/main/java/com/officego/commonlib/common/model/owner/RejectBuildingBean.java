package com.officego.commonlib.common.model.owner;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shijie
 * Date 2020/11/10
 **/
public class RejectBuildingBean {

    /**
     * address : 上海
     * buildingId : 1
     * buildingName : 广田大厦
     * businessDistrict : 八佰伴
     * mainPic : https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1810958178,2297186643&fm=26&gp=0.jpgs
     * buildingCardTemp :
     * remark :
     */

    @SerializedName("address")
    private String address;
    @SerializedName("buildingName")
    private String buildingName;
    @SerializedName("businessDistrict")
    private String businessDistrict;
    @SerializedName("districtId")
    private String districtId;
    @SerializedName("mainPic")
    private String mainPic;
    @SerializedName("buildingCardTemp")
    private List<BuildingCardTempBean> buildingCardTemp;
    @SerializedName("remark")
    private String remark;
    @SerializedName("buildId")
    private int buildId;

    public List<BuildingCardTempBean> getBuildingCardTemp() {
        return buildingCardTemp;
    }

    public void setBuildingCardTemp(List<BuildingCardTempBean> buildingCardTemp) {
        this.buildingCardTemp = buildingCardTemp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getMainPic() {
        return mainPic;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getBuildId() {
        return buildId;
    }

    public void setBuildId(int buildId) {
        this.buildId = buildId;
    }

    public static class BuildingCardTempBean {
        @SerializedName("id")
        private int id;
        @SerializedName("imgUrl")
        private String imgUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }
    }
}
