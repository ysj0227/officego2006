package com.officego.ui.find;

import android.content.Context;

import com.officego.commonlib.base.BasePresenter;
import com.officego.commonlib.common.model.DirectoryBean;
import com.officego.commonlib.constant.Constants;
import com.officego.commonlib.retrofit.RetrofitCallback;
import com.officego.rpc.OfficegoApi;

import java.util.List;

/**
 * Created by YangShiJie
 * Data 2020/5/7.
 * Descriptions:
 **/
public class WantFindPresenter extends BasePresenter<WantFindContract.View> implements WantFindContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void getFactorList() {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().getFactor(new RetrofitCallback<List<DirectoryBean.DataBean>>() {
            @Override
            public void onSuccess(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.factorSuccess(data);
                }
            }

            @Override
            public void onFail(int code, String msg, List<DirectoryBean.DataBean> data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    if (code == Constants.DEFAULT_ERROR_CODE) {
                        mView.shortTip(msg);
                    }
                }
            }
        });
    }

    @Override
    public void save(String person, String rent, String factor) {
        mView.showLoadingDialog();
        OfficegoApi.getInstance().wantToFind(person, person, person, new RetrofitCallback<Object>() {
            @Override
            public void onSuccess(int code, String msg, Object data) {
                if (isViewAttached()) {
                    mView.hideLoadingDialog();
                    mView.saveSuccess();
                }
            }

            @Override
            public void onFail(int code, String msg, Object data) {
                mView.hideLoadingDialog();
                if (code == Constants.DEFAULT_ERROR_CODE) {
                    mView.shortTip(msg);
                }
            }
        });
    }

}
