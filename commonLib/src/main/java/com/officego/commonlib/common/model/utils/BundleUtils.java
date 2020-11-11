package com.officego.commonlib.common.model.utils;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.officego.commonlib.common.message.BuildingInfo;
import com.officego.commonlib.common.model.BuildingIdBundleBean;
import com.officego.commonlib.common.model.HouseIdBundleBean;
import com.officego.commonlib.constant.Constants;

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

    //楼盘聊天插入消息获取
    public static BuildingIdBundleBean buildingBean(Activity context) {
        Intent intent = context.getIntent();
        if (intent != null && intent.hasExtra("conversationBuilding")) {
            return (BuildingIdBundleBean) intent.getSerializableExtra("conversationBuilding");
        }
        return null;
    }

    //房源聊天插入消息获取
    public static HouseIdBundleBean houseBean(Activity context) {
        Intent intent = context.getIntent();
        if (intent != null && intent.hasExtra("conversationHouse")) {
            return (HouseIdBundleBean) intent.getSerializableExtra("conversationHouse");
        }
        return null;
    }

    //插入消息点击进入详情
    public static void gotoDetailsActivity(Context context, BuildingInfo info) {
        String pathClass = "com.officego.ui.home.";
        ComponentName comp;
        Intent intent = new Intent();
        if (info.getIsBuildOrHouse() == 1) {//楼盘网点
            if (Constants.TYPE_BUILDING == info.getBtype()) {
                comp = new ComponentName(context, pathClass + "BuildingDetailsActivity_");
            } else {
                comp = new ComponentName(context, pathClass + "BuildingDetailsJointWorkActivity_");
            }
            intent.putExtra("conversationBuilding", BundleUtils.BuildingMessage(info.getBtype(), info.getBuildingId()));
        } else {//房源
            if (Constants.TYPE_BUILDING == info.getBtype()) {
                comp = new ComponentName(context, pathClass + "BuildingDetailsChildActivity_");
            } else {
                comp = new ComponentName(context, pathClass + "BuildingDetailsJointWorkChildActivity_");
            }
            intent.putExtra("conversationHouse", BundleUtils.houseMessage(info.getBtype(), info.getHouseId()));
        }
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    //业主-首页进入详情********************************************
    public static void ownerGotoDetailsActivity(Context context, boolean isBuildOrHouse, int btype, int id, int isTemp) {
        String pathClass = "com.officego.ui.home.";
        ComponentName comp;
        Intent intent = new Intent();
        if (isBuildOrHouse) {//楼盘网点
            if (Constants.TYPE_BUILDING == btype) {
                comp = new ComponentName(context, pathClass + "BuildingDetailsActivity_");
            } else {
                comp = new ComponentName(context, pathClass + "BuildingDetailsJointWorkActivity_");
            }
            intent.putExtra("ownerPreviewBuilding", BundleUtils.BuildingMessage(btype, id));//楼盘id
        } else {//房源
            if (Constants.TYPE_BUILDING == btype) {
                comp = new ComponentName(context, pathClass + "BuildingDetailsChildActivity_");
            } else {
                comp = new ComponentName(context, pathClass + "BuildingDetailsJointWorkChildActivity_");
            }
            intent.putExtra("ownerPreviewHouse", BundleUtils.houseMessage(btype, id));//房源id
        }
        intent.putExtra("ownerIsTemp", isTemp);//业主进入楼盘详情参数
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    //业主首页进入详情
    public static BuildingIdBundleBean ownerBuildingBean(Activity context) {
        Intent intent = context.getIntent();
        if (intent != null && intent.hasExtra("ownerPreviewBuilding")) {
            return (BuildingIdBundleBean) intent.getSerializableExtra("ownerPreviewBuilding");
        }
        return null;
    }

    //业主首页进入详情
    public static HouseIdBundleBean ownerHouseBean(Activity context) {
        Intent intent = context.getIntent();
        if (intent != null && intent.hasExtra("ownerPreviewHouse")) {
            return (HouseIdBundleBean) intent.getSerializableExtra("ownerPreviewHouse");
        }
        return null;
    }

    //业主首页进入详情使用的 isTemp 0正式1临时
    public static int ownerIsTemp(Activity context) {
        Intent intent = context.getIntent();
        if (intent != null && intent.hasExtra("ownerIsTemp")) {
            return intent.getIntExtra("ownerIsTemp", 0);
        }
        return 0;
    }
}
