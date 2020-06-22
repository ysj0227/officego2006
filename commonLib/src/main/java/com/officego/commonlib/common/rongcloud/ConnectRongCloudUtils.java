package com.officego.commonlib.common.rongcloud;

import android.net.Uri;

import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.utils.log.LogCat;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by YangShiJie
 * Data 2020/6/18.
 * Descriptions:
 * 连接融云 設置用戶信息
 **/
public class ConnectRongCloudUtils {
    private final String TAG = this.getClass().getSimpleName();

    public ConnectRongCloudUtils() {
        initRCIM();
    }

    /**
     * 连接融云
     */
    private void initRCIM() {
        IMManager.getInstance().connectIM(SpUtils.getRongToken(), true, new ResultCallback<String>() {
            @Override
            public void onSuccess(String s) {
                LogCat.d(TAG, "111111111 onSuccess userRongChatId=" + s);
                setCurrentInfo(s);
            }

            @Override
            public void onFail(int errorCode) {
                LogCat.d(TAG, "111111111 connectIM errorCode=" + errorCode);
            }
        });
    }

    //方法一  保存,设置当前用户信息
    private void setCurrentInfo(String userRongChatId) {
        SpUtils.saveRongChatId(userRongChatId);
        UserInfo userInfo = new UserInfo(userRongChatId, SpUtils.getNickName(), Uri.parse(SpUtils.getHeaderImg()));

        RongIM.getInstance().setCurrentUserInfo(userInfo);
        //是否携带用户信息，true 携带，false 不携带。
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        RongIM.getInstance().enableNewComingMessageIcon(true);
        RongIM.getInstance().enableUnreadMessageIcon(true);
    }
}
