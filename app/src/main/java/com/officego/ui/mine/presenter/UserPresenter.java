package com.officego.ui.mine.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.collect.contract.CollectedContract;
import com.officego.ui.collect.model.CollectBuildingBean;
import com.officego.ui.collect.model.CollectHouseBean;
import com.officego.ui.mine.contract.UserContract;
import com.officego.ui.mine.model.UserBean;

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
        OfficegoApi.getInstance().getUserMsg(new RetrofitCallback<UserBean>() {
            @Override
            public void onSuccess(int code, String msg, UserBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    SpUtils.saveHeaderImg(data.getAvatar());
                    SpUtils.saveNickName(data.getNickname());
                    mView.userInfoSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, UserBean data) {
                LogCat.e(TAG, "getUserInfo onFail code=" + code + "  msg=" + msg);
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.userInfoFail(code, msg);
                }
            }
        });
    }
}