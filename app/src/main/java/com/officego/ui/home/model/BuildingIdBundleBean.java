package com.officego.ui.home.model;

import java.io.Serializable;

/**
 * Created by YangShiJie
 * Data 2020/6/8.
 * Descriptions:
 **/
public class BuildingIdBundleBean implements Serializable {
    private int btype;
    private int BuildingId;

    public int getBtype() {
        return btype;
    }

    public void setBtype(int btype) {
        this.btype = btype;
    }

    public int getBuildingId() {
        return BuildingId;
    }

    public void setBuildingId(int BuildingId) {
        this.BuildingId = BuildingId;
    }
}
