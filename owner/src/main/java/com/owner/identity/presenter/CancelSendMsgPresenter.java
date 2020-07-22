package com.owner.identity.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.identity.contract.CancelSendMsgContract;
import com.owner.rpc.OfficegoApi;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class CancelSendMsgPresenter extends BasePresenter<CancelSendMsgContract.View>
        implements CancelSendMsgContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();


    @Override
    public void getIdentityInfo(int id) {

    }

    @Override
    public void cancelApply(int id) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().deleteUserLicence(id, new RetrofitCallback<Object>() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.cancelApplySuccess();
                }
            }

            @Override
            public void onFail(int code, String msg, Object data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (code == Constants.DEFAULT_ERROR_CODE) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }
}