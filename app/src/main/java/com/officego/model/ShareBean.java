package com.officego.model;

import java.io.Serializable;

/**
 * Created by YangShiJie
 * Data 2020/6/16.
 * Descriptions:
 **/
public class ShareBean implements Serializable {
    private String title;
    private String des;
    private String imgUrl;
    private String detailsUrl;
    private int bType;//楼盘网点类型
    private String id;//楼盘网点id
    private boolean isHouseChild;//是否房源子页面分享

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }

    public int getbType() {
        return bType;
    }

    public void setbType(int bType) {
        this.bType = bType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isHouseChild() {
        return isHouseChild;
    }

    public void setHouseChild(boolean houseChild) {
        isHouseChild = houseChild;
    }
}
