package com.officego.commonlib.common.rongcloud;

import android.net.Uri;

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
        UserInfo userInfo = new UserInfo(id, name, Uri.parse(imgUrl));
        RongIM.getInstance().refreshUserInfoCache(userInfo);
    }
}
