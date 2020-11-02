package com.officego.commonlib.common.model;

import java.io.Serializable;

/**
 * Created by shijie
 * Date 2020/11/2
 **/
public class BuildingManagerBean implements Serializable {
    private int buildingId;
    private int isTemp;

    public BuildingManagerBean(int buildingId, int isTemp) {
        this.buildingId = buildingId;
        this.isTemp = isTemp;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public int getIsTemp() {
        return isTemp;
    }

    public void setIsTemp(int isTemp) {
        this.isTemp = isTemp;
    }
}
