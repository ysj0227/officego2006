package com.owner.identity.model;

public class ApplyJoinBean {

    /**
     * "id": 7,//申请id
     * "licenceId": 208,//企业id
     * "userId": 424//自己的id
     */

    private int id;
    private int licenceId;
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLicenceId() {
        return licenceId;
    }

    public void setLicenceId(int licenceId) {
        this.licenceId = licenceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
