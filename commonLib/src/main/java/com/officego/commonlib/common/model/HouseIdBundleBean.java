package com.officego.commonlib.common.model;

import java.io.Serializable;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public class HouseIdBundleBean implements Serializable {
    private int btype;
    private int houseId;

    public int getBtype() {
        return btype;
    }

    public void setBtype(int btype) {
        this.btype = btype;
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }
}
