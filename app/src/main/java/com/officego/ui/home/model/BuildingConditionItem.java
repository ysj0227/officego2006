package com.officego.ui.home.model;

/**
 * Created by YangShiJie
 * Data 2020/6/3.
 * Descriptions:
 **/
public class BuildingConditionItem {
    private String title;
    private String value;
    private String buildingNum;

    public BuildingConditionItem(String title,  String value,String buildingNum) {
        this.title = title;
        this.value = value;
        this.buildingNum = buildingNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBuildingNum() {
        return buildingNum;
    }

    public void setBuildingNum(String buildingNum) {
        this.buildingNum = buildingNum;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
