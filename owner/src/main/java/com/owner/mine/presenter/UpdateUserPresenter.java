package com.owner.mine.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.owner.mine.contract.UpdateUserContract;
import com.owner.mine.model.AvatarBean;
import com.owner.rpc.OfficegoApi;

import java.io.File;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class UpdateUserPresenter extends BasePresenter<UpdateUserContract.View>
        implements UpdateUserContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();


    @Override
    public void updateAvatar(File file) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().updateAvatar(file, new RetrofitCallback<AvatarBean>() {
            @Override
            public void onSuccess(int code, String msg, AvatarBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.UpdateAvatarSuccess(data.getAvatar());
                }
            }

            @Override
            public void onFail(int code, String msg, AvatarBean data) {
                LogCat.e(TAG, "getUserInfo onFail code=" + code + "  msg=" + msg);
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.UpdateUserFail(code, msg);
                }
            }
        });
    }

    @Override
    public void UpdateUserInfo(String realName, String sex,String company, String job) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().updateUserData(realName, sex, company,job,new RetrofitCallback<Object>() {
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