package com.officego.commonlib.common.rongcloud;

import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.utils.log.LogCat;

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
        IMManager.getInstance().connectIM(SpUtils.getRongToken(), new ResultCallback<String>() {
            @Override
            public void onSuccess(String s) {
                LogCat.d(TAG, "ConnectionStatus onSuccess userRongChatId=" + s);
                RongCloudSetUserInfoUtils.setCurrentInfo(s);
            }

            @Override
            public void onFail(int errorCode) {
                LogCat.d(TAG, "ConnectionStatus onFail errorCode=" + errorCode);
            }
        });
    }
}
