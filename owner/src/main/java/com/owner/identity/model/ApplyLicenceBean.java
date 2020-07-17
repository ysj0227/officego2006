package com.owner.identity.model;

public class ApplyLicenceBean {

    /**
     * targetId : 2521
     * proprietorRealname :
     * licenceId : 208
     * proprietorJob :
     * chattedId : 252
     * authority : 管理员
     * avatar : 头像
     */

    private String targetId;
    private String proprietorRealname;
    private int licenceId;
    private String proprietorJob;
    private int chattedId;
    private String authority;
    private String avatar;

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getProprietorRealname() {
        return proprietorRealname;
    }

    public void setProprietorRealname(String proprietorRealname) {
        this.proprietorRealname = proprietorRealname;
    }

    public int getLicenceId() {
        return licenceId;
    }

    public void setLicenceId(int licenceId) {
        this.licenceId = licenceId;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
