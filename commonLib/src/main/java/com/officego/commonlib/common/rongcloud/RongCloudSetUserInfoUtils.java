package com.officego.commonlib.common.rongcloud;

import android.net.Uri;
import android.text.TextUtils;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by YangShiJie
 * Data 2020/6/20.
 * Descriptions:
 **/
public class RongCloudSetUserInfoUtils {

    /**
     * 刷新融云用户信息
     *
     * @param id     融云聊天的id
     * @param name   用户名称
     * @param imgUrl 用户头像
     */
    public static void refreshUserInfoCache(String id, String name, String imgUrl) {
        if (TextUtils.isEmpty(id)) return;
        UserInfo userInfo = new UserInfo(id, name, Uri.parse(imgUrl));
        RongIM.getInstance().refreshUserInfoCache(userInfo);
        //是否携带用户信息，true 携带，false 不携带。
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        RongIM.getInstance().enableNewComingMessageIcon(true);
        RongIM.getInstance().enableUnreadMessageIcon(true);
    }

    /**
     * 获取租户融云id
     */
    public static String getRongTenantId(String id) {
        return id + "0";
    }

    /**
     * 获取业主融云id
     */
    public static String getRongOwnerId(String id) {
        return id + "1";
    }
}
