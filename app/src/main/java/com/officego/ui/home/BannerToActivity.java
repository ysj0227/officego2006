package com.officego.ui.home;

import android.content.Context;

import com.officego.commonlib.common.model.utils.BundleUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.h5.WebViewBannerActivity_;
import com.officego.h5.WebViewMeetingActivity_;

/**
 * Created by shijie
 * Date 2021/1/4
 **/
public class BannerToActivity {

    /**
     *
     * @param context
     * @param type 1内 3外连接
     * @param pageType  pageType内链类型1：楼盘详情，2:网点详情 3:楼盘房源详情,4:网点房源详情 5会议室
     * @param pageId pageId
     * @param wUrl  wUrl
     */
    public static void toActivity(Context context, int type, int pageType, int pageId, String wUrl) {
        if (context == null) return;
        if (type == 1) {
            if (pageType == 5) {//会议室
                WebViewMeetingActivity_.intent(context).start();
            }
            if (pageId != 0) {
                if (pageType == 1) {
                    BuildingDetailsActivity_.intent(context)
                            .mBuildingBean(BundleUtils.BuildingMessage(Constants.TYPE_BUILDING, pageId)).start();
                } else if (pageType == 2) {
                    BuildingDetailsJointWorkActivity_.intent(context)
                            .mBuildingBean(BundleUtils.BuildingMessage(Constants.TYPE_JOINTWORK, pageId)).start();
                } else if (pageType == 3) {
                    BuildingDetailsChildActivity_.intent(context)
                            .mChildHouseBean(BundleUtils.houseMessage(Constants.TYPE_BUILDING, pageId)).start();
                } else if (pageType == 4) {
                    BuildingDetailsJointWorkChildActivity_.intent(context)
                            .mChildHouseBean(BundleUtils.houseMessage(Constants.TYPE_JOINTWORK, pageId)).start();
                }
            }
        } else if (type == 3) {
            //外链跳转
            WebViewBannerActivity_.intent(context).url(wUrl).start();
        }
    }
    //pageType内链类型1：楼盘详情，2:网点详情 3:楼盘房源详情,4:网点房源详情 5会议室
    public static void toPushActivity(Context context, int requestCode,int type, int pageType, int pageId, String wUrl) {
        if (context == null) return;
        if (type == 1) {
            if (pageType == 5) {//会议室
                WebViewMeetingActivity_.intent(context).startForResult(requestCode);
            }
            if (pageId != 0) {
                if (pageType == 1) {
                    BuildingDetailsActivity_.intent(context)
                            .mBuildingBean(BundleUtils.BuildingMessage(Constants.TYPE_BUILDING, pageId)).startForResult(requestCode);
                } else if (pageType == 2) {
                    BuildingDetailsJointWorkActivity_.intent(context)
                            .mBuildingBean(BundleUtils.BuildingMessage(Constants.TYPE_JOINTWORK, pageId)).startForResult(requestCode);
                } else if (pageType == 3) {
                    BuildingDetailsChildActivity_.intent(context)
                            .mChildHouseBean(BundleUtils.houseMessage(Constants.TYPE_BUILDING, pageId)).startForResult(requestCode);
                } else if (pageType == 4) {
                    BuildingDetailsJointWorkChildActivity_.intent(context)
                            .mChildHouseBean(BundleUtils.houseMessage(Constants.TYPE_JOINTWORK, pageId)).startForResult(requestCode);
                }
            }
        } else if (type == 3) {
            //外链跳转
            WebViewBannerActivity_.intent(context).url(wUrl).startForResult(requestCode);
        }
    }
}
