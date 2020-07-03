package com.officego.commonlib.common;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by YangShiJie
 * Data 2020/6/19.
 * Descriptions:
 **/
public class GotoActivityUtils {

    public static void loginClearActivity(Context context) {
        ComponentName comp = new ComponentName(context, "com.officego.ui.login.LoginActivity_");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    public static void loginClearActivity(Context context, boolean isOwnerLogin) {
        ComponentName comp = new ComponentName(context, "com.officego.ui.login.LoginActivity_");
        Intent intent = new Intent();
        intent.putExtra("isOwnerLogin", isOwnerLogin);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    public static void loginActivity(Activity context, int code) {
        ComponentName comp = new ComponentName(context, "com.officego.ui.login.LoginActivity_");
        Intent intent = new Intent();
        intent.putExtra("isReOwnerLogin", true);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivityForResult(intent, code);
    }

    public static void mainActivity(Context context) {
        ComponentName comp = new ComponentName(context, "com.officego.MainActivity_");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    //预约行程详情
    public static void viewingDateDetailActivity(Context context, int scheduleId) {
        ComponentName comp = new ComponentName(context, "com.officego.ui.mine.ViewingDateDetailActivity_");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("scheduleId", scheduleId);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    public static void serviceActivity(Activity context) {
        ComponentName comp = new ComponentName(context, "com.officego.ui.mine.ServiceActivity_");
        Intent intent = new Intent();
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    //业主
    public static void mainOwnerActivity(Context context) {
        ComponentName comp = new ComponentName(context, "com.officego.MainOwnerActivity_");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }
}
