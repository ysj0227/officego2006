package com.officego.commonlib.common.model.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.officego.commonlib.common.model.BuildingIdBundleBean;
import com.officego.commonlib.common.model.HouseIdBundleBean;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class BundleUtils {

    /**
     * 房源网点详情的传值
     *
     * @param btype
     * @param buildingId
     * @return
     */
    public static BuildingIdBundleBean BuildingMessage(int btype, int buildingId) {
        BuildingIdBundleBean bean = new BuildingIdBundleBean();
        bean.setBtype(btype);
        bean.setBuildingId(buildingId);
        return bean;
    }

    /**
     * 房源网点详情的传值
     *
     * @param btype
     * @param houseId
     * @return
     */
    public static HouseIdBundleBean houseMessage(int btype, int houseId) {
        HouseIdBundleBean bean = new HouseIdBundleBean();
        bean.setBtype(btype);
        bean.setHouseId(houseId);
        return bean;
    }

    public static BuildingIdBundleBean buildingBean(Activity context) {
        Intent intent = context.getIntent();
        if (intent != null && intent.hasExtra("conversationBuilding")) {
            return (BuildingIdBundleBean) intent.getSerializableExtra("conversationBuilding");
        }
        return null;
    }

    public static HouseIdBundleBean houseBean(Activity context) {
        Intent intent = context.getIntent();
        if (intent != null && intent.hasExtra("conversationHouse")) {
            return (HouseIdBundleBean) intent.getSerializableExtra("conversationHouse");
        }
        return null;
    }
}
