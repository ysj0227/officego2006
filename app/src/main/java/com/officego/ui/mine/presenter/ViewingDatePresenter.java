package com.officego.ui.mine.presenter;

import com.officego.R;
import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.commonlib.utils.log.LogCat;
import com.officego.rpc.OfficegoApi;
import com.officego.ui.mine.contract.UserContract;
import com.officego.ui.mine.contract.ViewingDateContract;
import com.officego.ui.mine.model.UserBean;
import com.officego.ui.mine.model.ViewingDateBean;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/6/6.
 * Descriptions:
 **/
public class ViewingDatePresenter extends BasePresenter<ViewingDateContract.View>
        implements ViewingDateContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void getViewingDate(long startTime, long endTime) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getScheduleList(startTime, endTime, new RetrofitCallback<List<ViewingDateBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<ViewingDateBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (data != null) {
                        mView.viewingDateSuccess(data);
                    }else {
                        mView.shortTip(R.string.tip_current_no_data);
                    }
                }
            }

            @Override
            public void onFail(int code, String msg, List<ViewingDateBean.DataBean> data) {
                LogCat.e(TAG, "getViewingDate onFail code=" + code + "  msg=" + msg);
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.viewingDateFail(code, msg);
                }
            }
        });
    }

    @Override
    public void getOldViewingDate(long startTime, long endTime) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getOldScheduleList(startTime, endTime, new RetrofitCallback<List<ViewingDateBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<ViewingDateBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (data != null) {
                        mView.viewingDateSuccess(data);
                    }else {
                        mView.shortTip(R.string.tip_current_no_data);
                    }
                }
            }

            @Override
            public void onFail(int code, String msg, List<ViewingDateBean.DataBean> data) {
                LogCat.e(TAG, "getViewingDate onFail code=" + code + "  msg=" + msg);
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.viewingDateFail(code, msg);
                }
            }
        });
    }
}