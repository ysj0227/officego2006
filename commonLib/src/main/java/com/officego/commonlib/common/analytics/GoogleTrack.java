package com.officego.commonlib.common.analytics;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.officego.commonlib.common.SpUtils;

/**
 * Created by shijie
 * Date 2021/1/6
 * Firebase埋点 Analytics
 **/
public class GoogleTrack {

    /**
     * 用户属性上报
     */
    public static void setUserProperty(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.setUserId(SpUtils.getSignToken());//设置userId属性
        firebaseAnalytics.setUserProperty("userNick", SpUtils.getNickName());
        firebaseAnalytics.setUserProperty("phone", SpUtils.getPhoneNum());
        firebaseAnalytics.setUserProperty("role", TextUtils.equals("0", SpUtils.getRole()) ? "租户" : "房东");
        firebaseAnalytics.setUserProperty("channel", "android");
    }

    private static Bundle bundle() {
        Bundle bundle = new Bundle();
        bundle.putString("phone", TextUtils.isEmpty(SpUtils.getPhoneNum()) ? "" : SpUtils.getPhoneNum());
        return bundle;
    }


    /**
     * 登录事件名：login
     */
    public static void login(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("login", bundle());
    }

    /**
     * 免密登录事件名：local_tel_login
     */
    public static void keyLogin(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("local_tel_login", bundle());
    }

    /**
     * 楼盘网点详情事件：
     * 找房东聊
     * 楼盘详情：talk_building_detail
     * 网点详情：talk_shared_detail
     */
    public static void buildingDetailChat(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("talk_building_detail", bundle());
    }

    public static void jointWorkDetailChat(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("talk_shared_detail", bundle());
    }

    /**
     * 收藏
     * 楼盘：storeup_building_detail
     * 网点：storeup_shared_detail
     */
    public static void buildingCollect(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("storeup_building_detail", bundle());
    }

    public static void jointWorkCollect(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("storeup_shared_detail", bundle());
    }

    /**
     * 房源详情事件：
     * 找房东聊
     * 楼盘详情：talk_building_detail
     * 网点详情：talk_shared_detail
     */
    public static void buildingHouseDetailChat(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("talk_building_officedetail", bundle());
    }

    public static void jointWorkHouseDetailChat(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("talk_shared_officedetail", bundle());
    }

    /**
     * 房源收藏
     * 楼盘：storeup_building_detail
     * 网点：storeup_shared_detail
     */
    public static void buildingHouseCollect(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("storeup_building_officedetail", bundle());
    }

    public static void jointWorkHouseCollect(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("storeup_shared_officedetail", bundle());
    }

    /**
     * 租户端对话
     */
    //交换手机
    public static void exchangePhone(Context context, boolean isClick) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        if (TextUtils.equals("1", SpUtils.getRole())) {
            firebaseAnalytics.logEvent(isClick ? "owner_click_exchangephone" : "owner_clickdisable_exchangephone", bundle());
        } else {
            firebaseAnalytics.logEvent(isClick ? "customer_click_exchangephone" : "customer_clickdisable_exchangephone", bundle());
        }
    }

    //交换微信
    public static void exchangeWechat(Context context, boolean isClick) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        if (TextUtils.equals("1", SpUtils.getRole())) {
            firebaseAnalytics.logEvent(isClick ? "owner_click_exchangewechat" : "owner_clickdisable_exchangewechat", bundle());
        } else {
            firebaseAnalytics.logEvent(isClick ? "customer_click_exchangewechat" : "customer_clickdisable_exchangewechat", bundle());
        }
    }

    //看房
    public static void seeHouse(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        if (TextUtils.equals("1", SpUtils.getRole())) {
            firebaseAnalytics.logEvent("owner_click_seehouse", bundle());
        } else {
            firebaseAnalytics.logEvent("customer_click_seehouse", bundle());
        }
    }

    //切换身份
    public static void switchId(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        if (TextUtils.equals("1", SpUtils.getRole())) {
            firebaseAnalytics.logEvent("owner_to_customer", bundle());
        } else {
            firebaseAnalytics.logEvent("customer_to_owner", bundle());
        }
    }
}