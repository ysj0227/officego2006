package com.officego.db;

import org.litepal.LitePal;

/**
 * Created by YangShiJie
 * Data 2020/7/1.
 * Descriptions:
 **/
public class LitepalUtils {

    //保存数据
    public static void saveBuilding(String mValue) {
        InsertBuildingInfo buildingInfo = new InsertBuildingInfo();
        buildingInfo.setBuildingValue(mValue);
        buildingInfo.save();
    }

    //查询数据
    public static InsertBuildingInfo getBuildingInfo(String mValue) {
        return LitePal.where("buildingValue = ?", mValue).findFirst(InsertBuildingInfo.class);
    }
}
