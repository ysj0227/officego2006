package com.officego.commonlib.common;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.officego.commonlib.common.model.PayData;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.log.LogCat;

import java.io.Serializable;

/**
 * Created by YangShiJie
 * Data 2020/6/19.
 * Descriptions:
 **/
public class GotoActivityUtils {
    /**
     * 回到桌面
     */
    public static void gotoHome(Context context) {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(home);
    }

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

    //系统推送消息进入聊天
    public static void gotoSystemPushConversationActivity(Context context, String targetId) {
        if (context != null) {
            ComponentName comp = new ComponentName(context, "com.officego.ui.message.ConversationActivity_");
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("systemPushTargetId", targetId);
            intent.setComponent(comp);
            intent.setAction("android.intent.action.VIEW");
            context.startActivity(intent);
        }
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

    /**
     * 小米 vivo远程推送时在启动页判断bundle 然后进行跳转
     * schema: PushRemoteDetailActivity
     */
    public static void MI_VIVO_PushClick(Context context, String pushData) {
        LogCat.e("MI_VIVO_PushClick", "小米 vivo远程 pushData=" + pushData);
        String uri = "rong://com.officego/push_message?";
        Intent intent = new Intent();
        intent.putExtra("pushData", pushData);
        intent.setData(Uri.parse(uri));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//必加
        context.startActivity(intent);
    }

    /**
     * 微信支付
     * @param context
     */
    public static void gotoWeChatPayActivity(Context context, PayData bean) {
        ComponentName comp = new ComponentName(context, "com.officego.wxapi.WXPayEntryActivity");
        Intent intent = new Intent();
        intent.putExtra(Constants.WX_PAY, (Serializable) bean);
        intent.setComponent(comp);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }
}
