package com.officego.commonlib.common.rongcloud.push;

import android.content.Context;
import android.net.Uri;

import com.officego.commonlib.utils.log.LogCat;

import io.rong.push.PushType;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * 融云推送消息
 */
public class OfficeGoPushMessageReceiver extends PushMessageReceiver {
    private static final String TAG = "OfficeGoPushMessageReceiver";

    /**
     * 返回 false, 会弹出融云 SDK 默认通知; 返回 true, 融云 SDK 不会弹通知, 通知需要由您自定义。
     *
     */
    @Override
    public boolean onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage notificationMessage) {

        LogCat.e(TAG, "11111111111 Arrived  pushType=" + pushType.getName());
        return true;
    }

    /**
     * @param context                 context
     * @param pushType                pushType
     * @param pushNotificationMessage pushNotificationMessage
     * @return false  会走融云 SDK 默认处理逻辑, 即点击该通知会打开会话列表或会话界面; 返回 true, 则由您自定义处理逻辑。
     */
    @Override
    public boolean onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage pushNotificationMessage) {
        // 可通过 pushType 判断 Push 的类型
        LogCat.e(TAG, "11111111111  pushType");
        if (pushType == PushType.XIAOMI) {
            LogCat.e(TAG, "11111111111  mi");
            return true; // 此处返回 true. 代表不触发 SDK 默认实现，您自定义处理通知点击跳转事件。
        } else if (pushType == PushType.RONG) {
            return true; // 此处返回 true. 代表不触发 SDK 默认实现，您自定义处理通知点击跳转事件。
        }
        return false;
    }

}



