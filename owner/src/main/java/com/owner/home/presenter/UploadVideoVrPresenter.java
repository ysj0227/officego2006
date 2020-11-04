package com.owner.home.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.rpc.OfficegoApi;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.owner.home.contract.UploadVideoVrContract;

/**
 * Created by shijie
 * Date 2020/11/3
 **/
public class UploadVideoVrPresenter extends BasePresenter<UploadVideoVrContract.View>
        implements UploadVideoVrContract.Presenter {


    @Override
    public void publishBuilding(int buildingId, int isTemp, String vr) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().buildingPublishVr(buildingId, isTemp, vr,new RetrofitCallback<Object>() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
                if (isViewAttached()) {
                    mView.publishSuccess();
                    mView.hideLoadingDialog();
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
