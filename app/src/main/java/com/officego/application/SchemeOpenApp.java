package com.officego.application;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.officego.commonlib.common.model.utils.BundleUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.ui.home.BuildingDetailsActivity_;
import com.officego.ui.home.BuildingDetailsChildActivity_;
import com.officego.ui.home.BuildingDetailsJointWorkActivity_;
import com.officego.ui.home.BuildingDetailsJointWorkChildActivity_;

/**
 * web 外链唤醒app
 */
public class SchemeOpenApp {
    private final Activity context;

    public SchemeOpenApp(Activity context) {
        this.context = context;
        appScheme();
    }

    //外部链接唤起app
    private void appScheme() {
        if (context != null) {
            Intent intent = context.getIntent();
            Uri uri = intent.getData();
            if (uri != null) {
                String btyte = uri.getQueryParameter("btyte");
                String buildingId = uri.getQueryParameter("buildingId");
                String houseId = uri.getQueryParameter("houseId");
                if (buildingId != null && !TextUtils.isEmpty(buildingId) && !TextUtils.isEmpty(btyte)) {
                    //楼盘，网点
                    if (TextUtils.equals(String.valueOf(Constants.TYPE_BUILDING), btyte)) {
                        BuildingDetailsActivity_.intent(context)
                                .mBuildingBean(BundleUtils.BuildingMessage(Constants.TYPE_BUILDING, Integer.parseInt(buildingId))).start();
                    } else {
                        BuildingDetailsJointWorkActivity_.intent(context)
                                .mBuildingBean(BundleUtils.BuildingMessage(Constants.TYPE_JOINTWORK, Integer.parseInt(buildingId))).start();
                    }
                } else if (houseId != null && !TextUtils.isEmpty(houseId) && !TextUtils.isEmpty(btyte)) {
                    //房源
                    if (TextUtils.equals(String.valueOf(Constants.TYPE_BUILDING), btyte)) {
                        BuildingDetailsChildActivity_.intent(context)
                                .mChildHouseBean(BundleUtils.houseMessage(Constants.TYPE_BUILDING, Integer.parseInt(houseId))).start();
                    } else {
                        BuildingDetailsJointWorkChildActivity_.intent(context)
                                .mChildHouseBean(BundleUtils.houseMessage(Constants.TYPE_JOINTWORK, Integer.parseInt(houseId))).start();
                    }
                }
            }
        }
    }
}
