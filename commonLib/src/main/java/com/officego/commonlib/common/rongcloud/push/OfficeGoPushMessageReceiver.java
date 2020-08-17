package com.officego.commonlib.common.rongcloud.push;

import android.content.Context;
import android.text.TextUtils;

import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.rongcloud.IMManager;
import com.officego.commonlib.common.rongcloud.ResultCallback;
import com.officego.commonlib.common.rongcloud.RongCloudSetUserInfoUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.utils.log.LogCat;

import io.rong.push.PushType;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

import static com.officego.commonlib.common.GotoActivityUtils.gotoSystemPushConversationActivity;

/**
 * 融云推送消息
 */
public class OfficeGoPushMessageReceiver extends PushMessageReceiver {
    private static final String TAG = "OfficeGoPushMessageReceiver";
    private boolean isGotoConversion;

    /**
     * 返回 false, 会弹出融云 SDK 默认通知; 返回 true, 融云 SDK 不会弹通知, 通知需要由您自定义。
     */
    @Override
    public boolean onNotificationMessageArrived(Context context, PushType pushType, PushNotificationMessage notificationMessage) {
        return false;
    }

    /**
     * @param context  context
     * @param pushType pushType
     * @param message  pushNotificationMessage
     * @return false  会走融云 SDK 默认处理逻辑, 即点击该通知会打开会话列表或会话界面; 返回 true, 则由您自定义处理逻辑。
     */
    @Override
    public boolean onNotificationMessageClicked(Context context, PushType pushType, PushNotificationMessage message) {
        // true. 代表不触发 SDK 默认实现，您自定义处理通知点击跳转事件。  false 融云内置跳转
        String targetId = message.getTargetId();
        LogCat.e(TAG, "pushType=" + pushType.getName() + "  getTargetId=" + targetId);
        isGotoConversion = false;
        if (pushType == PushType.RONG) {
            //跳转系统消息
            if (!TextUtils.isEmpty(targetId) && TextUtils.equals(Constants.TYPE_SYSTEM, targetId.substring(targetId.length() - 1))) {
                gotoSystemPushConversationActivity(context, targetId);
                return true;
            }
            return false;
        } else if (pushType == PushType.XIAOMI) {
            //跳转系统消息
            pushMIClick(context, targetId);
            return isGotoConversion;
        }
        return false;
    }


    /**
     * 连接融云
     *
     * @param context  context
     * @param targetId targetId
     */
    private void pushMIClick(Context context, String targetId) {
        if (!TextUtils.isEmpty(SpUtils.getSignToken())) {
            IMManager.getInstance().connectIM(SpUtils.getRongToken(), true, new ResultCallback<String>() {
                @Override
                public void onSuccess(String s) {
                    RongCloudSetUserInfoUtils.setCurrentInfo(s);
                    if (!TextUtils.isEmpty(targetId) && TextUtils.equals(Constants.TYPE_SYSTEM, targetId.substring(targetId.length() - 1))) {
                        gotoSystemPushConversationActivity(context, targetId);
                        isGotoConversion = true;
                    }
                }

                @Override
                public void onFail(int errorCode) {
                }
            });
        }
    }
}



