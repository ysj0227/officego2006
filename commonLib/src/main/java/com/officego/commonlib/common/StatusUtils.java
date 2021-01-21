package com.officego.commonlib.common;

import android.text.TextUtils;

import com.officego.commonlib.constant.Constants;

import static com.officego.commonlib.common.GotoActivityUtils.gotoSystemPushConversationActivity;

/**
 * Created by shijie
 * Date 2021/1/21
 **/
public class StatusUtils {

    /**
     * 是否登录
     */
    public static boolean isLogin() {
        return !TextUtils.isEmpty(SpUtils.getSignToken());
    }

    /**
     * 是否系统消息
     */
    public static boolean isSystemMsg(String targetId) {
        if (TextUtils.isEmpty(targetId)) {
            return false;
        }
        return (targetId.length() == 1 && TextUtils.equals(Constants.TYPE_SYSTEM, targetId)) ||
                (targetId.length() > 1 && TextUtils.equals(Constants.TYPE_SYSTEM, targetId.substring(targetId.length() - 1)));
    }
}
