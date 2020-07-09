package com.owner.mine.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.owner.mine.contract.UserContract;
import com.owner.mine.model.UserOwnerBean;
import com.owner.rpc.OfficegoApi;

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
        OfficegoApi.getInstance().getUserMsg(new RetrofitCallback<UserOwnerBean>() {
            @Override
            public void onSuccess(int code, String msg, UserOwnerBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    SpUtils.saveHeaderImg(data.getAvatar());
                    mView.userInfoSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, UserOwnerBean data) {
                LogCat.e(TAG, "getUserInfo onFail code=" + code + "  msg=" + msg);
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.userInfoFail(code, msg);
                }
            }
        });
    }
}