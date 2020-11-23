package com.owner.mine.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.owner.UploadImageBean;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.owner.mine.contract.UpdateUserContract;
import com.owner.rpc.OfficegoApi;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class UpdateUserPresenter extends BasePresenter<UpdateUserContract.View>
        implements UpdateUserContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();


    @Override
    public void updateAvatar(String path) {
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().uploadSingleImageUrl(5, path, new RetrofitCallback<UploadImageBean>() {
            @Override
            public void onSuccess(int code, String msg, UploadImageBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (data != null && data.getUrls().size() > 0) {
                        mView.UpdateAvatarSuccess(data.getUrls().get(0).getUrl());
                    }
                }
            }

            @Override
            public void onFail(int code, String msg, UploadImageBean data) {
                LogCat.e(TAG, "getUserInfo onFail code=" + code + "  msg=" + msg);
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.UpdateUserFail(code, msg);
                }
            }
        });
    }

    @Override
    public void UpdateUserInfo(String avatar, String nickname, String sex, String company, String job, String wx) {
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().updateUserInfo(avatar,nickname, sex,
                company, job, wx, new RetrofitCallback<Object>() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.UpdateUserSuccess();
                }
            }

            @Override
            public void onFail(int code, String msg, Object data) {
                LogCat.e(TAG, "getUserInfo onFail code=" + code + "  msg=" + msg);
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.UpdateUserFail(code, msg);
                }
            }
        });
    }
}