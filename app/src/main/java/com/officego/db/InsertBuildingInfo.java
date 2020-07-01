package com.officego.db;

import org.litepal.crud.LitePalSupport;

/**
 * Created by yangshijie
 * Data 2020/5/7.
 **/
public class InsertBuildingInfo extends LitePalSupport {
    private int id;
    private String buildingValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuildingValue() {
        return buildingValue;
    }

    public void setBuildingValue(String buildingValue) {
        this.buildingValue = buildingValue;
    }
}
