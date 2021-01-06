package com.officego;

import android.content.Context;
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

    /**
     * 登录事件名：logoin
     */
    public static void login(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("logoin", null);
    }

    /**
     * 免密登录事件名：local_tel_logoin
     */
    public static void keyLogin(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("local_tel_logoin", null);
    }

    /**
     * 楼盘网点详情事件：
     * 找房东聊
     * 楼盘详情：talk_building_detail
     * 网点详情：talk_shared_detail
     */
    public static void buildingDetailChat(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("talk_building_detail", null);
    }

    public static void jointWorkDetailChat(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("talk_shared_detail", null);
    }

    /**
     * 收藏
     * 楼盘：storeup_building_detail
     * 网点：storeup_shared_detail
     */
    public static void buildingCollect(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("storeup_building_detail", null);
    }

    public static void jointWorkCollect(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("storeup_shared_detail", null);
    }
}
