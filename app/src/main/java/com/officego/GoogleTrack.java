package com.officego;

import android.app.Activity;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by shijie
 * Date 2021/1/6
 * Firebase埋点（Analytics
 **/
public class GoogleTrack {

    /**
     * 登录事件名：logoin
     */
    public static void login(Activity context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("logoin", null);
    }

    /**
     * 免密登录事件名：local_tel_logoin
     */
    public static void keyLogin(Activity context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("local_tel_logoin", null);
    }

    /**
     * 楼盘网点详情事件：
     * 找房东聊
     * 楼盘详情：talk_building_detail
     * 网点详情：talk_shared_detail
     * 收藏
     * 楼盘：storeup_building_detail
     * 网点：storeup_shared_detail
     */
    public static void buildingDetail(Activity context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("talk_building_detail", null);
    }

    public static void jointWorkDetail(Activity context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("talk_shared_detail", null);
    }

    public static void buildingCollect(Activity context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("storeup_building_detail", null);
    }
    public static void jointWorkCollect(Activity context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("storeup_shared_detail", null);
    }
}
