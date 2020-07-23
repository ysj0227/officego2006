package com.officego.commonlib.common.model;

public class QueryApplyLicenceBean {

    /**
     * address :
     * targetId : 4221
     * licenceId : 461
     * proprietorRealname :
     * identityType : 1
     * proprietorJob :
     * chattedId : 422
     * authority : 管理员
     * userLicenceId : 9
     * avatar : https://img.officego.com/head.png
     * title : 测试企业管理局
     */

    private String address;
    private String targetId;
    private int licenceId;
    private String proprietorRealname;
    private int identityType;
    private String proprietorJob;
    private int chattedId;
    private String authority;
    private int userLicenceId;
    private String avatar;
    private String title;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getLicenceId() {
        return licenceId;
    }

    public void setLicenceId(int licenceId) {
        this.licenceId = licenceId;
    }

    public String getProprietorRealname() {
        return proprietorRealname;
    }

    public void setProprietorRealname(String proprietorRealname) {
        this.proprietorRealname = proprietorRealname;
    }

    public int getIdentityType() {
        return identityType;
    }

    public void setIdentityType(int identityType) {
        this.identityType = identityType;
    }

    public String getProprietorJob() {
        return proprietorJob;
    }

    public void setProprietorJob(String proprietorJob) {
        this.proprietorJob = proprietorJob;
    }

    public int getChattedId() {
        return chattedId;
    }

    public void setChattedId(int chattedId) {
        this.chattedId = chattedId;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public int getUserLicenceId() {
        return userLicenceId;
    }

    public void setUserLicenceId(int userLicenceId) {
        this.userLicenceId = userLicenceId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
