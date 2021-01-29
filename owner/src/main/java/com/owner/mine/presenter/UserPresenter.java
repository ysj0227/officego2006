package com.owner.mine.presenter;

import android.text.TextUtils;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.common.model.UserMessageBean;
import com.officego.commonlib.common.rongcloud.RCloudSetUserInfoUtils;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.owner.mine.contract.UserContract;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class UserPresenter extends BasePresenter<UserContract.View>
        implements UserContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void getUserInfo() {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getUserMsg(new RetrofitCallback<UserMessageBean>() {
            @Override
            public void onSuccess(int code, String msg, UserMessageBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.userInfoSuccess(data);
                    if (data != null) {
                        //刷新融云头像用户信息
                        RCloudSetUserInfoUtils.refreshUserInfoCache(SpUtils.getRongChatId(), data.getNickname(), data.getAvatar());
                        SpUtils.saveHeaderImg(data.getAvatar());
                        SpUtils.saveWechat(data.getWxId() == null || TextUtils.isEmpty(data.getWxId()) ? "" : data.getWxId());
                    }
                }
            }

            @Override
            public void onFail(int code, String msg, UserMessageBean data) {
                LogCat.e(TAG, "getUserInfo onFail code=" + code + "  msg=" + msg);
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.userInfoFail(code, msg);
                }
            }
        });
    }
}