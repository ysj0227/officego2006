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
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    public static void gotoLoginActivity(Context context) {
        ComponentName comp = new ComponentName(context, "com.officego.ui.login.LoginActivity_");
        Intent intent = new Intent();
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    public static void loginClearActivity(Context context, boolean isOwnerLogin) {
        ComponentName comp = new ComponentName(context, "com.officego.ui.login.LoginActivity_");
        Intent intent = new Intent();
        intent.putExtra("isOwnerLogin", isOwnerLogin);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    public static void mainActivity(Context context) {
        ComponentName comp = new ComponentName(context, "com.officego.MainActivity_");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    //房东
    public static void mainOwnerActivity(Context context) {
        ComponentName comp = new ComponentName(context, "com.officego.MainOwnerActivity_");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    //房东-认证聊天默认返回个人中心
    public static void mainOwnerDefMainActivity(Context context) {
        ComponentName comp = new ComponentName(context, "com.officego.MainOwnerActivity_");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isIdentifyChat", true);//是否认证聊天返回
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    //认证申请进入聊天
    public static void gotoConversationActivity(Context context, String targetId) {
        ComponentName comp = new ComponentName(context, "com.officego.ui.message.ConversationActivity_");
        Intent intent = new Intent();
        intent.putExtra("isSendApply", true);
        intent.putExtra("chatTargetId", targetId);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    //系统推送消息进入聊天
    public static void gotoSystemPushConversationActivity(Context context, String targetId) {
        ComponentName comp = new ComponentName(context, "com.officego.ui.message.ConversationActivity_");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("systemPushTargetId", targetId);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    //历史聊天列表
    public static void gotoMessageHistoryListActivity(Context context) {
        ComponentName comp = new ComponentName(context, "com.officego.ui.chatlist.MessageListActivity_");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }
}
