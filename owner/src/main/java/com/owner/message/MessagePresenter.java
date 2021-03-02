package com.owner.message;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.ServiceBean;
import com.officego.commonlib.common.model.UserMessageBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class MessagePresenter extends BasePresenter<MessageContract.View>
        implements MessageContract.Presenter {

    @Override
    public void getUserInfo() {
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().getUserMsg(new RetrofitCallback<UserMessageBean>() {
            @Override
            public void onSuccess(int code, String msg, UserMessageBean data) {
                if (isViewAttached()) {
                    mView.userInfoSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, UserMessageBean data) {
            }
        });
    }

    @Override
    public void getSupportMobile() {
        mView.showLoadingDialog();
        com.officego.commonlib.common.rpc.OfficegoApi.getInstance().serviceMobile(new RetrofitCallback<ServiceBean>() {
            @Override
            public void onSuccess(int code, String msg, ServiceBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.supportMobileSuccess(data.getOwnerConsultation());
                }
            }

            @Override
            public void onFail(int code, String msg, ServiceBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.supportMobileSuccess(Constants.SERVICE_SUPPORT);
                }
            }
        });
    }
}