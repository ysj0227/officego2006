package com.owner.identity.model;

import java.io.Serializable;
import java.util.List;

public class GetIdentityInfoBean implements Serializable {


    /**
     * businessLicense : https://img.officego.com/authentication/1595848313061.png
     * idCard :
     * btype : 1
     * remark :
     * buildingManagersId : 166
     * leaseType : 1
     * licenceId : 47
     * identityType : 1
     * proprietorRealname :
     * proprietorJob :
     * company : 点点点
     * address : 对对对
     * contract : [{"imgUrl":"https://img.officego.com/authentication/1595848335773.png","typeId":47,"remark":null,"id":957,"imgType":2},{"imgUrl":"https://img.officego.com/authentication/1595848337563.png","typeId":47,"remark":null,"id":958,"imgType":2},{"imgUrl":"https://img.officego.com/authentication/1595848349783.png","typeId":47,"remark":null,"id":961,"imgType":2}]
     * userLicenceId : 94
     * idFront :
     * userId : 415
     * idBack :
     * buildingId : 4626
     * licenceStatus : 1
     * buildingName : 大来大楼
     * buildingAddress : 广东路51、59
     * userLicenceStatus : 1
     * authority : 1
     * branchesName :
     * auditStatus : 1
     * creditNo : 1222334
     * buildingTempId : 187
     * premisesPermit : [{"imgUrl":"https://img.officego.com/authentication/1595848324575.png","typeId":47,"remark":null,"id":955,"imgType":1},{"imgUrl":"https://img.officego.com/authentication/1595848325427.png","typeId":47,"remark":null,"id":956,"imgType":1},{"imgUrl":"https://img.officego.com/authentication/1595848343758.png","typeId":47,"remark":null,"id":959,"imgType":1},{"imgUrl":"https://img.officego.com/authentication/1595848352634.png","typeId":47,"remark":null,"id":960,"imgType":1}]
     * buildingStatus : 1
     */

    private String mainPic;
    private String isCreateCompany;
    private String isCreateBranch;
    private String isCreateBuilding;
    private String district;
    private String business;
    private String businessLicense;
    private String idCard;
    private String btype;
    private String remark;
    private String buildingManagersId;
    private String leaseType;
    private String licenceId;
    private String identityType;
    private String proprietorRealname;
    private String proprietorJob;
    private String company;
    private String address;
    private String userLicenceId;
    private String idFront;
    private String userId;
    private String idBack;
    private String buildingId;
    private String licenceStatus;
    private String buildingName;
    private String buildingAddress;
    private String userLicenceStatus;
    private String authority;
    private String branchesName;
    private String auditStatus;
    private String creditNo;
    private String buildingTempId;
    private String buildingStatus;
    private List<ContractBean> contract;
    private List<PremisesPermitBean> premisesPermit;

    public String getMainPic() {
        return mainPic;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }

    public String getIsCreateCompany() {
        return isCreateCompany;
    }

    public void setIsCreateCompany(String isCreateCompany) {
        this.isCreateCompany = isCreateCompany;
    }

    public String getIsCreateBranch() {
        return isCreateBranch;
    }

    public void setIsCreateBranch(String isCreateBranch) {
        this.isCreateBranch = isCreateBranch;
    }

    public String getIsCreateBuilding() {
        return isCreateBuilding;
    }

    public void setIsCreateBuilding(String isCreateBuilding) {
        this.isCreateBuilding = isCreateBuilding;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBtype() {
        return btype;
    }

    public void setBtype(String btype) {
        this.btype = btype;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBuildingManagersId() {
        return buildingManagersId;
    }

    public void setBuildingManagersId(String buildingManagersId) {
        this.buildingManagersId = buildingManagersId;
    }

    public String getLeaseType() {
        return leaseType;
    }

    public void setLeaseType(String leaseType) {
        this.leaseType = leaseType;
    }

    public String getLicenceId() {
        return licenceId;
    }

    public void setLicenceId(String licenceId) {
        this.licenceId = licenceId;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getProprietorRealname() {
        return proprietorRealname;
    }

    public void setProprietorRealname(String proprietorRealname) {
        this.proprietorRealname = proprietorRealname;
    }

    public String getProprietorJob() {
        return proprietorJob;
    }

    public void setProprietorJob(String proprietorJob) {
        this.proprietorJob = proprietorJob;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserLicenceId() {
        return userLicenceId;
    }

    public void setUserLicenceId(String userLicenceId) {
        this.userLicenceId = userLicenceId;
    }

    public String getIdFront() {
        return idFront;
    }

    public void setIdFront(String idFront) {
        this.idFront = idFront;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIdBack() {
        return idBack;
    }

    public void setIdBack(String idBack) {
        this.idBack = idBack;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getLicenceStatus() {
        return licenceStatus;
    }

    public void setLicenceStatus(String licenceStatus) {
        this.licenceStatus = licenceStatus;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getBuildingAddress() {
        return buildingAddress;
    }

    public void setBuildingAddress(String buildingAddress) {
        this.buildingAddress = buildingAddress;
    }

    public String getUserLicenceStatus() {
        return userLicenceStatus;
    }

    public void setUserLicenceStatus(String userLicenceStatus) {
        this.userLicenceStatus = userLicenceStatus;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getBranchesName() {
        return branchesName;
    }

    public void setBranchesName(String branchesName) {
        this.branchesName = branchesName;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }

    public String getBuildingTempId() {
        return buildingTempId;
    }

    public void setBuildingTempId(String buildingTempId) {
        this.buildingTempId = buildingTempId;
    }

    public String getBuildingStatus() {
        return buildingStatus;
    }

    public void setBuildingStatus(String buildingStatus) {
        this.buildingStatus = buildingStatus;
    }

    public List<ContractBean> getContract() {
        return contract;
    }

    public void setContract(List<ContractBean> contract) {
        this.contract = contract;
    }

    public List<PremisesPermitBean> getPremisesPermit() {
        return premisesPermit;
    }

    public void setPremisesPermit(List<PremisesPermitBean> premisesPermit) {
        this.premisesPermit = premisesPermit;
    }

    public static class ContractBean implements Serializable {
        /**
         * imgUrl : https://img.officego.com/authentication/1595848335773.png
         * typeId : 47
         * remark : null
         * id : 957
         * imgType : 2
         */

        private String imgUrl;
        private int typeId;
        private Object remark;
        private int id;
        private int imgType;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getImgType() {
            return imgType;
        }

        public void setImgType(int imgType) {
            this.imgType = imgType;
        }
    }

    public static class PremisesPermitBean implements Serializable {
        /**
         * imgUrl : https://img.officego.com/authentication/1595848324575.png
         * typeId : 47
         * remark : null
         * id : 955
         * imgType : 1
         */

        private String imgUrl;
        private int typeId;
        private Object remark;
        private int id;
        private int imgType;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getImgType() {
            return imgType;
        }

        public void setImgType(int imgType) {
            this.imgType = imgType;
        }
    }
}
