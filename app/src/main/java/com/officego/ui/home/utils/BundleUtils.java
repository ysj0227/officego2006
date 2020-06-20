package com.officego.ui.home.utils;

import com.officego.ui.home.model.BuildingIdBundleBean;
import com.officego.ui.home.model.HouseIdBundleBean;

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
}
