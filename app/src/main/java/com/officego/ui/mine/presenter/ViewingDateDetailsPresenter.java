package com.officego.ui.mine.presenter;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.mine.contract.ViewingDateDetailsContract;
import com.officego.ui.mine.model.ViewingDateDetailsBean;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class ViewingDateDetailsPresenter extends BasePresenter<ViewingDateDetailsContract.View>
        implements ViewingDateDetailsContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void getViewingDateDetails(int scheduleId) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getScheduleDetails(scheduleId, new RetrofitCallback<ViewingDateDetailsBean>() {
            @Override
            public void onSuccess(int code, String msg, ViewingDateDetailsBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (data != null) {
                        mView.dateSuccess(data);
                    }
                }
            }

            @Override
            public void onFail(int code, String msg, ViewingDateDetailsBean data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                }
            }
        });
    }
}