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
     * 用户id
     */
    public static void setUserId(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.setUserId(SpUtils.getSignToken());//设置userId属性
    }

    /**
     * 用户属性上报
     */
    public static void setUserProperty(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.setUserProperty("userNick", SpUtils.getNickName());
        firebaseAnalytics.setUserProperty("phone", SpUtils.getPhoneNum());
        firebaseAnalytics.setUserProperty("role", TextUtils.equals("0", SpUtils.getRole()) ? "租户" : "房东");
        firebaseAnalytics.setUserProperty("channel", "android");
    }

    /**
     * Bundle 公共参数
     */
    private static Bundle bundle() {
        Bundle bundle = new Bundle();
        bundle.putString("phone", TextUtils.isEmpty(SpUtils.getPhoneNum()) ? "" : SpUtils.getPhoneNum());
        bundle.putString("role", (TextUtils.isEmpty(SpUtils.getRole()) || TextUtils.equals("0", SpUtils.getRole())) ? "租户" : "房东");
        bundle.putString("channel", "android");
        return bundle;
    }

    /**
     * 登录事件名：login
     */
    public static void login(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Bundle bundle = new Bundle();
        bundle.putString("phone", TextUtils.isEmpty(SpUtils.getPhoneNum()) ? "" : SpUtils.getPhoneNum());
        bundle.putString("role", (TextUtils.isEmpty(SpUtils.getRole()) || TextUtils.equals("0", SpUtils.getRole())) ? "租户" : "房东");
        bundle.putString("channel", "android");
        firebaseAnalytics.logEvent("login", bundle);
    }

    /**
     * 免密登录事件名：local_tel_login
     */
    public static void keyLogin(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("localtellogin", bundle());
    }

    /**
     * 楼盘详情事件：
     * 找房东聊
     */
    public static void buildingDetailChat(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("talk_buildingdetail", bundle());
    }

    /**
     * 网点详情事件：
     * 找房东聊
     */
    public static void jointWorkDetailChat(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("talk_shareddetail", bundle());
    }

    /**
     * 楼盘收藏
     */
    public static void buildingCollect(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("storeup_buildingdetail", bundle());
    }

    /**
     * 网点收藏
     */
    public static void jointWorkCollect(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("storeup_shareddetail", bundle());
    }

    /**
     * 楼盘 房源详情事件：
     * 找房东聊
     */
    public static void buildingHouseDetailChat(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("talk_buildingofficedetail", bundle());
    }

    /**
     * 网点 房源详情事件：
     * 找房东聊
     */
    public static void jointWorkHouseDetailChat(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("talk_sharedofficedetail", bundle());
    }

    /**
     * 楼盘房源收藏
     */
    public static void buildingHouseCollect(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("storeup_buildingofficedetail", bundle());
    }

    /**
     * 网点房源收藏
     */
    public static void jointWorkHouseCollect(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        firebaseAnalytics.logEvent("storeup_sharedofficedetail", bundle());
    }

    /**
     * 交换手机
     */
    public static void exchangePhone(Context context, boolean isClick) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        if (TextUtils.equals("1", SpUtils.getRole())) {
            firebaseAnalytics.logEvent(isClick ? "exchangephone_enable_owner" : "exchangephone_disable_owner", bundle());
        } else {
            firebaseAnalytics.logEvent(isClick ? "exchangephone_enable_customer" : "exchangephone_disable_customer", bundle());
        }
    }

    /**
     * 交换微信
     */
    public static void exchangeWechat(Context context, boolean isClick) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        if (TextUtils.equals("1", SpUtils.getRole())) {
            firebaseAnalytics.logEvent(isClick ? "exchangewechat_enable_owner" : "exchangewechat_disable_owner", bundle());
        } else {
            firebaseAnalytics.logEvent(isClick ? "exchangewechat_enable_customer" : "exchangewechat_disable_customer", bundle());
        }
    }

    /**
     * 看房
     */
    public static void seeHouse(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        if (TextUtils.equals("1", SpUtils.getRole())) {
            firebaseAnalytics.logEvent("seehouse_owner", bundle());
        } else {
            firebaseAnalytics.logEvent("seehouse_customer", bundle());
        }
    }

    /**
     * 切换身份
     */
    public static void switchId(Context context) {
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        if (TextUtils.equals("1", SpUtils.getRole())) {
            firebaseAnalytics.logEvent("owner_to_customer", bundle());
        } else {
            firebaseAnalytics.logEvent("customer_to_owner", bundle());
        }
    }
}
