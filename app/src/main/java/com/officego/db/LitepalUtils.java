package com.officego.db;

import org.litepal.LitePal;

/**
 * Created by YangShiJie
 * Data 2020/7/1.
 * Descriptions:
 **/
public class LitepalUtils {

    //保存楼盘-网点
    public static void saveBuilding(String mValue) {
        InsertBuildingInfo buildingInfo = new InsertBuildingInfo();
        buildingInfo.setBuildingValue(mValue);
        buildingInfo.save();
    }

    //查询楼盘-网点
    public static InsertBuildingInfo getBuildingInfo(String mValue) {
        return LitePal.where("buildingValue = ?", mValue).findFirst(InsertBuildingInfo.class);
    }

    //保存楼盘-网点下房源
    public static void saveHouse(String mValue) {
        InsertHouseInfo houseInfo = new InsertHouseInfo();
        houseInfo.setHouseValue(mValue);
        houseInfo.save();
    }

    //查询楼盘-网点下房源
    public static InsertHouseInfo getHouseInfo(String mValue) {
        return LitePal.where("houseValue = ?", mValue).findFirst(InsertHouseInfo.class);
    }
}
