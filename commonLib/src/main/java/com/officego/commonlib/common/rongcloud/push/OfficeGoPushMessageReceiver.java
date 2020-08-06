package com.officego.commonlib.common.rongcloud.push;

import android.content.Context;

import io.rong.push.PushType;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * 融云推送消息
 */
public class OfficeGoPushMessageReceiver extends PushMessageReceiver {
    private static final String TAG = "OfficeGoPushMessageReceiver";

    @Override
    public boolean onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage notificationMessage) {
        // 可通过 pushType 判断 Push 的类型
        //用来接收服务器发来的通知栏消息
        return false;// 返回 false, 会弹出融云 SDK 默认通知; 返回 true, 融云 SDK 不会弹通知, 通知需要由您自定义。
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        return false;// 返回 false, 会走融云 SDK 默认处理逻辑, 即点击该通知会打开会话列表或会话界面; 返回 true, 则由您自定义处理逻辑。
    }


}


