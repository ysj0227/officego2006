package com.owner.mine.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.SpUtils;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.mine.contract.ModifyMobileContract;
import com.owner.rpc.OfficegoApi;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class ModifyMobilePresenter extends BasePresenter<ModifyMobileContract.View>
        implements ModifyMobileContract.Presenter {

    @Override
    public void getSms(String mobile) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getSmsCode(mobile, new RetrofitCallback<Object>() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                }
            }

            @Override
            public void onFail(int code, String msg, Object data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                }
            }
        });
    }

    @Override
    public void modifyMobile(String mobile, String code) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().modifyMobile(mobile, code, new RetrofitCallback<Object>() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
                if (isViewAttached()) {
                    SpUtils.clearLoginInfo();
                    mView.hideLoadingDialog();
                    mView.modifyMobileSuccess();
                    mView.shortTip("修改成功");
                }
            }

            @Override
            public void onFail(int code, String msg, Object data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (code == Constants.DEFAULT_ERROR_CODE) {
                        mView.shortTip(msg);
                    } else {
                        mView.shortTip("修改失败，请重新提交");
                    }
                }
            }
        });
    }
}