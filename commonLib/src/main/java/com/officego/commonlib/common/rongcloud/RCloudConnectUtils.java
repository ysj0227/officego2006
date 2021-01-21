package com.officego.commonlib.common.rongcloud;

import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.utils.log.LogCat;

/**
 * Created by YangShiJie
 * Data 2020/6/18.
 * Descriptions:
 * 连接融云 設置用戶信息
 **/
public class RCloudConnectUtils {
    private final String TAG = this.getClass().getSimpleName();

    public RCloudConnectUtils() {
        initRCIM();
    }

    /**
     * 连接融云
     */
    private void initRCIM() {
        IMManager.getInstance().connectIM(SpUtils.getRongToken(), new ResultCallback<String>() {
            @Override
            public void onSuccess(String s) {
                LogCat.e(TAG, "ConnectionStatus onSuccess userRongChatId=" + s);
                RCloudSetUserInfoUtils.setCurrentInfo(s);
            }

            @Override
            public void onFail(int errorCode) {
                LogCat.e(TAG, "ConnectionStatus onFail errorCode=" + errorCode);
            }
        });
    }
}
