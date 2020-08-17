package com.officego.commonlib.common.rongcloud;

import android.net.Uri;
import android.text.TextUtils;

import com.officego.commonlib.common.SpUtils;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by YangShiJie
 * Data 2020/6/20.
 * Descriptions:
 **/
public class RongCloudSetUserInfoUtils {

    /**
     * 融云连接成功
     * 保存,设置当前用户信息
     * @param userRongChatId targetId
     */
    public static void setCurrentInfo(String userRongChatId) {
        SpUtils.saveRongChatId(userRongChatId);
        UserInfo userInfo = new UserInfo(userRongChatId, SpUtils.getNickName(), Uri.parse(SpUtils.getHeaderImg()));
        RongIM.getInstance().setCurrentUserInfo(userInfo);
        //是否携带用户信息，true 携带，false 不携带。
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        RongIM.getInstance().enableNewComingMessageIcon(true);
        RongIM.getInstance().enableUnreadMessageIcon(true);
    }

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
