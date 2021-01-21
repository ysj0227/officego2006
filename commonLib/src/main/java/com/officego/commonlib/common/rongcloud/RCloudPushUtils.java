package com.officego.commonlib.common.rongcloud;

import android.text.TextUtils;

import com.officego.commonlib.common.model.RCloudPushBean;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;

/**
 * Created by YangShiJie
 * Data 2020/6/18.
 * Descriptions:
 * 连接融云 設置用戶信息
 **/
public class RCloudPushUtils {
    private final String TAG = this.getClass().getSimpleName();

    public RCloudPushUtils() {
        RCloudPush();
    }

    /**
     * 融云推送
     */
    private void RCloudPush() {
        if (TextUtils.isEmpty(SpUtils.getSignToken())) {
            OfficegoApi.getInstance().RCloudPush(new RetrofitCallback<RCloudPushBean>() {
                @Override
                public void onSuccess(int code, String msg, RCloudPushBean data) {
                    IMManager.getInstance().connectIM(data.getRongyuntoken(), new ResultCallback<String>() {
                        @Override
                        public void onSuccess(String s) {
                            LogCat.e(TAG, "ConnectionStatus onSuccess userRongChatId=" + s);
                            RCloudSetUserInfoUtils.setCurrentInfoPush(s, data.getNickName(), data.getAvatar());
                        }

                        @Override
                        public void onFail(int errorCode) {
                        }
                    });
                }

                @Override
                public void onFail(int code, String msg, RCloudPushBean data) {
                }
            });
        }
    }
}
